package dev.smai1e.carTrader.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface CarPickItem

data class Brand(
    val name: String,
    val logoUrl: String
) : CarPickItem

data class Model(
    val name: String
) : CarPickItem

@Parcelize
data class CarName(
    val brand: String = "",
    val model: String = "",
) : Parcelable