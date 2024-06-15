package dev.smai1e.carTrader.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SpinnerItem<T>(
    val type: T,
    @StringRes val nameId: Int,
    @DrawableRes val iconId: Int?
)