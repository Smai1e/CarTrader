package dev.smai1e.carTrader.data.repository

import dev.smai1e.carTrader.data.BidsMergeStrategy
import dev.smai1e.carTrader.data.MergeStrategy
import dev.smai1e.carTrader.data.localCache.dao.BidDao
import dev.smai1e.carTrader.data.localCache.models.BidDBO
import dev.smai1e.carTrader.data.network.models.request.BidRequest
import dev.smai1e.carTrader.data.network.api.BidsService
import dev.smai1e.carTrader.data.network.api.BidsSocketService
import dev.smai1e.carTrader.data.network.models.response.BidResponse
import dev.smai1e.carTrader.data.toBid
import dev.smai1e.carTrader.data.toBidDBO
import dev.smai1e.carTrader.di.IoDispatcher
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.Bid
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.data.network.NetworkResult
import dev.smai1e.carTrader.domain.repositoryInterfaces.BidsRepository
import dev.smai1e.carTrader.domain.repositoryInterfaces.RequestResultFlow
import dev.smai1e.carTrader.data.toRequestResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Implementation a [BidsRepository] which uses
 * [BidsService], [BidsSocketService] and [BidDao] as data sources.
 */
class BidsRepositoryImpl @Inject constructor(
    private val bidsService: BidsService,
    private val bidsSocketService: BidsSocketService,
    private val bidDao: BidDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BidsRepository {

    /**
     * Returns the [Bid]s list that match the specified auction [id]
     * as a stream of [RequestResult].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchBidsByAuctionId(
        id: Long,
        mergeStrategy: MergeStrategy<List<Bid>, List<Bid>, DataError>
    ): RequestResultFlow<List<Bid>, DataError> {

        val cachedBids = getAllFromDatabase(id)
        val remoteBids = getAllFromServer(id)

        return cachedBids.combine(remoteBids, mergeStrategy::mergeSame)
            .distinctUntilChanged()
            .flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    bidDao.observeBidsByAuctionId(id)
                        .map { dbos -> dbos.map { it.toBid() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    /**
     * Returns bids from local database that match the specified auction [id]
     * as a stream of [RequestResult].
     */
    private fun getAllFromDatabase(id: Long): RequestResultFlow<List<Bid>, DataError> {
        val dbRequest = flow { emit(bidDao.fetchBidsByAuctionId(id)) }
            .map { RequestResult.Success<List<BidDBO>, DataError>(it) }
            .catch { RequestResult.Error<List<BidDBO>, DataError>(DataError.LocalStorageException(it))}
        val start = flowOf<RequestResult<List<BidDBO>, DataError>>(RequestResult.Loading)

        return merge(start, dbRequest).map { result ->
            result.map { dbos ->
                dbos.map { it.toBid() }
            }
        }
    }

    /**
     * Returns bids from remote storage (joined apiRequest with webSocketRequest)
     * that match the specified auction [id] as a stream of [RequestResult].
     */
    private fun getAllFromServer(
        id: Long,
        mergeStrategy: MergeStrategy<List<BidResponse>, Unit, DataError> = BidsMergeStrategy()
    ): RequestResultFlow<List<Bid>, DataError> {

        val apiRequest = fetchBidsFromServer(id)
        val socketRequest = fetchBidsFromWebSocket(id)

        val resultRequest = apiRequest.combine(socketRequest, mergeStrategy::mergeDifferent)
        val start = flowOf<RequestResult<List<BidResponse>, DataError>>(RequestResult.Loading)

        return merge(start, resultRequest).map { result ->
            result.map { dtos ->
                dtos.map { it.toBid() }
            }
        }
    }

    /**
     * Returns bids from api that match the specified auction [id]
     * as a stream of [RequestResult].
     */
    private fun fetchBidsFromServer(id: Long): RequestResultFlow<List<BidResponse>, DataError> {
        return flow { emit(bidsService.fetchBidsByAuctionId(id)) }
            .onEach { result ->
                if (result is NetworkResult.ApiSuccess) {
                    saveNetResponseToCache(result.data)
                }
            }
            .map { result -> result.toRequestResult() }
    }

    /**
     * Starts receiving bids from webSocket.
     */
    private fun fetchBidsFromWebSocket(id: Long): RequestResultFlow<Unit, DataError> {
        return flow { emit(connect(id)) }
            .onEach { result ->
                if (result is RequestResult.Success<Unit, DataError>) {
                    listenBids()
                }
            }
            .catch { disconnect() }
    }

    /**
     * Receiving bids from webSocket.
     */
    private suspend fun listenBids() {
        bidsSocketService.observeBids()
            .onEach { result -> saveNetResponseToCache(result) }
            .collect()
    }

    /**
     * Inserts the [BidResponse] list into the local database.
     */
    private suspend fun saveNetResponseToCache(data: List<BidResponse>) {
        val dbos = data.map { bidDto ->
            bidDto.toBidDBO()
        }
        bidDao.insertBids(dbos)
    }

    /**
     * Inserts the [BidResponse] into the local database.
     */
    private suspend fun saveNetResponseToCache(data: BidResponse) {
        val dbo = data.toBidDBO()
        bidDao.insertBid(dbo)
    }

    /**
     * Connects to websocket.
     */
    override suspend fun connect(auctionId: Long): RequestResult<Unit, DataError> {
        return bidsSocketService.initSession(auctionId)
            .toRequestResult()
    }

    /**
     * Makes a bid at an auction.
     */
    override suspend fun insertBid(bid: BidRequest): RequestResult<Bid, DataError> {
        return bidsService.insertBid(bid)
            .toRequestResult()
            .map { it.toBid() }
    }

    /**
     * Stops receiving bids.
     */
    override suspend fun disconnect() {
        bidsSocketService.closeSession()
    }
}