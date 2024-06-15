package dev.smai1e.carTrader.ui.views.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.CacheBottomDialogBinding
import dev.smai1e.carTrader.domain.models.StorageSpaceState
import dev.smai1e.carTrader.ui.views.BaseDialogFragment
import dev.smai1e.carTrader.ui.composeUI.StorageSpaceBar
import dev.smai1e.carTrader.appComponent
import dev.smai1e.carTrader.utils.findTopNavController
import javax.inject.Inject

class CacheBottomDialog : BaseDialogFragment() {

    private var _binding: CacheBottomDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: CacheBottomDialogViewModelFactory
    private val viewModel: CacheBottomDialogViewModel by viewModels {
        factory
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CacheBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clearCacheButton.setOnClickListener {
            viewModel.clearCache()
        }
        viewModel.uiState.collectFlow { result ->
            result
                .onSuccess {
                    initStorageSpaceBar(it)
                    binding.clearCacheButton.text = getString(R.string.clear_cache_button, it.cache.toString())
                }
                .onError(::showErrorSnackbar)
        }
        viewModel.clearCacheResultFlow.collectFlow { result ->
            result
                .onSuccess {
                    showSuccessSnackbar(R.string.operation_was_completed_successfully)
                    goBack()
                }
                .onError(::showErrorSnackbar)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initStorageSpaceBar(state: StorageSpaceState) {
        binding.capacityBar.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    StorageSpaceBar(state)
                }
            }
        }
    }

    private fun goBack() {
        findTopNavController().popBackStack()
    }
}