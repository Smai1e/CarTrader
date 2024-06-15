package dev.smai1e.carTrader.ui.models

/**
 * Entity for operations in the ui layer.
 */
data class BidUI(
    val id: Long,
    val auctionId: Long,
    val bidderId: Long,
    val bidTime: String,
    val bidAmount: Int
)