package dev.smai1e.carTrader.data.network.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Entity for sign-in requesting in the API.
 */
@Serializable
data class SignInRequest(

    @SerialName("email")
    val email: String,

    @SerialName("password")
    val password: String
)
