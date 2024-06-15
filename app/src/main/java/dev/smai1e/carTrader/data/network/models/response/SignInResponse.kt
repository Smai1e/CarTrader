package dev.smai1e.carTrader.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Entity for sign-in responding from API.
 */
@Serializable
data class SignInResponse(
    @SerialName("token")
    var token: String
)