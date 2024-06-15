package dev.smai1e.carTrader.domain.models

/**
 * Entity for operations in the domain layer.
 */
data class Car(
    val id: Long,
    val imageUrlList: List<String>,
    val brand: String,
    val model: String,
    val manufacturerDate: Int,
    val color: Color,
    val mileage: Int,
    val horsepower: Int,
    val driveWheel: DriveWheel,
    val gearbox: Gearbox,
    val description: String,
    val vin: String
)