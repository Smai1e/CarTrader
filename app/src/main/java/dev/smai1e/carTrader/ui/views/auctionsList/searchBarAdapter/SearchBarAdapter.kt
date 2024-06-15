package dev.smai1e.carTrader.ui.views.auctionsList.searchBarAdapter

import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.ui.views.auctionsList.brandsAdapter.PopularBrandsAdapter

class SearchBarAdapter(
    private val queryTextListener: OnQueryTextListener,
    private val popularBrandsAdapter: PopularBrandsAdapter,
    private val onClickFilterButton: () -> Unit
) : RecyclerView.Adapter<SearchBarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBarViewHolder =
        SearchBarViewHolder.inflateFrom(parent)

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: SearchBarViewHolder, position: Int) {
        holder.bind(queryTextListener, popularBrandsAdapter, onClickFilterButton)
    }
}