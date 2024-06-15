package dev.smai1e.carTrader.ui.errorMessageHandler

import android.content.Context
import androidx.annotation.StringRes

sealed interface ErrorUiMessage {

    class Message(
        val message: String
    ) : ErrorUiMessage

    class StringResource(
        @StringRes val id: Int
    ) : ErrorUiMessage

    fun getMessage(context: Context): String {
        return when (this) {
            is Message -> this.message
            is StringResource -> context.getString(this.id)
        }
    }
}