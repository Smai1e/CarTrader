package dev.smai1e.carTrader.domain.validators

import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.errorTypes.RootError
import dev.smai1e.carTrader.domain.errorTypes.ValidationError
import dev.smai1e.carTrader.domain.models.SignUpData
import javax.inject.Inject

/**
 * The validator of the sign-up data.
 * - If successful, it returns:
 *      - RequestResult.Success<Unit>.
 * - In case of error:
 *      - RequestResult.Error<ValidationError.PasswordError>
 *      - RequestResult.Error<ValidationError.EmailError>
 *      - RequestResult.Error<ValidationError.PhoneError>
 *      - RequestResult.Error<ValidationError.UserInfoError>
 */
class SignUpDataValidator @Inject constructor(
    private val passwordValidator: PasswordValidator,
    private val emailValidator: EmailValidator,
    private val phoneValidator: PhoneValidator
) {

    fun validate(data: SignUpData): RequestResult<Unit, RootError> {
        val emailValidationResult = emailValidator.validate(data.email)
        if (emailValidationResult is RequestResult.Error) {
            return emailValidationResult
        }
        val passwordValidationResult = passwordValidator.validate(data.password)
        if (passwordValidationResult is RequestResult.Error) {
            return passwordValidationResult
        }
        val phoneValidationResult = phoneValidator.validate(data.phone)
        if (phoneValidationResult is RequestResult.Error) {
            return phoneValidationResult
        }
        if (data.firstName.isBlank()) {
            return RequestResult.Error(ValidationError.UserInfoError.INCORRECT_FIRST_NAME)
        }
        if (data.middleName.isBlank()) {
            return RequestResult.Error(ValidationError.UserInfoError.INCORRECT_MIDDLE_NAME)
        }
        if (data.lastName.isBlank()) {
            return RequestResult.Error(ValidationError.UserInfoError.INCORRECT_LAST_NAME)
        }
        return RequestResult.Success(Unit)
    }
}