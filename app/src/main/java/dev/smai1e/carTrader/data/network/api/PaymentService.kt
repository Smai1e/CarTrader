package dev.smai1e.carTrader.data.network.api

import dev.smai1e.carTrader.data.network.NetworkResult
import dev.smai1e.carTrader.data.network.models.request.WalletRequest
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API for funds transactions.
 */
interface PaymentService {

    /**
     * Withdraws funds from account.
     */
    @POST("user/wallet/withdraw")
    suspend fun withdraw(
        @Body wallet: WalletRequest
    ): NetworkResult<Boolean>

    /**
     * Replenishes account.
     */
    @POST("user/wallet/replenish")
    suspend fun replenish(
        @Body wallet: WalletRequest
    ): NetworkResult<Boolean>
}