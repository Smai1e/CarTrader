package dev.smai1e.carTrader.domain.useCases

import dev.smai1e.carTrader.data.network.models.request.SignInRequest
import dev.smai1e.carTrader.domain.repositoryInterfaces.UserRepository
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.di.DefaultDispatcher
import dev.smai1e.carTrader.domain.repositoryInterfaces.Token
import dev.smai1e.carTrader.domain.repositoryInterfaces.TokenRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * A use case that logs in to the application
 * and stores the auth token in the local storage.
 */
class SignInUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(email: String, password: String): RequestResult<Token, DataError> {
        val result = SignInRequest(email, password).let { data ->
            userRepository.signIn(data)
        }
        if (result is RequestResult.Success) {
            tokenRepository.saveAuthToken(result.data)
        }
        return result
    }
}