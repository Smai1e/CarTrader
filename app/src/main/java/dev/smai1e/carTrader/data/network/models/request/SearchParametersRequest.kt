package dev.smai1e.carTrader.data.network.models.request

import dev.smai1e.carTrader.domain.models.Color
import dev.smai1e.carTrader.domain.models.DriveWheel
import dev.smai1e.carTrader.domain.models.Gearbox
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Params entity for requesting in the API.
 */
@Serializable
data class SearchParametersRequest(

    @SerialName("search_request")
    val searchRequest: String? = null,

    @SerialName("open_date")
    val openDate: String? = null,

    @SerialName("close_date")
    val closeDate: String? = null,

    @SerialName("min_bid_start")
    val minBidStart: Int? = null,

    @SerialName("min_bid_end")
    val minBidEnd: Int? = null,

    @SerialName("brand")
    val brand: String? = null,

    @SerialName("model")
    val model: String? = null,

    @SerialName("manufacturer_date_start")
    val manufacturerDateStart: Int? = null,

    @SerialName("manufacturer_date_end")
    val manufacturerDateEnd: Int? = null,

    @SerialName("color")
    val color: Color? = null,

    @SerialName("horsepower_start")
    val horsepowerStart: Int? = null,

    @SerialName("horsepower_end")
    val horsepowerEnd: Int? = null,

    @SerialName("mileage_start")
    val mileageStart: Int? = null,

    @SerialName("mileage_end")
    val mileageEnd: Int? = null,

    @SerialName("drive_wheel")
    val driveWheel: DriveWheel? = null,

    @SerialName("gearbox")
    val gearbox: Gearbox? = null
)