package dev.smai1e.carTrader.data.network.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Bid entity for requesting in the API.
 */
@Serializable
data class BidRequest(

    @SerialName("auction_id")
    val auctionId: Long,

    @SerialName("bid_amount")
    val bidAmount: Int
)
