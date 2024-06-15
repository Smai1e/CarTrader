package dev.smai1e.carTrader.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Entity for sign-up responding from API.
 */
@Serializable
data class SignUpResponse(

    @SerialName("id")
    val id: Long,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("middle_name")
    val middleName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("email")
    val email: String,

    @SerialName("phone")
    val phone: String
)