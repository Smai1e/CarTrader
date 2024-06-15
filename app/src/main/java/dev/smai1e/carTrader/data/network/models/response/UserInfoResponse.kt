package dev.smai1e.carTrader.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * User entity for responding from API.
 */
@Serializable
data class UserInfoResponse(

    @SerialName("id")
    val id: Long,

    @SerialName("first_name")
    val firstName: String,

    @SerialName("middle_name")
    val middleName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("photo_url")
    val photoUrl: String,

    @SerialName("email")
    val email: String,

    @SerialName("phone")
    val phone: String,

    @SerialName("money")
    val money: Int = 0,

    @SerialName("bids_count")
    val bidsCount: Int,

    @SerialName("auctions_count")
    val auctionsCount: Int,

    @SerialName("registration_date")
    val registrationDate: String
)