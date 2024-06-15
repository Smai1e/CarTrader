package dev.smai1e.carTrader.ui.views.filterPage

import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.ui.models.ColorUI
import dev.smai1e.carTrader.ui.models.DriveWheelUI
import dev.smai1e.carTrader.ui.models.GearboxUI
import dev.smai1e.carTrader.ui.models.SpinnerItem

val gearboxList = listOf(
    SpinnerItem(null, R.string.empty, null),
    SpinnerItem(GearboxUI.AUTOMATIC, GearboxUI.AUTOMATIC.nameId, GearboxUI.AUTOMATIC.iconId),
    SpinnerItem(GearboxUI.MANUAL, GearboxUI.MANUAL.nameId, GearboxUI.MANUAL.iconId),
    SpinnerItem(GearboxUI.CVT, GearboxUI.CVT.nameId, GearboxUI.CVT.iconId)
)
val colorList = listOf(
    SpinnerItem(null, R.string.empty , null),
    SpinnerItem(ColorUI.RED, ColorUI.RED.nameId, ColorUI.RED.iconId),
    SpinnerItem(ColorUI.BLUE, ColorUI.BLUE.nameId, ColorUI.BLUE.iconId),
    SpinnerItem(ColorUI.GREEN, ColorUI.GREEN.nameId, ColorUI.GREEN.iconId),
    SpinnerItem(ColorUI.BLACK, ColorUI.BLACK.nameId, ColorUI.BLACK.iconId),
    SpinnerItem(ColorUI.WHITE, ColorUI.WHITE.nameId, ColorUI.WHITE.iconId)
)
val driveWheelList = listOf(
    SpinnerItem(null, R.string.empty, null),
    SpinnerItem(DriveWheelUI.FWD, DriveWheelUI.FWD.nameId, DriveWheelUI.FWD.iconId),
    SpinnerItem(DriveWheelUI.FOURWD, DriveWheelUI.FOURWD.nameId, DriveWheelUI.FOURWD.iconId),
    SpinnerItem(DriveWheelUI.AWD, DriveWheelUI.AWD.nameId, DriveWheelUI.AWD.iconId),
    SpinnerItem(DriveWheelUI.RWD, DriveWheelUI.RWD.nameId, DriveWheelUI.RWD.iconId)
)