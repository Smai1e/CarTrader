package dev.smai1e.carTrader.data.network

import retrofit2.HttpException
import retrofit2.Response

fun <T : Any> handleApi(
    execute: () -> Response<T>
): NetworkResult<T> {

    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.ApiSuccess(body)
        } else {
            NetworkResult.ApiError(
                code = response.code(),
                message = response.message()
            )
        }
    } catch (e: HttpException) {
        NetworkResult.ApiError(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkResult.ApiException(e)
    }
}