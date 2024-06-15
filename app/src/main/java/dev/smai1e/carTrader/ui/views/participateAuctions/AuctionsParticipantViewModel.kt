package dev.smai1e.carTrader.ui.views.participateAuctions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.ui.models.AuctionUI
import dev.smai1e.carTrader.ui.models.toAuctionUI
import dev.smai1e.carTrader.domain.repositoryInterfaces.AuctionsRepository
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuctionsParticipantViewModel(
    private val auctionsRepository: AuctionsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(State.Idle))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getAuctions() {
        viewModelScope.launch {
            auctionsRepository.fetchAuctionsParticipant().let { result ->
                val uiAuctions = result
                    .map { auctions -> auctions.map { it.toAuctionUI() } }
                    .toState()
                _uiState.update { UiState(uiAuctions) }
            }
        }
    }

    data class UiState(
        var auctionsUiState: State<List<AuctionUI>>
    )
}

class AuctionsParticipantViewModelFactory @Inject constructor(
    private val auctionsRepository: AuctionsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuctionsParticipantViewModel::class.java)) {
            return AuctionsParticipantViewModel(auctionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}