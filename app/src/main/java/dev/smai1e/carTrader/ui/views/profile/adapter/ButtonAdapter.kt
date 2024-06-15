package dev.smai1e.carTrader.ui.views.profile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.ui.models.NavButton
import dev.smai1e.carTrader.ui.models.NavParams

class ButtonAdapter(
    private val onClick: (NavParams) -> Unit
) : RecyclerView.Adapter<ButtonViewHolder>() {

    var buttonsList: List<NavButton> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder =
        ButtonViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        val menuButton = buttonsList[position]
        holder.bind(menuButton, onClick)
    }

    override fun getItemCount(): Int = buttonsList.size
}