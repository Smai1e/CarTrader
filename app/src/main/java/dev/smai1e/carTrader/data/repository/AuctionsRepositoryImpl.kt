package dev.smai1e.carTrader.data.repository

import android.net.Uri
import dev.smai1e.carTrader.data.MergeStrategy
import dev.smai1e.carTrader.data.ProtocolManager
import dev.smai1e.carTrader.data.localCache.dao.AuctionDao
import dev.smai1e.carTrader.data.localCache.models.AuctionDBO
import dev.smai1e.carTrader.data.network.models.request.AuctionRequest
import dev.smai1e.carTrader.data.network.models.request.SearchParametersRequest
import dev.smai1e.carTrader.data.network.api.AuctionsService
import dev.smai1e.carTrader.data.network.api.ImageUrl
import dev.smai1e.carTrader.data.network.models.response.AuctionResponse
import dev.smai1e.carTrader.data.toAuction
import dev.smai1e.carTrader.data.toAuctionDBO
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.Auction
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.data.network.NetworkResult
import dev.smai1e.carTrader.domain.repositoryInterfaces.AuctionsRepository
import dev.smai1e.carTrader.domain.repositoryInterfaces.RequestResultFlow
import dev.smai1e.carTrader.data.toRequestResult
import dev.smai1e.carTrader.di.IoDispatcher
import dev.smai1e.carTrader.domain.repositoryInterfaces.FileName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Implementation a [AuctionsRepository] which uses
 * [AuctionDao] and [AuctionsService] as data sources.
 */
