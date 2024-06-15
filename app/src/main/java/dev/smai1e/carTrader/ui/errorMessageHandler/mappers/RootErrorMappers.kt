package dev.smai1e.carTrader.ui.errorMessageHandler.mappers

import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.domain.errorTypes.RootError
import dev.smai1e.carTrader.domain.errorTypes.TimerException
import dev.smai1e.carTrader.domain.errorTypes.ValidationError
import dev.smai1e.carTrader.ui.errorMessageHandler.ErrorUiMessage

fun RootError.toErrorUiMessage(): ErrorUiMessage {
    return when (this) {
        is DataError -> this.toErrorUiMessage()
        is TimerException -> this.toErrorUiMessage()
        is ValidationError -> this.toErrorUiMessage()
    }
}