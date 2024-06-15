package dev.smai1e.carTrader.ui.models

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.navigation.NavOptions


/**
 * Button with uses in the nav button lists.
 */
data class NavButton(
    @StringRes val title: Int,
    val navParams: NavParams
)

data class NavParams(
    @IdRes val destinationId: Int,
    val args: Bundle? = null,
    val navOptions: NavOptions? = null
)