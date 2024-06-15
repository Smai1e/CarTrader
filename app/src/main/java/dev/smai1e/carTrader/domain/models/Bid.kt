package dev.smai1e.carTrader.domain.models

/**
 * Entity for operations in the domain layer.
 */
data class Bid(
    val id: Long,
    val auctionId: Long,
    val bidderId: Long,
    val bidTime: String,
    val bidAmount: Int
)