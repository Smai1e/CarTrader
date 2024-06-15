package dev.smai1e.carTrader.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Bid entity for responding from API.
 */
@Serializable
data class BidResponse(

    @SerialName("id")
    val id: Long,

    @SerialName("auction_id")
    val auctionId: Long,

    @SerialName("bidder_id")
    val bidderId: Long,

    @SerialName("bid_time")
    val bidTime: String,

    @SerialName("bid_amount")
    val bidAmount: Int
)