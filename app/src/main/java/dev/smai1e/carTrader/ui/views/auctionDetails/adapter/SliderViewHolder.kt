package dev.smai1e.carTrader.ui.views.auctionDetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.databinding.PhotoContainerBinding
import dev.smai1e.carTrader.utils.loadFromUrl

class SliderViewHolder(
    private val binding: PhotoContainerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(urlList: List<String>, position: Int, onClick: (List<String>, Int) -> Unit) {
        val itemUrl = urlList[position]
        binding.photo.loadFromUrl(itemUrl)
        binding.photo.setOnClickListener {
            onClick(urlList, position)
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): SliderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PhotoContainerBinding.inflate(layoutInflater, parent, false)
            return SliderViewHolder(binding)
        }
    }
}