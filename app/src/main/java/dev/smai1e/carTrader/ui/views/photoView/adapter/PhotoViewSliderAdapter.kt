package dev.smai1e.carTrader.ui.views.photoView.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PhotoViewSliderAdapter : RecyclerView.Adapter<PhotoViewSliderViewHolder>() {

    var urlList: List<String> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewSliderViewHolder =
        PhotoViewSliderViewHolder.inflateFrom(parent)

    override fun getItemCount(): Int = urlList.size

    override fun onBindViewHolder(holder: PhotoViewSliderViewHolder, position: Int) {
        val itemUrl = urlList[position]
        holder.bind(itemUrl)
    }
}