package dev.smai1e.carTrader.ui.views.auctionsList.auctionsAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.smai1e.carTrader.ui.models.AuctionUI

class AuctionsAdapter(
    private val onClick: (Long) -> Unit
) : ListAdapter<AuctionUI, AuctionsViewHolder>(AUCTION_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuctionsViewHolder =
        AuctionsViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: AuctionsViewHolder, position: Int) {
        val auctionItem = getItem(position)
        holder.bind(auctionItem, onClick)
    }

    companion object {
        private val AUCTION_DIFF_CALLBACK = object : DiffUtil.ItemCallback<AuctionUI>() {
            override fun areItemsTheSame(oldItem: AuctionUI, newItem: AuctionUI) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AuctionUI, newItem: AuctionUI) =
                oldItem == newItem
        }
    }
}