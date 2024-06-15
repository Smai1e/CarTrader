package dev.smai1e.carTrader.data.repository

import dev.smai1e.carTrader.data.MergeStrategy
import dev.smai1e.carTrader.data.localCache.dao.UserDao
import dev.smai1e.carTrader.data.localCache.models.UserDBO
import dev.smai1e.carTrader.data.network.api.UserService
import dev.smai1e.carTrader.data.network.models.request.SignInRequest
import dev.smai1e.carTrader.data.network.models.request.SignUpRequest
import dev.smai1e.carTrader.data.network.models.response.UserInfoResponse
import dev.smai1e.carTrader.data.toUser
import dev.smai1e.carTrader.data.toUserDBO
import dev.smai1e.carTrader.di.IoDispatcher
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.UserInfo
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.data.network.NetworkResult
import dev.smai1e.carTrader.domain.repositoryInterfaces.RequestResultFlow
import dev.smai1e.carTrader.domain.repositoryInterfaces.Token
import dev.smai1e.carTrader.domain.repositoryInterfaces.UserRepository
import dev.smai1e.carTrader.data.toRequestResult
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
import javax.inject.Inject

/**
 * Implementation a [UserRepository] which uses
 * [UserDao] and [UserService] as data sources.
 */
class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val userDao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    /**
     * Sings-in and returns the [Token] as a [RequestResult].
     */
    override suspend fun signIn(data: SignInRequest): RequestResult<Token, DataError> {
        return userService.signIn(data)
            .toRequestResult()
            .map { it.token }
    }

    /**
     * Create a new user
     */
    override suspend fun signUp(data: SignUpRequest): RequestResult<Unit, DataError> {
        return userService.signUp(data)
            .toRequestResult()
            .map { Unit }
    }

    /**
     * Returns [UserInfo] of the current signed-in user
     * as a [RequestResult].
     */
    override suspend fun fetchUserInfo(): RequestResult<UserInfo, DataError> {
        return userService.fetchUserInfo()
            .toRequestResult()
            .map { it.toUser() }
    }

    /**
     * Returns [UserInfo] that match the specified seller [id]
     * as a stream of [RequestResult].
     */
    override fun fetchSellerInfo(
        id: Long,
        mergeStrategy: MergeStrategy<UserInfo, UserInfo, DataError>
    ): RequestResultFlow<UserInfo, DataError> {

        val cachedUserInfo = getFromDatabase(id)
        val remoteUserInfo = getFromServer(id)

        return cachedUserInfo.combine(remoteUserInfo, mergeStrategy::mergeSame)
    }

    /**
     * Returns bidders that match the specified [auctionId]
     * as a stream of [RequestResult].
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchBiddersByAuctionId(
        auctionId: Long,
        mergeStrategy: MergeStrategy<List<UserInfo>, List<UserInfo>, DataError>
    ): RequestResultFlow<List<UserInfo>, DataError> {

        val cachedBidders = getBiddersFromDatabase(auctionId)
        val remoteBidders = getBiddersFromServer(auctionId)

        return cachedBidders.combine(remoteBidders, mergeStrategy::mergeSame)
            .distinctUntilChanged()
            .flatMapLatest { result ->
                if (result is RequestResult.Success) {
                    userDao.observeBiddersByAuctionId(auctionId)
                        .map { dbos -> dbos.map { it.toUser() } }
                        .map { RequestResult.Success(it) }
                } else {
                    flowOf(result)
                }
            }
    }

    /**
     * Returns seller info from local database
     * that match the specified seller [id]
     * as a stream of [RequestResult].
     */
    private fun getFromDatabase(id: Long): RequestResultFlow<UserInfo, DataError> {
        val dbRequest = flow { emit(userDao.fetchUserById(id)) }
            .map { RequestResult.Success<UserDBO, DataError>(it) }
            .catch { RequestResult.Error<UserDBO, DataError>(DataError.LocalStorageException(it)) }
        val start = flowOf<RequestResult<UserDBO, DataError>>(RequestResult.Loading)

        return merge(start, dbRequest).map { result ->
            result.map { userDbo ->
                userDbo.toUser()
            }
        }
    }

    /**
     * Returns bidders from local database
     * that match the specified [auctionId]
     * as a stream of [RequestResult].
     */
    private fun getBiddersFromDatabase(auctionId: Long): RequestResultFlow<List<UserInfo>, DataError> {
        val dbRequest = flow { emit(userDao.fetchBiddersByAuctionId(auctionId)) }
            .map { RequestResult.Success<List<UserDBO>, DataError>(it) }
            .catch { RequestResult.Error<List<UserDBO>, DataError>(DataError.LocalStorageException(it)) }
        val start = flowOf<RequestResult<List<UserDBO>, DataError>>(RequestResult.Loading)

        return merge(start, dbRequest).map { result ->
            result.map { dbos ->
                dbos.map { it.toUser() }
            }
        }
    }

    /**
     * Returns seller info from remote storage
     * that match the specified seller [id]
     * as a stream of [RequestResult].
     */
    private fun getFromServer(id: Long): RequestResultFlow<UserInfo, DataError> {
        val apiRequest = flow { emit(userService.fetchSellerInfo(id)) }
            .onEach { result ->
                if (result is NetworkResult.ApiSuccess) {
                    saveNetResponseToCache(result.data)
                }
            }
            .map { result -> result.toRequestResult() }
        val start = flowOf<RequestResult<UserInfoResponse, DataError>>(RequestResult.Loading)

        return merge(start, apiRequest).map { result ->
            result.map { userDto ->
                userDto.toUser()
            }
        }
    }

    /**
     * Returns bidders from remote storage
     * that match the specified  [auctionId]
     * as a stream of [RequestResult].
     */
    private fun getBiddersFromServer(auctionId: Long): RequestResultFlow<List<UserInfo>, DataError> {
        val apiRequest = flow { emit(userService.fetchBiddersByAuctionId(auctionId)) }
            .onEach { result ->
                if (result is NetworkResult.ApiSuccess) {
                    saveNetResponseToCache(result.data)
                }
            }
            .map { result -> result.toRequestResult() }
        val start = flowOf<RequestResult<List<UserInfoResponse>, DataError>>(RequestResult.Loading)

        return merge(start, apiRequest).map { result ->
            result.map { dtos ->
                dtos.map { it.toUser() }
            }
        }
    }

    /**
     * Inserts the seller info into the local database.
     */
    private suspend fun saveNetResponseToCache(data: UserInfoResponse) {
        val dbo = data.toUserDBO()
        userDao.insertUser(dbo)
    }

    /**
     * Inserts the bidders into the local database.
     */
    private suspend fun saveNetResponseToCache(data: List<UserInfoResponse>) {
        val dbos = data.map { userDto ->
            userDto.toUserDBO()
        }
        userDao.insertUsers(dbos)
    }
}