package dev.smai1e.carTrader.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.smai1e.carTrader.R

/**
 * Entity for operations in the ui layer.
 */
enum class ColorUI(
    @StringRes val nameId: Int,
    @DrawableRes val iconId: Int
) {
    RED(R.string.color_red, R.drawable.color_red),
    BLUE(R.string.color_blue, R.drawable.color_blue),
    GREEN(R.string.color_green, R.drawable.color_green),
    BLACK(R.string.color_black, R.drawable.color_black),
    WHITE(R.string.color_white, R.drawable.color_white)
}