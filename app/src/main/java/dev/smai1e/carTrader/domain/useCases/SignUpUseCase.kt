package dev.smai1e.carTrader.domain.useCases

import dev.smai1e.carTrader.data.toSignUpRequest
import dev.smai1e.carTrader.di.DefaultDispatcher
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.errorTypes.RootError
import dev.smai1e.carTrader.domain.validators.SignUpDataValidator
import dev.smai1e.carTrader.domain.models.SignUpData
import dev.smai1e.carTrader.domain.repositoryInterfaces.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A use case that allows the user to register
 * after checking his information for validity.
 */
class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val validator: SignUpDataValidator,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(data: SignUpData): RequestResult<Unit, RootError> {
        return withContext(defaultDispatcher) {
            val validationResult = validator.validate(data)
            if (validationResult is RequestResult.Error) {
                return@withContext validationResult
            }
            return@withContext data
                .toSignUpRequest()
                .let { userRepository.signUp(it) }
        }
    }
}