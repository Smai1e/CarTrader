package dev.smai1e.carTrader.ui.views.auctionsList.searchBarAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import dev.smai1e.carTrader.databinding.SearchBarBinding
import dev.smai1e.carTrader.ui.views.auctionsList.brandsAdapter.PopularBrandsAdapter

class SearchBarViewHolder(
    private val binding: SearchBarBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        queryTextListener: SearchView.OnQueryTextListener,
        popularBrandsAdapter: PopularBrandsAdapter,
        onClickFilterButton: () -> Unit
    ) {
        binding.apply {
            popularBrandsRecyclerView.adapter = popularBrandsAdapter
            searchView.setOnQueryTextListener(queryTextListener)
            filterButton.setOnClickListener {
                onClickFilterButton()
            }
        }
    }

    companion object {
        fun inflateFrom(parent: ViewGroup): SearchBarViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SearchBarBinding.inflate(layoutInflater, parent, false)
            return SearchBarViewHolder(binding)
        }
    }
}