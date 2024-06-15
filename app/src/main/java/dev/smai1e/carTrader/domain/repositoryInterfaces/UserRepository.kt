package dev.smai1e.carTrader.domain.repositoryInterfaces

import dev.smai1e.carTrader.data.MergeStrategy
import dev.smai1e.carTrader.data.ResponseMergeStrategy
import dev.smai1e.carTrader.data.network.models.request.SignInRequest
import dev.smai1e.carTrader.data.network.models.request.SignUpRequest
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.UserInfo

typealias Token = String

/**
 * Data layer implementation for [UserInfo].
 */
interface UserRepository {

    /**
     * Sings in and returns the [Token] as a [RequestResult].
     */
    suspend fun signIn(data: SignInRequest): RequestResult<Token, DataError>

    /**
     * Create a new user
     */
    suspend fun signUp(data: SignUpRequest): RequestResult<Unit, DataError>

    /**
     * Returns [UserInfo] of the current signed-in user
     * as a [RequestResult].
     */
    suspend fun fetchUserInfo(): RequestResult<UserInfo, DataError>

    /**
     * Returns seller info that match the specified seller [id]
     * as a stream of [RequestResult].
     */
    fun fetchSellerInfo(
        id: Long,
        mergeStrategy: MergeStrategy<UserInfo, UserInfo, DataError> = ResponseMergeStrategy()
    ): RequestResultFlow<UserInfo, DataError>

    /**
     * Returns bidders that match the specified [auctionId]
     * as a stream of [RequestResult].
     */
    fun fetchBiddersByAuctionId(
        auctionId: Long,
        mergeStrategy: MergeStrategy<List<UserInfo>, List<UserInfo>, DataError> = ResponseMergeStrategy()
    ): RequestResultFlow<List<UserInfo>, DataError>
}