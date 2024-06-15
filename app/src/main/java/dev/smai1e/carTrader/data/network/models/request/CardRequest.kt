package dev.smai1e.carTrader.data.network.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Card entity for requesting in the API.
 */
@Serializable
data class CardRequest(

    @SerialName("number")
    val number: String? = null,

    @SerialName("validity_period")
    val validityPeriod: String? = null,

    @SerialName("cvc")
    val cvv: Int? = null
)