package dev.smai1e.carTrader.ui.errorMessageHandler.mappers

import androidx.annotation.StringRes
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.domain.errorTypes.ValidationError
import dev.smai1e.carTrader.ui.errorMessageHandler.ErrorUiMessage

fun ValidationError.toErrorUiMessage(): ErrorUiMessage {
    return when (this) {
        is ValidationError.EmailError -> this.toErrorUiMessage()
        is ValidationError.PasswordError -> this.toErrorUiMessage()
        is ValidationError.AuctionDataError -> this.toErrorUiMessage()
        is ValidationError.PhoneError -> this.toErrorUiMessage()
        is ValidationError.UserInfoError -> this.toErrorUiMessage()
    }
}

/**
 * Convert EmailError type into ErrorUiMessage with error text.
 */
fun ValidationError.EmailError.toErrorUiMessage(): ErrorUiMessage {
    @StringRes val stringId = when (this) {
        ValidationError.EmailError.INCORRECT_EMAIL -> R.string.incorrect_email
    }
    return ErrorUiMessage.StringResource(stringId)
}

/**
 * Convert PasswordError type into ErrorUiMessage with error text.
 */
fun ValidationError.PasswordError.toErrorUiMessage(): ErrorUiMessage {
    @StringRes val stringId = when (this) {
        ValidationError.PasswordError.NO_DIGIT -> R.string.no_digits
        ValidationError.PasswordError.NO_UPPERCASE -> R.string.no_uppercase
        ValidationError.PasswordError.TOO_SHORT -> R.string.too_short
    }
    return ErrorUiMessage.StringResource(stringId)
}

/**
 * Convert AuctionDataError type into ErrorUiMessage with error text.
 */
fun ValidationError.AuctionDataError.toErrorUiMessage(): ErrorUiMessage {
    @StringRes val stringId = when (this) {
        ValidationError.AuctionDataError.INCORRECT_OPEN_DATE -> R.string.incorrect_open_date
        ValidationError.AuctionDataError.INCORRECT_CLOSE_DATE -> R.string.incorrect_close_date
        ValidationError.AuctionDataError.EMPTY_BRAND -> R.string.empty_brand
        ValidationError.AuctionDataError.EMPTY_MODEL -> R.string.empty_model
        ValidationError.AuctionDataError.INCORRECT_VIN -> R.string.incorrect_vin
        ValidationError.AuctionDataError.EMPTY_DESCRIPTION -> R.string.empty_description_error
        ValidationError.AuctionDataError.EMPTY_MIN_BID -> R.string.empty_min_bid
        ValidationError.AuctionDataError.EMPTY_COLOR -> R.string.empty_color
        ValidationError.AuctionDataError.EMPTY_MILEAGE -> R.string.empty_mileage
        ValidationError.AuctionDataError.EMPTY_GEARBOX -> R.string.empty_gearbox
        ValidationError.AuctionDataError.EMPTY_DRIVE_WHEEL -> R.string.empty_drive_wheel
        ValidationError.AuctionDataError.EMPTY_HORSEPOWER -> R.string.empty_horsepower
        ValidationError.AuctionDataError.INCORRECT_MANUFACTURER_DATE -> R.string.incorrect_manufacturer_year
    }
    return ErrorUiMessage.StringResource(stringId)
}

/**
 * Convert PhoneError type into ErrorUiMessage with error text.
 */
fun ValidationError.PhoneError.toErrorUiMessage(): ErrorUiMessage {
    @StringRes val stringId = when (this) {
        ValidationError.PhoneError.INCORRECT_PHONE -> R.string.incorrect_phone
    }
    return ErrorUiMessage.StringResource(stringId)
}

/**
 * Convert UserInfoError type into ErrorUiMessage with error text.
 */
fun ValidationError.UserInfoError.toErrorUiMessage(): ErrorUiMessage {
    @StringRes val stringId = when (this) {
        ValidationError.UserInfoError.INCORRECT_FIRST_NAME -> R.string.empty_field_error
        ValidationError.UserInfoError.INCORRECT_MIDDLE_NAME -> R.string.empty_field_error
        ValidationError.UserInfoError.INCORRECT_LAST_NAME -> R.string.empty_field_error
    }
    return ErrorUiMessage.StringResource(stringId)
}