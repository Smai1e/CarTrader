package dev.smai1e.carTrader.di

import dev.smai1e.carTrader.data.network.api.BidsSocketService
import dev.smai1e.carTrader.data.network.api.BidsSocketServiceImpl
import dev.smai1e.carTrader.domain.repositoryInterfaces.AuctionsRepository
import dev.smai1e.carTrader.data.repository.AuctionsRepositoryImpl
import dev.smai1e.carTrader.domain.repositoryInterfaces.BidsRepository
import dev.smai1e.carTrader.data.repository.BidsRepositoryImpl
import dev.smai1e.carTrader.domain.repositoryInterfaces.CarsRepository
import dev.smai1e.carTrader.data.repository.CarsRepositoryImpl
import dev.smai1e.carTrader.data.repository.PaymentRepositoryImpl
import dev.smai1e.carTrader.data.repository.TokenRepositoryImpl
import dev.smai1e.carTrader.domain.repositoryInterfaces.UserRepository
import dev.smai1e.carTrader.data.repository.UserRepositoryImpl
import dev.smai1e.carTrader.domain.repositoryInterfaces.PaymentRepository
import dev.smai1e.carTrader.domain.repositoryInterfaces.TokenRepository
import dagger.Binds
import dagger.Module

@Module
interface AppBindModule {

    @Suppress("FunctionName")
    @Binds
    fun bindAuctionsRepositoryImpl_to_AuctionsRepository(
        auctionsRepositoryImpl: AuctionsRepositoryImpl
    ): AuctionsRepository

    @Suppress("FunctionName")
    @Binds
    fun bindCarsRepositoryImpl_to_CarsRepository(
        carsRepositoryImpl: CarsRepositoryImpl
    ): CarsRepository

    @Suppress("FunctionName")
    @Binds
    fun bindBidsRepositoryImpl_to_BidsRepository(
        bidsRepositoryImpl: BidsRepositoryImpl
    ): BidsRepository

    @Suppress("FunctionName")
    @Binds
    fun bindUserRepositoryImpl_to_UserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Suppress("FunctionName")
    @Binds
    fun bindPaymentRepositoryImpl_to_PaymentRepository(
        paymentRepositoryImpl: PaymentRepositoryImpl
    ): PaymentRepository

    @Suppress("FunctionName")
    @Binds
    fun bindTokenRepositoryImpl_to_TokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Suppress("FunctionName")
    @Binds
    fun bindBidsSocketServiceImpl_to_BidsSocketService(
        bidsSocketServiceImpl: BidsSocketServiceImpl
    ): BidsSocketService
}