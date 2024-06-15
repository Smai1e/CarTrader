package dev.smai1e.carTrader.ui.models

/**
 * Entity for operations in the ui layer.
 */
data class CarUI(
    val id: Long,
    val imageUrlList: List<String>,
    val brand: String,
    val model: String,
    val manufacturerDate: Int,
    val color: ColorUI,
    val mileage: Int,
    val horsepower: Int,
    val driveWheel: DriveWheelUI,
    val gearbox: GearboxUI,
    val description: String,
    val vin: String
)