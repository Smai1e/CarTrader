package dev.smai1e.carTrader.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.smai1e.carTrader.R

/**
 * Entity for operations in the ui layer.
 */
enum class GearboxUI(
    @StringRes val nameId: Int,
    @DrawableRes val iconId: Int
) {
    MANUAL(R.string.gearbox_manual, R.drawable.ic_gearbox_manual),
    AUTOMATIC(R.string.gearbox_automatic, R.drawable.ic_gearbox_automatic),
    CVT(R.string.gearbox_cvt, R.drawable.ic_gearbox_cvt)
}