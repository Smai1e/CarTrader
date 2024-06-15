package dev.smai1e.carTrader.ui

import android.content.Context
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.errorTypes.RootError
import dev.smai1e.carTrader.ui.errorMessageHandler.ErrorUiMessage
import dev.smai1e.carTrader.ui.errorMessageHandler.mappers.toErrorUiMessage

sealed interface State<out T : Any> {

    data object Idle : State<Nothing>

    data object Loading : State<Nothing>

    class Success<out T : Any>(
        val data: T
    ) : State<T>

    class Error<out T : Any>(
        private val error: ErrorUiMessage
    ) : State<T> {

        fun getErrorText(context: Context): String {
            return error.getMessage(context)
        }
    }
}

/**
 * Convert RequestResult<D, ED> into State<D>.
 */
fun <D : Any, ED : RootError> RequestResult<D, ED>.toState(): State<D> {
    return when (this) {
        is RequestResult.Loading -> State.Loading
        is RequestResult.Success -> State.Success(this.data)
        is RequestResult.Error -> State.Error(this.error.toErrorUiMessage())
    }
}