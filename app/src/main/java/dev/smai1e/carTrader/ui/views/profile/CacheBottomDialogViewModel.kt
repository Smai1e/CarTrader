package dev.smai1e.carTrader.ui.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.domain.models.StorageSpaceState
import dev.smai1e.carTrader.domain.useCases.ClearCacheUseCase
import dev.smai1e.carTrader.domain.useCases.GetStorageInfoUseCase
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CacheBottomDialogViewModel(
    private val getStorageInfoUseCase: GetStorageInfoUseCase,
    private val clearCacheUseCase: ClearCacheUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<State<StorageSpaceState>>(State.Idle)
    val uiState: StateFlow<State<StorageSpaceState>> = _uiState.asStateFlow()

    private val _clearCacheResultFlow = MutableStateFlow<State<Unit>>(State.Idle)
    val clearCacheResultFlow: StateFlow<State<Unit>> = _clearCacheResultFlow.asStateFlow()

    init {
        getStorageInfo()
    }

    private fun getStorageInfo() {
        viewModelScope.launch {
            getStorageInfoUseCase().let { result ->
                _uiState.update { result.toState() }
            }
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            clearCacheUseCase().let { result ->
                _clearCacheResultFlow.update { result.toState() }
            }
        }
    }
}

class CacheBottomDialogViewModelFactory @Inject constructor(
    private val getStorageInfoUseCase: GetStorageInfoUseCase,
    private val clearCacheUseCase: ClearCacheUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CacheBottomDialogViewModel::class.java)) {
            return CacheBottomDialogViewModel(
                getStorageInfoUseCase,
                clearCacheUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}