package dev.smai1e.carTrader.ui.views.ownAuctionDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.UserWithBidItemBinding
import dev.smai1e.carTrader.domain.models.BidWithUser
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.loadFromUrl
import dev.smai1e.carTrader.utils.toCurrencyString

class BidsViewHolder(
    private val binding: UserWithBidItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(bid: BidWithUser) {
        val resources = itemView.resources
        binding.apply {
            userImage.loadFromUrl(bid.bidder.photoUrl)
            userName.text = resources.getString(R.string.username_template, bid.bidder.lastName, bid.bidder.firstName)
            bidTime.text = bid.bidTime.convertISO8601ToFormattedDate()
            bidAmount.text = bid.bidAmount.toCurrencyString()
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): BidsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = UserWithBidItemBinding.inflate(layoutInflater, parent, false)
            return BidsViewHolder(binding)
        }
    }
}