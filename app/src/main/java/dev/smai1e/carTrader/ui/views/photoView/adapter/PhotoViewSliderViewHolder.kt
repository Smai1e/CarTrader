package dev.smai1e.carTrader.ui.views.photoView.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.databinding.ZoomPhotoContainerBinding
import dev.smai1e.carTrader.utils.loadFromUrl

class PhotoViewSliderViewHolder(
    private val binding: ZoomPhotoContainerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(itemUrl: String) {
        binding.zoomedPhoto.loadFromUrl(itemUrl)
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): PhotoViewSliderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ZoomPhotoContainerBinding.inflate(layoutInflater, parent, false)
            return PhotoViewSliderViewHolder(binding)
        }
    }
}