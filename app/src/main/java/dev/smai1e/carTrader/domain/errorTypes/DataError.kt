package dev.smai1e.carTrader.domain.errorTypes

/**
 * Errors that occur when receiving and sending data.
 */
sealed interface DataError : RootError {

    enum class NetworkError : DataError {
        NOT_FOUND,
        UNKNOWN,
        BAD_REQUEST,
        CONFLICT,
        UNAUTHORIZED,
        REQUEST_TIMEOUT
    }

    enum class LocalStorageError : DataError {
        NOT_BE_SAVED
    }

    data class NetworkException(val e: Throwable) : DataError

    data class LocalStorageException(val e: Throwable) : DataError
}