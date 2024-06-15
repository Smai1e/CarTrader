package dev.smai1e.carTrader.domain.repositoryInterfaces

import android.net.Uri
import dev.smai1e.carTrader.data.MergeStrategy
import dev.smai1e.carTrader.data.ResponseMergeStrategy
import dev.smai1e.carTrader.data.network.models.request.AuctionRequest
import dev.smai1e.carTrader.data.network.models.request.SearchParametersRequest
import dev.smai1e.carTrader.data.network.api.ImageUrl
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.Auction
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

typealias RequestResultFlow<T, E> = Flow<RequestResult<T, E>>
typealias FileName = String

/**
 * Data layer implementation for [Auction]
 */
interface AuctionsRepository {

    /**
     * Returns auctions that match the specified [searchParams]
     * as a stream of [RequestResult].
     */
    fun fetchAllAuctions(
        searchParams: SearchParametersRequest,
        mergeStrategy: MergeStrategy<List<Auction>, List<Auction>, DataError> = ResponseMergeStrategy()
    ): RequestResultFlow<List<Auction>, DataError>

    /**
     * Returns the auction that match the specified [id]
     * as a stream of [RequestResult].
     */
    fun fetchAuctionById(
        id: Long,
        mergeStrategy: MergeStrategy<Auction, Auction, DataError> = ResponseMergeStrategy()
    ): RequestResultFlow<Auction, DataError>

    /**
     * Uploads images for the auction.
     */
    suspend fun uploadImages(parts: List<MultipartBody.Part>): RequestResult<List<ImageUrl>, DataError>

    /**
     * Downloads the auction protocol that match specified [auctionId]
     * and returns its [FileName] as a [RequestResult].
     */
    suspend fun downloadProtocol(auctionId: Long, uri: Uri): RequestResult<FileName, DataError>

    /**
     * Creates a new auction with the provided data.
     */
    suspend fun createAuction(auctionData: AuctionRequest): RequestResult<Unit, DataError>

    /**
     * Returns all auctions owned by the user as a [RequestResult].
     */
    suspend fun fetchOwnAuctions(): RequestResult<List<Auction>, DataError>

    /**
     * Returns auctions in which the user is a participant
     * as a [RequestResult].
     */
    suspend fun fetchAuctionsParticipant(): RequestResult<List<Auction>, DataError>
}