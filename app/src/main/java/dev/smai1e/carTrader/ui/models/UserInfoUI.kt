package dev.smai1e.carTrader.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Entity for operations in the ui layer.
 */
@Parcelize
data class UserInfoUI(
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
) : Parcelable