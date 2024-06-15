package dev.smai1e.carTrader.data.network.api

import dev.smai1e.carTrader.data.network.models.request.AuctionRequest
import dev.smai1e.carTrader.data.network.models.request.SearchParametersRequest
import dev.smai1e.carTrader.data.network.models.response.AuctionResponse
import dev.smai1e.carTrader.data.network.NetworkResult
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

typealias ImageUrl = String

/**
 * API for fetching auctions.
 */
interface AuctionsService {

    /**
     * Get all auctions owned by the user.
     */
    @GET("auctions/owner")
    suspend fun fetchOwnAuctions(): NetworkResult<List<AuctionResponse>>

    /**
     * Get all auctions in which the user is a participant.
     */
    @GET("auctions/participant")
    suspend fun fetchAuctionsParticipant(): NetworkResult<List<AuctionResponse>>

    /**
     * Get the auction that match the specified [auctionId].
     */
    @GET("auction/{id}")
    suspend fun fetchAuctionById(
        @Path("id") auctionId: Long
    ): NetworkResult<AuctionResponse>

    /**
     * Downloads the auction protocol that match specified [auctionId].
     */
    @GET("auction/{id}/protocol")
    suspend fun downloadProtocol(
        @Path("id") auctionId: Long
    ): NetworkResult<ResponseBody>

    /**
     * Creates a new auction with the provided data.
     */
    @POST("auction")
    suspend fun createAuction(
        @Body auction: AuctionRequest
    ): NetworkResult<AuctionResponse>

    /**
     * Get a list of auctions that match the specified [searchParams].
     */
    @POST("auctions")
    suspend fun fetchAuctions(
        @Body searchParams: SearchParametersRequest
    ): NetworkResult<List<AuctionResponse>>

    /**
     * Uploads images for the auction.
     */
    @Multipart
    @POST("images")
    suspend fun uploadImages(
        @Part images: List<MultipartBody.Part>
    ): NetworkResult<List<ImageUrl>>
}