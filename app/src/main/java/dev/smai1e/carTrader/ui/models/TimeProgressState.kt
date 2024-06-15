package dev.smai1e.carTrader.ui.models

import java.time.Duration

/**
 * Entity for storing information of the trading date.
 */
data class TimeProgressState(
    val auctionDuration: Duration,
    val timeLeft: Duration,
    var isActive: Boolean
)