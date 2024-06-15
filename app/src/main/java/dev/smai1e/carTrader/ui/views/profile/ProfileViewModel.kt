package dev.smai1e.carTrader.ui.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.domain.models.UserInfo
import dev.smai1e.carTrader.domain.repositoryInterfaces.UserRepository
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<State<UserInfo>>(State.Idle)
    val uiState: StateFlow<State<UserInfo>> = _uiState.asStateFlow()

    init {
        getUserInfo()
    }

    fun getUserInfo() {
        viewModelScope.launch {
            userRepository.fetchUserInfo().let { result ->
                _uiState.update { result.toState() }
            }
        }
    }
}

class ProfileViewModelFactory @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}