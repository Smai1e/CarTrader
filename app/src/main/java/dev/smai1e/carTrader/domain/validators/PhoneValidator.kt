package dev.smai1e.carTrader.domain.validators

import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.errorTypes.RootError
import dev.smai1e.carTrader.domain.errorTypes.ValidationError
import javax.inject.Inject

/**
 * The validator of the phone number.
 * - If successful, it returns:
 *      - RequestResult.Success<Unit>.
 * - In case of error:
 *      - RequestResult.Error<ValidationError.PhoneError>
 */
class PhoneValidator @Inject constructor() {

    fun validate(phone: String): RequestResult<Unit, RootError> {
        if (!phone.matches(phoneRegex)) {
            return RequestResult.Error(ValidationError.PhoneError.INCORRECT_PHONE)
        }
        return RequestResult.Success(Unit)
    }

    private companion object {
        val phoneRegex = Regex("^(?:[0-9\\-\\(\\)\\/\\.]\\s?){6,15}[0-9]{1}\$")
    }
}