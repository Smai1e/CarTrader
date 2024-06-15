package dev.smai1e.carTrader.ui.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.data.network.models.request.CardRequest
import dev.smai1e.carTrader.data.network.models.request.WalletRequest
import dev.smai1e.carTrader.domain.repositoryInterfaces.PaymentRepository
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class WalletBottomDialogViewModel(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<State<Boolean>>(State.Idle)
    val uiState: StateFlow<State<Boolean>> = _uiState.asStateFlow()

    fun replenish(money: Int, number: String, validityPeriod: String, cvv: Int) {
        viewModelScope.launch {
            paymentRepository.replenishAccount(
                WalletRequest(money, CardRequest(number, validityPeriod, cvv))
            ).let { result ->
                _uiState.update { result.toState() }
            }
        }
    }

    fun withdraw(money: Int, number: String) {
        viewModelScope.launch {
            paymentRepository.withdrawAccount(
                WalletRequest(money, CardRequest(number = number))
            ).let { result ->
                _uiState.update { result.toState() }
            }
        }
    }
}

class WalletBottomDialogViewModelFactory @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalletBottomDialogViewModel::class.java)) {
            return WalletBottomDialogViewModel(paymentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}