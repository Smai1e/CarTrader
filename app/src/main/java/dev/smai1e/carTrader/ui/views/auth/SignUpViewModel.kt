package dev.smai1e.carTrader.ui.views.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.domain.models.SignUpData
import dev.smai1e.carTrader.domain.useCases.SignUpUseCase
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _signUpEvent = MutableStateFlow<State<Unit>>(State.Idle)
    val signUpEvent: StateFlow<State<Unit>> = _signUpEvent.asStateFlow()

    fun signUp(data: SignUpData) {
        viewModelScope.launch {
            signUpUseCase(data).let { result ->
                _signUpEvent.update { result.toState() }
            }
        }
    }
}

class SignUpViewModelFactory @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(signUpUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}