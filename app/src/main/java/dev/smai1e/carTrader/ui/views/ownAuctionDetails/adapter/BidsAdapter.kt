package dev.smai1e.carTrader.ui.views.ownAuctionDetails.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.smai1e.carTrader.domain.models.BidWithUser

class BidsAdapter : ListAdapter<BidWithUser, BidsViewHolder>(BIDS_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BidsViewHolder =
        BidsViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: BidsViewHolder, position: Int) {
        val bidItem = getItem(position)
        holder.bind(bidItem)
    }

    companion object {
        private val BIDS_DIFF_CALLBACK = object : DiffUtil.ItemCallback<BidWithUser>() {
            override fun areItemsTheSame(oldItem: BidWithUser, newItem: BidWithUser) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BidWithUser, newItem: BidWithUser) =
                oldItem == newItem
        }
    }
}