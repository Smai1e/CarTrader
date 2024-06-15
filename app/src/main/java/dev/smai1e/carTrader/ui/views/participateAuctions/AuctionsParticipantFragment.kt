package dev.smai1e.carTrader.ui.views.participateAuctions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.smai1e.carTrader.databinding.FragmentAuctionsParticipantBinding
import dev.smai1e.carTrader.databinding.PartResultBinding
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.views.participateAuctions.adapter.ParticipateAuctionsAdapter
import dev.smai1e.carTrader.ui.views.tabs.TabsFragmentDirections
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.utils.findTopNavController
import javax.inject.Inject

class AuctionsParticipantFragment : BaseFragment() {

    private var _binding: FragmentAuctionsParticipantBinding? = null
    private val binding get() = _binding!!

    private var _resultBinding: PartResultBinding? = null
    private val resultBinding get() = _resultBinding!!

    @Inject
    lateinit var factory: AuctionsParticipantViewModelFactory
    private val viewModel: AuctionsParticipantViewModel by viewModels {
        factory
    }

    private val auctionsAdapter = ParticipateAuctionsAdapter(::onAuctionPressed)

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuctionsParticipantBinding.inflate(layoutInflater, container, false)
        _resultBinding = PartResultBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        updateUi()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAuctions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _resultBinding = null
        _binding = null
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.auctionsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = auctionsAdapter
        }
    }

    private fun onAuctionPressed(auctionId: Long) {
        val direction =
            TabsFragmentDirections.actionTabsFragmentToDetailsFragment(auctionId)
        findTopNavController().navigate(direction)
    }

    private fun updateUi() {
        viewModel.uiState.collectFlow { uiState ->
            resultBinding.progressBar.visibility = if (uiState.auctionsUiState is State.Loading) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.auctionsRecyclerView.visibility =
                if (uiState.auctionsUiState is State.Success) {
                    auctionsAdapter.submitList((uiState.auctionsUiState as State.Success).data)
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
        }
    }
}