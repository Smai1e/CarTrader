package dev.smai1e.carTrader.ui.views.auctionsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.domain.models.Brand
import dev.smai1e.carTrader.domain.models.CarName
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.data.network.models.request.SearchParametersRequest
import dev.smai1e.carTrader.domain.repositoryInterfaces.AuctionsRepository
import dev.smai1e.carTrader.domain.repositoryInterfaces.CarsRepository
import dev.smai1e.carTrader.ui.models.AuctionUI
import dev.smai1e.carTrader.ui.models.ColorUI
import dev.smai1e.carTrader.ui.models.DriveWheelUI
import dev.smai1e.carTrader.ui.models.GearboxUI
import dev.smai1e.carTrader.ui.models.toAuctionUI
import dev.smai1e.carTrader.ui.models.toColor
import dev.smai1e.carTrader.ui.models.toDriveWheel
import dev.smai1e.carTrader.ui.models.toGearbox
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuctionsListViewModel(
    private val auctionsRepository: AuctionsRepository,
    private val carsRepository: CarsRepository
) : ViewModel() {

    private val _auctionsListFlow = MutableStateFlow<State<List<AuctionUI>>>(State.Loading)
    private val _brandsListFlow = MutableStateFlow<State<List<Brand>>>(State.Loading)

    private val _searchParamsFlow = MutableStateFlow(SearchParametersRequest())
    val searchParamsFlow: StateFlow<SearchParametersRequest> = _searchParamsFlow.asStateFlow()

    var uiState: StateFlow<UiState> = combine(
        _auctionsListFlow,
        _brandsListFlow,
    ) { auctionsListResult, brandsListResult ->
        UiState(auctionsListResult, brandsListResult)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiState(
            State.Loading,
            State.Loading
        )
    )

    init {
        getAuctions()
        getPopularBrands()
    }

    fun getAuctions() {
        viewModelScope.launch {
            auctionsRepository.fetchAllAuctions(_searchParamsFlow.value).collect { result ->
                val uiAuctions = result
                    .map { auctions -> auctions.map { it.toAuctionUI() } }
                    .toState()
                _auctionsListFlow.update { uiAuctions }
            }
        }
    }

    private fun getPopularBrands() {
        viewModelScope.launch {
            carsRepository.fetchPopularBrands().let { result ->
                _brandsListFlow.update { result.toState() }
            }
        }
    }

    fun reset() = viewModelScope.launch {
        _searchParamsFlow.update { SearchParametersRequest() }
        getAuctions()
    }

    fun getByBrand(brand: String) = viewModelScope.launch {
        _searchParamsFlow.update { SearchParametersRequest(brand = brand) }
        getAuctions()
    }

    fun getBySearchRequest(request: String) = viewModelScope.launch {
        _searchParamsFlow.update { SearchParametersRequest(searchRequest = request) }
        getAuctions()
    }

    fun setAuctionDate(openDate: String, closeDate: String) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(openDate = openDate, closeDate = closeDate) }
    }

    fun setCarName(car: CarName) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(brand = car.brand, model = car.model) }
    }

    fun setHorsepowerStart(horsepower: Int?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(horsepowerStart = horsepower) }
    }

    fun setHorsepowerEnd(horsepower: Int?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(horsepowerEnd = horsepower) }
    }

    fun setMinBidStart(amount: Int) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(minBidStart = amount) }
    }

    fun setMinBidEnd(amount: Int) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(minBidEnd = amount) }
    }

    fun setManufacturerDateStart(year: Int?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(manufacturerDateStart = year) }
    }

    fun setManufacturerDateEnd(year: Int?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(manufacturerDateEnd = year) }
    }

    fun setMileageStart(mileage: Int?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(mileageStart = mileage) }
    }

    fun setMileageEnd(mileage: Int?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(mileageEnd = mileage) }
    }

    fun setGearbox(gearbox: GearboxUI?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(gearbox = gearbox?.toGearbox()) }
    }

    fun setDriveWheel(driveWheel: DriveWheelUI?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(driveWheel = driveWheel?.toDriveWheel()) }
    }

    fun setColor(color: ColorUI?) = viewModelScope.launch {
        _searchParamsFlow.update { it.copy(color = color?.toColor()) }
    }

    data class UiState(
        var auctionsUiState: State<List<AuctionUI>>,
        var popularBrandsUiState: State<List<Brand>>
    )
}

class AuctionsListViewModelFactory @Inject constructor(
    private val auctionsRepository: AuctionsRepository,
    private val carsRepository: CarsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuctionsListViewModel::class.java)) {
            return AuctionsListViewModel(auctionsRepository, carsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}