package dev.smai1e.carTrader.domain.repositoryInterfaces

import dev.smai1e.carTrader.data.MergeStrategy
import dev.smai1e.carTrader.data.ResponseMergeStrategy
import dev.smai1e.carTrader.data.network.models.request.BidRequest
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.Bid

/**
 * Data layer implementation for [Bid].
 */
interface BidsRepository {

    /**
     * Connects to websocket.
     */
    suspend fun connect(auctionId: Long): RequestResult<Unit, DataError>

    /**
     * Returns the [Bid] list that match the specified auction [id]
     * as a stream of [RequestResult].
     */
    fun fetchBidsByAuctionId(
        id: Long,
        mergeStrategy: MergeStrategy<List<Bid>,List<Bid>, DataError> = ResponseMergeStrategy()
    ): RequestResultFlow<List<Bid>, DataError>

    /**
     * Makes a bid at an auction.
     */
    suspend fun insertBid(bid: BidRequest): RequestResult<Bid, DataError>

    /**
     * Stops receiving bids.
     */
    suspend fun disconnect()
}