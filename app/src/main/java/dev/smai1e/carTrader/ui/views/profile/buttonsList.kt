package dev.smai1e.carTrader.ui.views.profile

import androidx.navigation.navOptions
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.ui.models.NavButton
import dev.smai1e.carTrader.ui.models.NavParams

fun getProfileButtonList() = listOf(
    NavButton(
        title = R.string.replenish_account,
        navParams = NavParams(destinationId = R.id.action_profileFragment_to_replenishmentBottomDialog)
    ),
    NavButton(
        title = R.string.withdrawal_money,
        navParams = NavParams(destinationId = R.id.action_profileFragment_to_withdrawalBottomDialog)
    ),
    NavButton(
        title = R.string.clear_cache,
        navParams = NavParams(destinationId = R.id.action_profileFragment_to_cacheBottomDialog)
    ),
    NavButton(
        title = R.string.logout,
        navParams = NavParams(
            destinationId = R.id.signInFragment,
            navOptions = navOptions {
                popUpTo(R.id.tabsFragment) {
                    inclusive = true
                }
            })
    )
)