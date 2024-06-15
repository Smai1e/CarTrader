package dev.smai1e.carTrader.ui.models

/**
 * Entity for operations in the ui layer.
 */
data class AuctionUI(
    var id: Long,
    var sellerId: Long,
    var openDate: String,
    var closeDate: String,
    var minBid: Int,
    var auctionStatus: StatusUI,
    var car: CarUI
)