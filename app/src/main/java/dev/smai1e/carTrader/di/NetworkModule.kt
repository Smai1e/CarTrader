package dev.smai1e.carTrader.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.smai1e.carTrader.data.network.AuthInterceptor
import dev.smai1e.carTrader.data.network.NetworkResultCallAdapterFactory
import dev.smai1e.carTrader.data.network.api.AuctionsService
import dev.smai1e.carTrader.data.network.api.BidsService
import dev.smai1e.carTrader.data.network.api.CarsService
import dev.smai1e.carTrader.data.network.api.PaymentService
import dev.smai1e.carTrader.data.network.api.UserService
import dev.smai1e.carTrader.domain.repositoryInterfaces.TokenRepository
import dagger.Module
import dagger.Provides
import dev.smai1e.carTrader.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.request.header
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(repository: TokenRepository): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            defaultRequest {
                header(API_KEY_HEADER, "Bearer ${repository.fetchAuthToken()}")
            }
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuctionsService(retrofit: Retrofit): AuctionsService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideCarsService(retrofit: Retrofit): CarsService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideBidsService(retrofit: Retrofit): BidsService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create()
    }

    @Provides
    @Singleton
    fun providePaymentService(retrofit: Retrofit): PaymentService {
        return retrofit.create()
    }

    private companion object {
        const val API_KEY_HEADER = "Authorization"
    }
}