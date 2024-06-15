package dev.smai1e.carTrader.data.network.api

import dev.smai1e.carTrader.data.network.models.request.BidRequest
import dev.smai1e.carTrader.data.network.models.response.BidResponse
import dev.smai1e.carTrader.data.network.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API for fetching bids.
 */
interface BidsService {

    /**
     * Get all bids that match the specified auction [id].
     */
    @GET("bids/{id}")
    suspend fun fetchBidsByAuctionId(
        @Path("id") id: Long
    ): NetworkResult<List<BidResponse>>

    /**
     * Make a [bid] at an auction.
     */
    @POST("bid")
    suspend fun insertBid(
        @Body bid: BidRequest
    ): NetworkResult<BidResponse>
}