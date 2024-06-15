package dev.smai1e.carTrader.ui.views.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.ui.models.NavButton
import dev.smai1e.carTrader.ui.models.NavParams
import dev.smai1e.carTrader.databinding.ItemMenuButtonBinding

class ButtonViewHolder(
    private val binding: ItemMenuButtonBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(button: NavButton, onClick: (NavParams) -> Unit) {
        val resources = itemView.resources
        binding.title.text = resources.getString(button.title)
        itemView.setOnClickListener {
            onClick(button.navParams)
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): ButtonViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMenuButtonBinding.inflate(layoutInflater, parent, false)
            return ButtonViewHolder(binding)
        }
    }
}