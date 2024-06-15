package dev.smai1e.carTrader.ui.views.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.domain.repositoryInterfaces.Token
import dev.smai1e.carTrader.domain.repositoryInterfaces.TokenRepository
import dev.smai1e.carTrader.domain.useCases.SignInUseCase
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _navigateToTabsFragmentEvent = MutableStateFlow<State<Token>>(State.Idle)
    val navigateToTabsFragmentEvent: StateFlow<State<Token>> = _navigateToTabsFragmentEvent.asStateFlow()

    init {
        tokenRepository.clearAuthToken()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password).let { result ->
                _navigateToTabsFragmentEvent.update { result.toState() }
            }
        }
    }
}

class SignInViewModelFactory @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val tokenRepository: TokenRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(signInUseCase, tokenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}