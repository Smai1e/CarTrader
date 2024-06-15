package dev.smai1e.carTrader.domain.errorTypes

/**
 * An exception that occurs when the timer fails.
 */
data class TimerException(val e: Throwable) : RootError