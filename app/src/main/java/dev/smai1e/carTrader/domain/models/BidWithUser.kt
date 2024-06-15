package dev.smai1e.carTrader.domain.models

/**
 * Entity for operations in the domain layer.
 */
data class BidWithUser(
    val id: Long,
    val auctionId: Long,
    val bidder: UserInfo,
    val bidTime: String,
    val bidAmount: Int
)
