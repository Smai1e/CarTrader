package dev.smai1e.carTrader.ui.views.newLot.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class NewLotPhotoAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<String, NewLotPhotoHolder>(URL_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewLotPhotoHolder =
        NewLotPhotoHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: NewLotPhotoHolder, position: Int) {
        val uriItem = getItem(position)
        holder.bind(uriItem, onClick)
    }

    override fun submitList(list: List<String>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    companion object {
        private val URL_DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        }
    }
}