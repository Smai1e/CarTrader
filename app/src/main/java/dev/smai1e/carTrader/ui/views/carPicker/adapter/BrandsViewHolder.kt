package dev.smai1e.carTrader.ui.views.carPicker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.domain.models.Brand
import dev.smai1e.carTrader.databinding.BrandItemLittleBinding
import dev.smai1e.carTrader.utils.loadFromUrlWithAnimation

class BrandsViewHolder(
    private val binding: BrandItemLittleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Brand, onClick: (Brand) -> Unit) {
        binding.brandTextView.text = item.name
        binding.brandImageView.loadFromUrlWithAnimation(item.logoUrl)
        itemView.setOnClickListener {
            onClick(item)
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): BrandsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = BrandItemLittleBinding.inflate(layoutInflater, parent, false)
            return BrandsViewHolder(binding)
        }
    }
}