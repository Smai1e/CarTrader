package dev.smai1e.carTrader.data.repository

import dev.smai1e.carTrader.data.network.api.PaymentService
import dev.smai1e.carTrader.data.network.models.request.WalletRequest
import dev.smai1e.carTrader.di.IoDispatcher
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.repositoryInterfaces.PaymentRepository
import dev.smai1e.carTrader.data.toRequestResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Implementation a [PaymentRepository]
 * which uses [PaymentService] as data source.
 */
class PaymentRepositoryImpl @Inject constructor(
    private val paymentService: PaymentService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): PaymentRepository {

    /**
     * Withdraws funds from account.
     */
    override suspend fun withdrawAccount(wallet: WalletRequest): RequestResult<Boolean, DataError> {
        return paymentService.withdraw(wallet).toRequestResult()
    }

    /**
     * Replenishes account.
     */
    override suspend fun replenishAccount(wallet: WalletRequest): RequestResult<Boolean, DataError> {
        return paymentService.replenish(wallet).toRequestResult()
    }
}