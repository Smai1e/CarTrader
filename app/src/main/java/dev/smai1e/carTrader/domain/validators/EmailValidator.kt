package dev.smai1e.carTrader.domain.validators

import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.errorTypes.ValidationError
import javax.inject.Inject

/**
 * The validator of the email.
 * - If successful, it returns:
 *      - RequestResult.Success<Unit>.
 * - In case of error:
 *      - RequestResult.Error<ValidationError.EmailError>
 */
class EmailValidator @Inject constructor() {

    fun validate(email: String): RequestResult<Unit, ValidationError.EmailError> {
        if (!emailRegex.matches(email)) {
            return RequestResult.Error(ValidationError.EmailError.INCORRECT_EMAIL)
        }
        return RequestResult.Success(Unit)
    }

    private companion object {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
    }
}