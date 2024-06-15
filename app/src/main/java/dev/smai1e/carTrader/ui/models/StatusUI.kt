package dev.smai1e.carTrader.ui.models

import androidx.annotation.StringRes
import dev.smai1e.carTrader.R

/**
 * Entity for operations in the ui layer.
 */
enum class StatusUI(
    @StringRes val nameId: Int
) {
    WAITING(R.string.waiting_state),
    ACTIVE(R.string.active_state),
    COMPLETED(R.string.completed_state)
}