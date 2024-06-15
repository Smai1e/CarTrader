package dev.smai1e.carTrader.ui.views.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.domain.repositoryInterfaces.TokenRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableSharedFlow<Boolean>()
    val launchMainScreenEvent: SharedFlow<Boolean> = _launchMainScreenEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(3500)
            (tokenRepository.fetchAuthToken() != null).let { isSignedIn ->
                _launchMainScreenEvent.emit(isSignedIn)
            }
        }
    }
}

class SplashViewModelFactory @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(tokenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}