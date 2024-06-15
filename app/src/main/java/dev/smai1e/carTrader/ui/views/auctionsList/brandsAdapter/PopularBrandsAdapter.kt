package dev.smai1e.carTrader.ui.views.auctionsList.brandsAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.domain.models.Brand

class PopularBrandsAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<PopularBrandsViewHolder>() {

    var brandsList: List<Brand> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularBrandsViewHolder =
        PopularBrandsViewHolder.inflateFrom(parent)

    override fun getItemCount() = brandsList.size

    override fun onBindViewHolder(holder: PopularBrandsViewHolder, position: Int) {
        val item = brandsList[position]
        holder.bind(item, onClick)
    }
}