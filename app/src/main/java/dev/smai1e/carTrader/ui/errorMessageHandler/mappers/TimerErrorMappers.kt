package dev.smai1e.carTrader.ui.errorMessageHandler.mappers

import dev.smai1e.carTrader.domain.errorTypes.TimerException
import dev.smai1e.carTrader.ui.errorMessageHandler.ErrorUiMessage

/**
 * Convert TimerException into ErrorUiMessage with error text.
 */
fun TimerException.toErrorUiMessage(): ErrorUiMessage {
    return ErrorUiMessage.Message(this.e.message ?: "unknown timer exception")
}