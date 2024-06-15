package dev.smai1e.carTrader.ui.errorMessageHandler.mappers

import androidx.annotation.StringRes
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.ui.errorMessageHandler.ErrorUiMessage

fun DataError.toErrorUiMessage(): ErrorUiMessage {
    return when (this) {
        is DataError.NetworkError -> this.toErrorUiMessage()
        is DataError.LocalStorageError -> this.toErrorUiMessage()
        is DataError.NetworkException -> ErrorUiMessage.Message(this.e.message ?: "unknown error")
        is DataError.LocalStorageException -> ErrorUiMessage.Message(this.e.message ?: "unknown error")
    }
}

/**
 * Convert NetworkError type into ErrorUiMessage with error text.
 */
fun DataError.NetworkError.toErrorUiMessage(): ErrorUiMessage {
    @StringRes val stringId = when (this) {
        DataError.NetworkError.UNKNOWN -> R.string.unknown_error
        DataError.NetworkError.CONFLICT -> R.string.conflict
        DataError.NetworkError.NOT_FOUND -> R.string.not_found
        DataError.NetworkError.BAD_REQUEST -> R.string.bad_request
        DataError.NetworkError.UNAUTHORIZED -> R.string.unauthorized_request
        DataError.NetworkError.REQUEST_TIMEOUT -> R.string.request_timeout
    }
    return ErrorUiMessage.StringResource(stringId)
}

/**
 * Convert LocalStorageError type into ErrorUiMessage with error text.
 */
fun DataError.LocalStorageError.toErrorUiMessage(): ErrorUiMessage {
    @StringRes val stringId = when (this) {
        DataError.LocalStorageError.NOT_BE_SAVED -> R.string.file_could_not_be_saved
    }
    return ErrorUiMessage.StringResource(stringId)
}