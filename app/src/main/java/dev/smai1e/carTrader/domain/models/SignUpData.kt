package dev.smai1e.carTrader.domain.models

data class SignUpData(
    val email: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val phone: String,
    val password: String
)