package dev.smai1e.carTrader.data.network.models.response

import dev.smai1e.carTrader.data.network.api.ImageUrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Car entity for responding from API.
 */
@Serializable
data class CarResponse(

    @SerialName("id")
    val id: Long,

    @SerialName("image_url_list")
    val imageUrlList: List<ImageUrl>,

    @SerialName("brand")
    val brand: String,

    @SerialName("model")
    val model: String,

    @SerialName("manufacturer_date")
    val manufacturerDate: Int,

    @SerialName("color")
    val color: String,

    @SerialName("mileage")
    val mileage: Int,

    @SerialName("horsepower")
    val horsepower: Int,

    @SerialName("drive_wheel")
    val driveWheel: String,

    @SerialName("gearbox")
    val gearbox: String,

    @SerialName("description")
    val description: String,

    @SerialName("vin")
    val vin: String
)