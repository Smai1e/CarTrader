package dev.smai1e.carTrader.ui.views.auctionDetails.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SliderAdapter(
    private val onClick: (List<String>, Int) -> Unit
) : ListAdapter<String, SliderViewHolder>(URL_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder =
        SliderViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(currentList, position, onClick)
    }

    companion object {
        private val URL_DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }
}