class AuctionsRepositoryImpl @Inject constructor(
    private val auctionsService: AuctionsService,
    private val auctionsDao: AuctionDao,
    private val protocolManager: ProtocolManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuctionsRepository {

    /**
     * Downloads the auction protocol that match specified [auctionId]
     * and returns its [FileName] as a [RequestResult].
     */
    override suspend fun downloadProtocol(
        auctionId: Long,
        uri: Uri
    ): RequestResult<FileName, DataError> {

        val response = auctionsService.downloadProtocol(auctionId).toRequestResult()
        return if (response is RequestResult.Success) {
            protocolManager.saveProtocolToDirectory(uri, auctionId, response.data.bytes())
        } else {
            response.map { "crutch" }
        }
    }

    /**
     * Returns all auctions owned by the user as a [RequestResult].
     */
    override suspend fun fetchOwnAuctions(): RequestResult<List<Auction>, DataError> {
        return auctionsService.fetchOwnAuctions()
            .toRequestResult()
            .map { dtos ->
                dtos.map { it.toAuction() }
            }
    }

    /**
     * Returns auctions in which the user is a participant as a [RequestResult].
     */
    override suspend fun fetchAuctionsParticipant(): RequestResult<List<Auction>, DataError> {
        return auctionsService.fetchAuctionsParticipant()
            .toRequestResult()
            .map { dtos ->
                dtos.map { it.toAuction() }
            }
    }

    /**
     * Uploads images for the auction.
     */
    override suspend fun uploadImages(parts: List<MultipartBody.Part>): RequestResult<List<ImageUrl>, DataError> {
        return auctionsService.uploadImages(parts)
            .toRequestResult()
    }

    /**
     * Creates a new auction with the provided data.
     */
    override suspend fun createAuction(auctionData: AuctionRequest): RequestResult<Unit, DataError> {
        return auctionsService.createAuction(auctionData)
            .toRequestResult()
            .map { Unit }
    }

    /**
     * Returns the auction that match the specified [id]
     * as a stream of [RequestResult].
     */
    override fun fetchAuctionById(
        id: Long,
        mergeStrategy: MergeStrategy<Auction, Auction, DataError>
    ): RequestResultFlow<Auction, DataError> {

        val cachedAuction = getFromDatabase(id)
        val remoteAuction = getFromServer(id)

        return cachedAuction.combine(remoteAuction, mergeStrategy::mergeSame)
    }

    /**
     * Returns auctions that match the specified [searchParams]
     * as a stream of [RequestResult].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchAllAuctions(
        searchParams: SearchParametersRequest,
        mergeStrategy: MergeStrategy<List<Auction>, List<Auction>, DataError>
    ): RequestResultFlow<List<Auction>, DataError> {

        val cachedAuctions = getAllFromDatabase(searchParams)
        val remoteAuctions = getAllFromServer(searchParams)

        return cachedAuctions.combine(remoteAuctions, mergeStrategy::mergeSame)
            .distinctUntilChanged()
            .flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    observeAllAuctionsFromDatabase(searchParams)
                        .map { dbos -> dbos.map { it.toAuction() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    /**
     * Returns auction from local database that match the specified [id]
     * as a stream of [RequestResult].
     */
    private fun getFromDatabase(id: Long): RequestResultFlow<Auction, DataError> {
        val dbRequest = flow { emit(auctionsDao.fetchAuctionById(id)) }
            .map { RequestResult.Success<AuctionDBO, DataError>(it) }
            .catch { RequestResult.Error<AuctionDBO, DataError>(DataError.LocalStorageException(it)) }
        val start = flowOf<RequestResult<AuctionDBO, DataError>>(RequestResult.Loading)

        return merge(start, dbRequest).map { result ->
            result.map { auctionDbo ->
                auctionDbo.toAuction()
            }
        }
    }

    /**
     * Returns auctions from local database that match the specified [searchParams]
     * as a stream of [RequestResult].
     */
    private fun getAllFromDatabase(searchParams: SearchParametersRequest): RequestResultFlow<List<Auction>, DataError> {
        return flow { emit(getAllAuctionsFromDatabase(searchParams)) }
            .map { RequestResult.Success<List<AuctionDBO>, DataError>(it) }
            .map { result ->
                result.map { dbos ->
                    dbos.map { it.toAuction() }
                }
            }
            .catch { RequestResult.Error<List<AuctionDBO>, DataError>(DataError.LocalStorageException(it)) }
    }

    /**
     * Returns auction from remote storage that match the specified [id]
     * as a stream of [RequestResult].
     */
    private fun getFromServer(id: Long): RequestResultFlow<Auction, DataError> {
        val apiRequest = flow { emit(auctionsService.fetchAuctionById(id)) }
            .onEach { result ->
                if (result is NetworkResult.ApiSuccess) {
                    saveNetResponseToCache(result.data)
                }
            }
            .map { result -> result.toRequestResult() }
        val start = flowOf<RequestResult<AuctionResponse, DataError>>(RequestResult.Loading)

        return merge(start, apiRequest).map { result ->
            result.map { auctionDto ->
                auctionDto.toAuction()
            }
        }
    }

    /**
     * Returns auctions from remote storage that match the specified [searchParams]
     * as a stream of [RequestResult].
     */
    private fun getAllFromServer(searchParams: SearchParametersRequest): RequestResultFlow<List<Auction>, DataError> {
        val apiRequest = flow { emit(auctionsService.fetchAuctions(searchParams)) }
            .onEach { result ->
                if (result is NetworkResult.ApiSuccess) {
                    saveNetResponseToCache(result.data)
                }
            }
            .map { result -> result.toRequestResult() }
        val start = flowOf<RequestResult<List<AuctionResponse>, DataError>>(RequestResult.Loading)

        return merge(start, apiRequest).map { result ->
            result.map { dtos ->
                dtos.map { it.toAuction() }
            }
        }
    }

    /**
     * Inserts the [AuctionResponse] list into the local database.
     */
    private suspend fun saveNetResponseToCache(data: List<AuctionResponse>) {
        val dbos = data.map { auctionDto ->
            auctionDto.toAuctionDBO()
        }
        auctionsDao.insertAuctions(dbos)
    }

    /**
     * Inserts the [AuctionResponse] into the local database.
     */
    private suspend fun saveNetResponseToCache(data: AuctionResponse) {
        val dbo = data.toAuctionDBO()
        auctionsDao.insertAuction(dbo)
    }

    private suspend fun getAllAuctionsFromDatabase(searchParams: SearchParametersRequest) =
        auctionsDao.fetchAllAuctions(
            searchParams.searchRequest,
            searchParams.minBidStart,
            searchParams.minBidEnd,
            searchParams.brand,
            searchParams.model,
            searchParams.manufacturerDateStart,
            searchParams.manufacturerDateEnd,
            searchParams.color?.name,
            searchParams.horsepowerStart,
            searchParams.horsepowerEnd,
            searchParams.mileageStart,
            searchParams.mileageEnd,
            searchParams.driveWheel?.name,
            searchParams.gearbox?.name
        )

    private fun observeAllAuctionsFromDatabase(searchParams: SearchParametersRequest) =
        auctionsDao.observeAllAuctions(
            searchParams.searchRequest,
            searchParams.minBidStart,
            searchParams.minBidEnd,
            searchParams.brand,
            searchParams.model,
            searchParams.manufacturerDateStart,
            searchParams.manufacturerDateEnd,
            searchParams.color?.name,
            searchParams.horsepowerStart,
            searchParams.horsepowerEnd,
            searchParams.mileageStart,
            searchParams.mileageEnd,
            searchParams.driveWheel?.name,
            searchParams.gearbox?.name
        )
}