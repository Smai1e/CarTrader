package dev.smai1e.carTrader.ui.views.auctionsList.auctionsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.AuctionItemBigBinding
import dev.smai1e.carTrader.ui.models.AuctionUI
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.loadFromUrlWithAnimation
import dev.smai1e.carTrader.utils.toCurrencyString

class AuctionsViewHolder(
    private val binding: AuctionItemBigBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(auction: AuctionUI, onClick: (Long) -> Unit) {
        val resources = itemView.resources
        binding.apply {
            modelTextView.text = resources.getString(
                R.string.car_name_template,
                auction.car.brand,
                auction.car.model
            )
            costTextView.text = resources.getString(R.string.min_bid_template, auction.minBid.toCurrencyString())
            mileageTextView.text = resources.getString(R.string.mileage_template, auction.car.mileage)
            gearboxTextView.text = resources.getString(auction.car.gearbox.nameId)
            driveWheelTextView.text = resources.getString(auction.car.driveWheel.nameId)
            horsepowerTextView.text = resources.getString(R.string.horsepower_template, auction.car.horsepower)
            openDateTextView.text = auction.openDate.convertISO8601ToFormattedDate()
            closeDateTextView.text = auction.closeDate.convertISO8601ToFormattedDate()
            imageView.loadFromUrlWithAnimation(auction.car.imageUrlList.first())
        }
        itemView.setOnClickListener {
            onClick(auction.id)
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): AuctionsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AuctionItemBigBinding.inflate(layoutInflater, parent, false)
            return AuctionsViewHolder(binding)
        }
    }
}