package dev.smai1e.carTrader.ui.views.newLot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.databinding.ItemPhotoBinding
import dev.smai1e.carTrader.utils.loadFromUrl

class NewLotPhotoHolder(
    private val binding: ItemPhotoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uri: String, onClick: (String) -> Unit) {
        binding.ivImage.loadFromUrl(uri)
        binding.deleteButton.setOnClickListener {
            onClick(uri)
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): NewLotPhotoHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPhotoBinding.inflate(layoutInflater, parent, false)
            return NewLotPhotoHolder(binding)
        }
    }
}