package dev.smai1e.carTrader.data.network

import dev.smai1e.carTrader.domain.repositoryInterfaces.TokenRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(val repository: TokenRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        repository.fetchAuthToken().let { token ->
            requestBuilder.addHeader(API_KEY_HEADER, "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }

    private companion object {
        private const val API_KEY_HEADER = "Authorization"
    }
}