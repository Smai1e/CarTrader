package dev.smai1e.carTrader.ui.views.participateAuctions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.AuctionItemMediumBinding
import dev.smai1e.carTrader.ui.models.AuctionUI
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.loadFromUrlWithAnimation
import dev.smai1e.carTrader.utils.toCurrencyString

class AuctionViewHolder(
    private val binding: AuctionItemMediumBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(auction: AuctionUI, onClick: (Long) -> Unit) {
        val resources = itemView.resources
        binding.apply {
            imageView.loadFromUrlWithAnimation(auction.car.imageUrlList.first())
            brandTextView.text = resources.getString(R.string.car_name_template, auction.car.brand, auction.car.model)
            costTextView.text = auction.minBid.toCurrencyString()
            statusTextView.text = resources.getString(R.string.state, resources.getString(auction.auctionStatus.nameId))
            openDateTextView.text = auction.openDate.convertISO8601ToFormattedDate()
            closeDateTextView.text = auction.closeDate.convertISO8601ToFormattedDate()
        }
        itemView.setOnClickListener {
            onClick(auction.id)
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): AuctionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AuctionItemMediumBinding.inflate(layoutInflater, parent, false)
            return AuctionViewHolder(binding)
        }
    }
}