package dev.smai1e.carTrader.data.network

sealed interface NetworkResult<out T : Any> {

    data object ApiPending : NetworkResult<Nothing>

    class ApiSuccess<out T : Any>(
        val data: T
    ) : NetworkResult<T>

    class ApiError<out T : Any>(
        val code: Int,
        val message: String?
    ) : NetworkResult<T>

    class ApiException<out T : Any>(
        val e: Throwable
    ) : NetworkResult<T>
}

/**
 * Convert NetworkResult<T> into NetworkResult<R>.
 */
fun <T: Any, R: Any> NetworkResult<T>.map(
    mapper: ((T) -> R)? = null
): NetworkResult<R> {

    return when (this) {
        is NetworkResult.ApiPending -> NetworkResult.ApiPending
        is NetworkResult.ApiSuccess<T> -> {
            if (mapper == null) {
                throw IllegalStateException("Can't map NetworkResult.ApiSuccess<T> result without mapper.")
            } else {
                NetworkResult.ApiSuccess(mapper(this.data))
            }
        }
        is NetworkResult.ApiError<T> -> NetworkResult.ApiError(this.code, this.message)
        is NetworkResult.ApiException<T> -> NetworkResult.ApiException(this.e)
    }
}