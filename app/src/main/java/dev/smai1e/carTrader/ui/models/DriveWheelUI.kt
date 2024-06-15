package dev.smai1e.carTrader.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.smai1e.carTrader.R

/**
 * Entity for operations in the ui layer.
 */
enum class DriveWheelUI(
    @StringRes val nameId: Int,
    @DrawableRes val iconId: Int
) {
    FWD(R.string.driveWheel_fwd, R.drawable.ic_drive_wheel_fwd),
    FOURWD(R.string.driveWheel_fourwd, R.drawable.ic_drive_wheel_fourwd),
    AWD(R.string.driveWheel_awd, R.drawable.ic_drive_wheel_awd),
    RWD(R.string.driveWheel_rwd, R.drawable.ic_drive_wheel_rwd)
}