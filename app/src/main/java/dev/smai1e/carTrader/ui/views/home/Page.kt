package dev.smai1e.carTrader.ui.views.home

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.ui.views.ownAuctions.OwnAuctionsFragment
import dev.smai1e.carTrader.ui.views.participateAuctions.AuctionsParticipantFragment

data class Page(
    val fragment: Fragment,

    @StringRes
    val titleId: Int
)

val pagesList = listOf(
        Page(
            OwnAuctionsFragment(),
            R.string.own_auctions_page_title
        ),
        Page(
            AuctionsParticipantFragment(),
            R.string.participate_auctions_page_title
        )
    )