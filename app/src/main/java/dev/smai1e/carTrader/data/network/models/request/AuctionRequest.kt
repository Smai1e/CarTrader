package dev.smai1e.carTrader.data.network.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Auction entity for requesting in the API.
 */
@Serializable
data class AuctionRequest(

    @SerialName("open_date")
    var openDate: String? = null,

    @SerialName("close_date")
    var closeDate: String? = null,

    @SerialName("min_bid")
    var minBid: Int? = null,

    @SerialName("car")
    var car: CarRequest = CarRequest()
)