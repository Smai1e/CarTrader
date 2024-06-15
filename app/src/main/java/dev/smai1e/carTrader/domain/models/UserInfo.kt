package dev.smai1e.carTrader.domain.models

/**
 * Entity for operations in the domain layer.
 */
data class UserInfo(
    val id: Long,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val photoUrl: String,
    val email: String,
    val phone: String,
    val money: Int = 0,
    val bidsCount: Int,
    val auctionsCount: Int,
    val registrationDate: String
)