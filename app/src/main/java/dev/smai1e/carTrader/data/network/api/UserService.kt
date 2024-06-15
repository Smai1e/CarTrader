package dev.smai1e.carTrader.data.network.api

import dev.smai1e.carTrader.data.network.models.request.SignInRequest
import dev.smai1e.carTrader.data.network.models.request.SignUpRequest
import dev.smai1e.carTrader.data.network.models.response.SignInResponse
import dev.smai1e.carTrader.data.network.models.response.SignUpResponse
import dev.smai1e.carTrader.data.network.models.response.UserInfoResponse
import dev.smai1e.carTrader.data.network.NetworkResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API for fetching users.
 */
interface UserService {

    /**
     * Execute sign-in request.
     */
    @POST("signin")
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ): NetworkResult<SignInResponse>

    /**
     * Create a new user.
     */
    @POST("signup")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): NetworkResult<SignUpResponse>

    /**
     * Get the user info of the current signed-in user.
     */
    @GET("user/info")
    suspend fun fetchUserInfo(): NetworkResult<UserInfoResponse>

    /**
     * Get the user info that match the specified [id].
     */
    @GET("user/{id}/seller/info")
    suspend fun fetchSellerInfo(
        @Path("id") id: Long
    ): NetworkResult<UserInfoResponse>

    /**
     * Get bidders that match the specified auction [id].
     */
    @GET("auction/{id}/bidders")
    suspend fun fetchBiddersByAuctionId(
        @Path("id") id: Long
    ): NetworkResult<List<UserInfoResponse>>
}