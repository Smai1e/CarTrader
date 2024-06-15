package dev.smai1e.carTrader.data.network.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Entity for sign-up requesting in the API.
 */
@Serializable
data class SignUpRequest(

    @SerialName("email")
    val email: String,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("middle_name")
    val middleName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("phone")
    val phone: String,

    @SerialName("password")
    val password: String
)