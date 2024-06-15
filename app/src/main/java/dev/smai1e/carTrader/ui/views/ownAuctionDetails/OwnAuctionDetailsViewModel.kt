package dev.smai1e.carTrader.ui.views.ownAuctionDetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.domain.models.BidWithUser
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.models.TimeProgressState
import dev.smai1e.carTrader.domain.repositoryInterfaces.AuctionsRepository
import dev.smai1e.carTrader.domain.useCases.GetBidsWithUserUseCase
import dev.smai1e.carTrader.ui.models.AuctionUI
import dev.smai1e.carTrader.ui.models.toAuctionUI
import dev.smai1e.carTrader.ui.toState
import dev.smai1e.carTrader.utils.TimerHandler
import dev.smai1e.carTrader.utils.parseToZonedDateTime
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

class OwnAuctionDetailsViewModel(
    private val id: Long,
    private val auctionsRepository: AuctionsRepository,
    private val getBidsWithUserUseCase: GetBidsWithUserUseCase
) : ViewModel() {

    private val _auctionInfoFlow = MutableStateFlow<State<AuctionUI>>(State.Idle)
    private val _bidsListFlow = MutableStateFlow<State<List<BidWithUser>>>(State.Idle)
    private val _timeStateFlow = MutableStateFlow<State<TimeProgressState>>(State.Idle)

    private val _protocolSaveEvent = MutableSharedFlow<State<String>>()
    val protocolStateEvent: SharedFlow<State<String>> = _protocolSaveEvent.asSharedFlow()

    val uiState: StateFlow<UiState> = combine(
        _auctionInfoFlow, _bidsListFlow, _timeStateFlow, ::merge
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiState(
            State.Idle,
            State.Idle,
            State.Idle,
            false
        )
    )

    init {
        getAuction()
        getBids()
    }

    private fun merge(
        auctionInfoState: State<AuctionUI>,
        bidsListState: State<List<BidWithUser>>,
        timeState: State<TimeProgressState>
    ): UiState {

        val bottomProtocolBarIsVisible = if (auctionInfoState is State.Success) {
            val currentDate = ZonedDateTime.now(ZoneId.systemDefault())
            val isVisible = currentDate.isAfter(auctionInfoState.data.closeDate.parseToZonedDateTime())
            isVisible
        } else {
            false
        }

        return UiState(
            auctionInfoState,
            bidsListState,
            timeState,
            bottomProtocolBarIsVisible
        )
    }

    private fun getAuction() {
        viewModelScope.launch {
            auctionsRepository.fetchAuctionById(id).collect { result ->
                val auctionInfo = result
                    .map { it.toAuctionUI() }
                    .toState()
                if (auctionInfo is State.Success) {
                    setValueOnProgressbar(auctionInfo.data)
                }
                _auctionInfoFlow.update { auctionInfo }
            }
        }
    }

    private fun getBids() {
        viewModelScope.launch {
            getBidsWithUserUseCase(id).collect { result ->
                _bidsListFlow.update { result.toState() }
            }
        }
    }

    private fun setValueOnProgressbar(auction: AuctionUI) {
        viewModelScope.launch {
            TimerHandler(this, auction).flow.collect { result ->
                _timeStateFlow.update { result.toState() }
            }
        }
    }

    fun getProtocol(uri: Uri) {
        viewModelScope.launch {
            auctionsRepository.downloadProtocol(id, uri).let { result ->
                _protocolSaveEvent.emit(result.toState())
            }
        }
    }

    data class UiState(
        val auctionUiState: State<AuctionUI>,
        val bidsUiState: State<List<BidWithUser>>,
        val timeUiState: State<TimeProgressState>,
        val bottomProtocolBarIsVisible: Boolean
    )
}

class OwnAuctionDetailsViewModelFactory @AssistedInject constructor(
    @Assisted("auctionId") val id: Long,
    private val auctionsRepository: AuctionsRepository,
    private val getBidsWithUserUseCase: GetBidsWithUserUseCase,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OwnAuctionDetailsViewModel::class.java)) {
            return OwnAuctionDetailsViewModel(
                id,
                auctionsRepository,
                getBidsWithUserUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("auctionId") id: Long): OwnAuctionDetailsViewModelFactory
    }
}