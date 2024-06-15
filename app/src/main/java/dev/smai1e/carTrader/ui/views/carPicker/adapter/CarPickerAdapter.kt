package dev.smai1e.carTrader.ui.views.carPicker.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.domain.models.Brand
import dev.smai1e.carTrader.domain.models.CarPickItem
import dev.smai1e.carTrader.domain.models.Model

class CarPickerAdapter<T: Any>(
    private val onClick: (T) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<CarPickItem> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_BRAND -> BrandsViewHolder.inflateFrom(parent)
            TYPE_MODEL -> ModelsViewHolder.inflateFrom(parent)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_BRAND -> {
                val item = items[position] as Brand
                (holder as BrandsViewHolder).bind(item, onClick as (Brand) -> Unit)
            }

            TYPE_MODEL -> {
                val item = items[position] as Model
                (holder as ModelsViewHolder).bind(item, onClick as (Model) -> Unit)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is Brand -> TYPE_BRAND
            is Model -> TYPE_MODEL
            else -> throw IllegalArgumentException("Invalid item type")
        }

    private companion object {
        private const val TYPE_BRAND = 1
        private const val TYPE_MODEL = 2
    }
}