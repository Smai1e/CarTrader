package dev.smai1e.carTrader.data.network.models.request

import dev.smai1e.carTrader.domain.models.Color
import dev.smai1e.carTrader.domain.models.DriveWheel
import dev.smai1e.carTrader.domain.models.Gearbox
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Car entity for requesting in the API.
 */
@Serializable
data class CarRequest(

    @SerialName("image_url_list")
    var imageUrlList: List<String> = emptyList(),

    @SerialName("brand")
    var brand: String? = null,

    @SerialName("model")
    var model: String? = null,

    @SerialName("manufacturer_date")
    var manufacturerDate: Int? = null,

    @SerialName("color")
    var color: Color? = null,

    @SerialName("mileage")
    var mileage: Int? = null,

    @SerialName("horsepower")
    var horsepower: Int? = null,

    @SerialName("drive_wheel")
    var driveWheel: DriveWheel? = null,

    @SerialName("gearbox")
    var gearbox: Gearbox? = null,

    @SerialName("description")
    var description: String? = null,

    @SerialName("vin")
    var vin: String? = null
)
