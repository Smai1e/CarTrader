package dev.smai1e.carTrader.domain.models

/**
 * Entity for operations in the domain layer.
 */
data class Auction(
    var id: Long,
    var sellerId: Long,
    var openDate: String,
    var closeDate: String,
    var minBid: Int,
    var auctionStatus: Status,
    var car: Car
)