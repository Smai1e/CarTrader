package dev.smai1e.carTrader.data

import dev.smai1e.carTrader.data.network.NetworkResult
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.domain.errorTypes.RootError

sealed interface RequestResult<out D: Any, out E : RootError> {

    data object Loading : RequestResult<Nothing, Nothing>

    class Success<out D: Any, out E : RootError>(
        val data: D
    ) : RequestResult<D, E>

    class Error<out D: Any, out E : RootError>(
        val error: E
    ) : RequestResult<D, E>
}

/**
 * Convert NetworkResult<D> into RequestResult<D, Error>.
 */
fun <D : Any> NetworkResult<D>.toRequestResult(): RequestResult<D, DataError> {
    return when (this) {
        is NetworkResult.ApiPending -> RequestResult.Loading
        is NetworkResult.ApiSuccess -> RequestResult.Success(this.data)
        is NetworkResult.ApiError -> RequestResult.Error(this.code.toNetworkError())
        is NetworkResult.ApiException -> RequestResult.Error(DataError.NetworkException(this.e))
    }
}

/**
 * Convert RequestResult<D, E> into RequestResult<R, E>.
 */
fun <D : Any, E : RootError, R : Any> RequestResult<D, E>.map(
    mapper: ((D) -> R)? = null
): RequestResult<R, E> {

    return when (this) {
        is RequestResult.Loading -> RequestResult.Loading
        is RequestResult.Success -> {
            if (mapper == null) {
                throw IllegalStateException("Can't map RequestResult.Success<D, E> result without mapper.")
            } else {
                RequestResult.Success(mapper(this.data))
            }
        }
        is RequestResult.Error -> RequestResult.Error(this.error)
    }
}