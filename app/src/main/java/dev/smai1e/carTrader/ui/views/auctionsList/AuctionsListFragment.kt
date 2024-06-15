package dev.smai1e.carTrader.ui.views.auctionsList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.FragmentAuctionsListBinding
import dev.smai1e.carTrader.databinding.PartResultBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.views.auctionsList.auctionsAdapter.AuctionsAdapter
import dev.smai1e.carTrader.ui.views.auctionsList.brandsAdapter.PopularBrandsAdapter
import dev.smai1e.carTrader.ui.views.auctionsList.searchBarAdapter.SearchBarAdapter
import dev.smai1e.carTrader.ui.views.tabs.TabsFragmentDirections
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.utils.findTopNavController
import javax.inject.Inject

class AuctionsListFragment : BaseFragment() {

    private var _binding: FragmentAuctionsListBinding? = null
    private val binding get() = _binding!!

    private var _resultBinding: PartResultBinding? = null
    private val resultBinding get() = _resultBinding!!

    @Inject
    lateinit var factory: AuctionsListViewModelFactory
    private val viewModel: AuctionsListViewModel by activityViewModels {
        factory
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(queryText: String): Boolean {
            search(queryText)
            return true
        }
        override fun onQueryTextChange(queryText: String): Boolean {
            return false
        }
    }
    private val popularBrandsAdapter = PopularBrandsAdapter(::onBrandPressed)
    private val searchBarAdapter = SearchBarAdapter(
        queryTextListener,
        popularBrandsAdapter,
        ::onFilterButtonPressed
    )
    private val auctionsAdapter = AuctionsAdapter(::onLotPressed)
    private val concatAdapter = ConcatAdapter(searchBarAdapter, auctionsAdapter)

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuctionsListBinding.inflate(inflater, container, false)
        _resultBinding = PartResultBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        updateUi()
        initButtonClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _resultBinding = null
    }

    private fun initButtonClickListeners() {
        resultBinding.tryAgainButton.setOnClickListener {
            viewModel.reset()
        }
    }

    private fun search(searchRequest: String) {
        viewModel.getBySearchRequest(searchRequest)
    }

    private fun onLotPressed(auctionId: Long) {
        val action = TabsFragmentDirections.actionTabsFragmentToDetailsFragment(auctionId)
        findTopNavController().navigate(action)
    }

    private fun onBrandPressed(brand: String) {
        viewModel.getByBrand(brand)
    }

    private fun onFilterButtonPressed() {
        findTopNavController().navigate(R.id.filterFragment)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = concatAdapter
        }
    }

    private fun updateUi() {
        viewModel.uiState.collectFlow { uiState ->
            resultBinding.progressBar.visibility = if (
                uiState.auctionsUiState is State.Loading ||
                uiState.popularBrandsUiState is State.Loading
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.recyclerView.visibility = if (
                uiState.auctionsUiState is State.Success &&
                uiState.popularBrandsUiState is State.Success
            ) {
                auctionsAdapter.submitList((uiState.auctionsUiState as State.Success).data)
                popularBrandsAdapter.brandsList = (uiState.popularBrandsUiState as State.Success).data
                View.VISIBLE
            } else {
                View.GONE
            }

            resultBinding.errorContainer.visibility = if (uiState.auctionsUiState is State.Error) {
                showErrorSnackbar((uiState.auctionsUiState as State.Error).getErrorText(requireContext()))
                View.VISIBLE
            } else {
                View.GONE
            }
            if (uiState.popularBrandsUiState is State.Error) {
                showErrorSnackbar((uiState.popularBrandsUiState as State.Error).getErrorText(requireContext()))
            }
        }
    }
}