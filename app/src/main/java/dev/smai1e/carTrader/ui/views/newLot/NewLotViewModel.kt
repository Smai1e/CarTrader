package dev.smai1e.carTrader.ui.views.newLot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.data.network.models.request.AuctionRequest
import dev.smai1e.carTrader.domain.models.CarName
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.domain.useCases.UploadAuctionDataUseCase
import dev.smai1e.carTrader.ui.models.ColorUI
import dev.smai1e.carTrader.ui.models.DriveWheelUI
import dev.smai1e.carTrader.ui.models.GearboxUI
import dev.smai1e.carTrader.ui.models.toColor
import dev.smai1e.carTrader.ui.models.toDriveWheel
import dev.smai1e.carTrader.ui.models.toGearbox
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewLotViewModel(
    private val uploadAuctionDataUseCase: UploadAuctionDataUseCase
) : ViewModel() {

    private val _goBackEvent = MutableStateFlow<State<Unit>>(State.Idle)
    val goBackEvent: StateFlow<State<Unit>> = _goBackEvent.asStateFlow()

    private val _auctionDataFlow = MutableStateFlow(AuctionRequest())
    val auctionDataFlow: StateFlow<AuctionRequest> = _auctionDataFlow.asStateFlow()

    private val _selectedImagesFlow = MutableStateFlow<List<String>>(emptyList())
    val selectedImagesFlow: StateFlow<List<String>> = _selectedImagesFlow.asStateFlow()

    fun uploadAuction() {
        viewModelScope.launch {
            uploadAuctionDataUseCase(_selectedImagesFlow.value, _auctionDataFlow.value).let { result ->
                _goBackEvent.update { result.toState() }
            }
        }
    }

    fun addUri(uri: String) {
        viewModelScope.launch {
            _selectedImagesFlow.update {
                it.toMutableList().apply { add(uri) }
            }
        }
    }

    fun addAllUris(uris: List<String>) {
        viewModelScope.launch {
            _selectedImagesFlow.update { uris }
        }
    }

    fun removeUri(uri: String) {
        viewModelScope.launch {
            _selectedImagesFlow.update {
                it.toMutableList().apply { remove(uri) }
            }
        }
    }

    fun clearUris() {
        viewModelScope.launch {
            _selectedImagesFlow.update { emptyList() }
        }
    }

    fun setCarName(carName: CarName?) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(brand = carName?.brand, model = carName?.model)
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setManufacturerDate(manufacturerDate: Int?) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(manufacturerDate = manufacturerDate)
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setMileage(mileage: Int?) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(mileage = mileage)
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setGearbox(gearbox: GearboxUI?) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(gearbox = gearbox?.toGearbox())
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setColor(color: ColorUI?) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(color = color?.toColor())
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setDriveWheel(driveWheel: DriveWheelUI?) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(driveWheel = driveWheel?.toDriveWheel())
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setHorsepower(horsepower: Int?) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(horsepower = horsepower)
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setVin(vin: String) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(vin = vin)
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setDescription(description: String) = viewModelScope.launch {
        val car = _auctionDataFlow.value.car.copy(description = description)
        _auctionDataFlow.update { it.copy(car = car) }
    }

    fun setMinBid(minBid: Int) = viewModelScope.launch {
        _auctionDataFlow.update { it.copy(minBid = minBid) }
    }

    fun setAuctionDate(openDate: String, closeDate: String) = viewModelScope.launch {
        _auctionDataFlow.update { it.copy(openDate = openDate, closeDate = closeDate) }
    }
}

class NewLotViewModelFactory @Inject constructor(
    private val uploadAuctionDataUseCase: UploadAuctionDataUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewLotViewModel::class.java)) {
            return NewLotViewModel(uploadAuctionDataUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}