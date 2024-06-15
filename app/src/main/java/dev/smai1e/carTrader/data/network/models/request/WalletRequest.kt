package dev.smai1e.carTrader.data.network.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Wallet entity for requesting in the API.
 */
@Serializable
data class WalletRequest(

    @SerialName("money")
    val money: Int,

    @SerialName("card")
    val card: CardRequest
)
