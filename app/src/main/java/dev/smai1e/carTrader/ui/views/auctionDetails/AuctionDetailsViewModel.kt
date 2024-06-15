package dev.smai1e.carTrader.ui.views.auctionDetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.data.network.models.request.BidRequest
import dev.smai1e.carTrader.domain.models.Bid
import dev.smai1e.carTrader.ui.models.TimeProgressState
import dev.smai1e.carTrader.domain.repositoryInterfaces.AuctionsRepository
import dev.smai1e.carTrader.domain.repositoryInterfaces.BidsRepository
import dev.smai1e.carTrader.domain.repositoryInterfaces.UserRepository
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
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.models.AuctionUI
import dev.smai1e.carTrader.ui.models.BidUI
import dev.smai1e.carTrader.ui.models.UserInfoUI
import dev.smai1e.carTrader.ui.models.toAuctionUI
import dev.smai1e.carTrader.ui.models.toBidUI
import dev.smai1e.carTrader.ui.models.toUserInfoUI
import dev.smai1e.carTrader.ui.toState
import dev.smai1e.carTrader.utils.TimerHandler
import dev.smai1e.carTrader.utils.parseToZonedDateTime
import kotlinx.coroutines.flow.asStateFlow
import java.time.ZoneId
import java.time.ZonedDateTime

class AuctionDetailsViewModel(
    private val id: Long,
    private val auctionsRepository: AuctionsRepository,
    private val userRepository: UserRepository,
    private val bidsRepository: BidsRepository
) : ViewModel() {

    private val _auctionInfoFlow = MutableStateFlow<State<AuctionUI>>(State.Idle)
    private val _bidsListFlow = MutableStateFlow<State<List<BidUI>>>(State.Idle)
    private val _sellerInfoFlow = MutableStateFlow<State<UserInfoUI>>(State.Idle)
    private val _timeStateFlow = MutableStateFlow<State<TimeProgressState>>(State.Idle)

    val uiState: StateFlow<UiState> = combine(
        _auctionInfoFlow, _bidsListFlow, _sellerInfoFlow, _timeStateFlow, ::merge
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiState(
            State.Idle,
            State.Idle,
            State.Idle,
            State.Idle,
            bottomBidsBarIsVisible = false,
            bottomProtocolBarIsVisible = false
        )
    )

    private val _protocolSaveEvent = MutableSharedFlow<State<String>>()
    val protocolSaveEvent: SharedFlow<State<String>> = _protocolSaveEvent.asSharedFlow()

    private val _bidInsertEvent = MutableSharedFlow<State<Bid>>()
    val bidInsertEvent: SharedFlow<State<Bid>> = _bidInsertEvent.asSharedFlow()

    private val _bidAmountChangeEvent = MutableStateFlow(0)
    val bidAmountChangeEvent: StateFlow<Int> = _bidAmountChangeEvent.asStateFlow()

    init {
        getAuction()
        getBids()
    }

    private fun merge(
        auctionInfoState: State<AuctionUI>,
        bidsListState: State<List<BidUI>>,
        sellerInfoState: State<UserInfoUI>,
        timeState: State<TimeProgressState>
    ): UiState {

        val currentDate = ZonedDateTime.now(ZoneId.systemDefault())
        val bottomBidsBarIsVisible = if (auctionInfoState is State.Success) {
            currentDate.isAfter(auctionInfoState.data.openDate.parseToZonedDateTime())
            && currentDate.isBefore(auctionInfoState.data.closeDate.parseToZonedDateTime())
        } else {
            false
        }
        val bottomProtocolBarIsVisible = if (auctionInfoState is State.Success) {
            currentDate.isAfter(auctionInfoState.data.closeDate.parseToZonedDateTime())
        } else {
            false
        }

        return UiState(
            auctionInfoState,
            bidsListState,
            sellerInfoState,
            timeState,
            bottomBidsBarIsVisible,
            bottomProtocolBarIsVisible
        )
    }

    fun changeBid(bidAmount: Int) {
        viewModelScope.launch {
            _bidAmountChangeEvent.update { bidAmount }
        }
    }

    fun raiseBid() {
        viewModelScope.launch {
            val currBidAmount = _bidAmountChangeEvent.value
            if (currBidAmount >= Int.MAX_VALUE - MONEY_SHIFT) return@launch
            val resultBidAmount = currBidAmount + MONEY_SHIFT
            changeBid(resultBidAmount)
        }
    }

    fun lowerBid() {
        viewModelScope.launch {
            val currBidAmount = _bidAmountChangeEvent.value
            if (currBidAmount < MONEY_SHIFT) return@launch
            val resultBidAmount = currBidAmount - MONEY_SHIFT
            changeBid(resultBidAmount)
        }
    }

    private fun getAuction() {
        viewModelScope.launch {
            auctionsRepository.fetchAuctionById(id).collect { result ->
                val auctionInfo = result
                    .map { it.toAuctionUI() }
                    .toState()
                if (auctionInfo is State.Success) {
                    getSellerInfo(auctionInfo.data.sellerId)
                    setValueOnProgressbar(auctionInfo.data)
                }
                _auctionInfoFlow.update { auctionInfo }
            }
        }
    }

    private fun getBids() {
        viewModelScope.launch {
            bidsRepository.fetchBidsByAuctionId(id).collect { result ->
                val uiBids = result
                    .map { bids -> bids.map { it.toBidUI() } }
                    .toState()
                _bidsListFlow.update { uiBids }
            }
        }
    }

    private fun getSellerInfo(sellerId: Long) {
        viewModelScope.launch {
            userRepository.fetchSellerInfo(sellerId).collect { result ->
                _sellerInfoFlow.update {
                    result.map { it.toUserInfoUI() }.toState()
                }
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

    fun insertBid() {
        viewModelScope.launch {
            bidsRepository.insertBid(
                BidRequest(id, _bidAmountChangeEvent.value)
            ).let { result ->
                _bidInsertEvent.emit(result.toState())
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

    data class UiState(
        val auctionUiState: State<AuctionUI>,
        val bidsUiState: State<List<BidUI>>,
        val sellerUiState: State<UserInfoUI>,
        val timeUiState: State<TimeProgressState>,
        val bottomBidsBarIsVisible: Boolean,
        val bottomProtocolBarIsVisible: Boolean
    )

    companion object {
        private const val MONEY_SHIFT = 10_000
    }
}

class AuctionDetailsViewModelFactory @AssistedInject constructor(
    @Assisted("auctionId") val id: Long,
    private val auctionsRepository: AuctionsRepository,
    private val userRepository: UserRepository,
    private val bidsRepository: BidsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuctionDetailsViewModel::class.java)) {
            return AuctionDetailsViewModel(
                id,
                auctionsRepository,
                userRepository,
                bidsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("auctionId") id: Long): AuctionDetailsViewModelFactory
    }
}