package dev.smai1e.carTrader.ui.views.carPicker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.databinding.ModelItemBinding
import dev.smai1e.carTrader.domain.models.Model

class ModelsViewHolder(
    private val binding: ModelItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Model, onClick: (Model) -> Unit) {
        binding.modelTextView.text = item.name
        itemView.setOnClickListener {
            onClick(item)
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): ModelsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ModelItemBinding.inflate(layoutInflater, parent, false)
            return ModelsViewHolder(binding)
        }
    }
}