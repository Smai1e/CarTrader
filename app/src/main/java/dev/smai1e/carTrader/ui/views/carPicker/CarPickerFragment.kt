package dev.smai1e.carTrader.ui.views.carPicker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.smai1e.carTrader.databinding.FragmentCarPickerBinding
import dev.smai1e.carTrader.databinding.PartResultBinding
import dev.smai1e.carTrader.domain.models.CarPickItem
import dev.smai1e.carTrader.ui.views.BaseFragment
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.views.carPicker.adapter.CarPickerAdapter
import dev.smai1e.carTrader.appComponent
import javax.inject.Inject

class CarPickerFragment : BaseFragment() {

    private var _binding: FragmentCarPickerBinding? = null
    private val binding get() = _binding!!

    private var _resultBinding: PartResultBinding? = null
    private val resultBinding get() = _resultBinding!!

    @Inject
    lateinit var factory: CarPickerViewModelFactory
    private val viewModel: CarPickerViewModel by viewModels {
        factory
    }

    private val carPickerAdapter = CarPickerAdapter(::onItemPressed)

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarPickerBinding.inflate(layoutInflater, container, false)
        _resultBinding = PartResultBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initBackEventListener()
        initTryAgainButtonListener()
        updateUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _resultBinding = null
    }

    private fun <T: CarPickItem> onItemPressed(item: T) {
        viewModel.onItemPressed(item)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.carPickerRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = carPickerAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    linearLayoutManager.orientation
                )
            )
        }

    }

    private fun updateUi() {
        viewModel.uiState.collectFlow { uiState ->
            resultBinding.progressBar.visibility =
                if (uiState is State.Loading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

            binding.carPickerRecyclerView.visibility =
                if (uiState is State.Success) {
                    carPickerAdapter.items = uiState.data
                    View.VISIBLE
                } else {
                    View.GONE
                }

            resultBinding.errorContainer.visibility =
                if (uiState is State.Error) {
                    showErrorSnackbar(uiState.getErrorText(requireContext()))
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }
    }

    private fun initBackEventListener() {
        viewModel.goBackEvent.collectFlow { isGoBack ->
            if (isGoBack) {
                setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(EXTRA_KEY to viewModel.getCarName())
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun initTryAgainButtonListener() {
        resultBinding.tryAgainButton.setOnClickListener {
            viewModel.getBrands()
        }
    }

    companion object {
        const val REQUEST_KEY = "car_picker_fragment_result"
        const val EXTRA_KEY = "car_name"
    }
}