package dev.smai1e.carTrader.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Auction entity for responding from API.
 */
@Serializable
data class AuctionResponse(

    @SerialName("id")
    val id: Long,

    @SerialName("seller_id")
    val sellerId: Long,

    @SerialName("open_date")
    val openDate: String,

    @SerialName("close_date")
    val closeDate: String,

    @SerialName("min_bid")
    val minBid: Int,

    @SerialName("auction_status")
    val auctionStatus: String,

    @SerialName("car")
    val car: CarResponse
)