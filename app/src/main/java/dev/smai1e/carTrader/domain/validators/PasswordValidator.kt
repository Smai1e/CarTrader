package dev.smai1e.carTrader.domain.validators

import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.errorTypes.ValidationError
import javax.inject.Inject

/**
 * The validator of the password.
 * - If successful, it returns:
 *      - RequestResult.Success<Unit>.
 * - In case of error:
 *      - RequestResult.Error<ValidationError.PasswordError>
 */
class PasswordValidator @Inject constructor() {

    fun validate(password: String): RequestResult<Unit, ValidationError.PasswordError> {
        if (password.length < 9) {
            return RequestResult.Error(ValidationError.PasswordError.TOO_SHORT)
        }

        val hasUppercaseChar = password.any { it.isUpperCase() }
        if (!hasUppercaseChar) {
            return RequestResult.Error(ValidationError.PasswordError.NO_UPPERCASE)
        }

        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) {
            return RequestResult.Error(ValidationError.PasswordError.NO_DIGIT)
        }

        return RequestResult.Success(Unit)
    }
}