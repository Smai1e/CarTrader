package dev.smai1e.carTrader.ui.views.auctionsList.brandsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.domain.models.Brand
import dev.smai1e.carTrader.databinding.BrandItemBigBinding
import dev.smai1e.carTrader.utils.loadFromUrlWithAnimation

class PopularBrandsViewHolder(
    private val binding: BrandItemBigBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Brand, onClick: (String) -> Unit) {
        binding.apply {
            title.text = item.name
            brandImageView.loadFromUrlWithAnimation(item.logoUrl)
        }
        itemView.setOnClickListener {
            onClick(item.name)
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): PopularBrandsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = BrandItemBigBinding.inflate(layoutInflater, parent, false)
            return PopularBrandsViewHolder(binding)
        }
    }
}