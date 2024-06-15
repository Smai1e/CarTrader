package dev.smai1e.carTrader.domain.repositoryInterfaces

import dev.smai1e.carTrader.data.network.models.request.WalletRequest
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult

/**
 * Data layer implementation for [WalletRequest]
 */
interface PaymentRepository {

    /**
     * Withdraws funds from account.
     */
    suspend fun withdrawAccount(
        wallet: WalletRequest
    ): RequestResult<Boolean, DataError>

    /**
     * Replenishes account.
     */
    suspend fun replenishAccount(
        wallet: WalletRequest
    ): RequestResult<Boolean, DataError>
}