package dev.smai1e.carTrader.ui.views.carPicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.smai1e.carTrader.domain.models.Brand
import dev.smai1e.carTrader.domain.models.CarName
import dev.smai1e.carTrader.domain.models.CarPickItem
import dev.smai1e.carTrader.domain.models.Model
import dev.smai1e.carTrader.domain.repositoryInterfaces.CarsRepository
import dev.smai1e.carTrader.ui.State
import dev.smai1e.carTrader.ui.toState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CarPickerViewModel(
    private val carsRepository: CarsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<State<List<CarPickItem>>>(State.Idle)
    val uiState: StateFlow<State<List<CarPickItem>>> = _uiState.asStateFlow()

    private val _goBackEvent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val goBackEvent: StateFlow<Boolean> = _goBackEvent.asStateFlow()

    private var _carName: CarName = CarName()

    init {
        getBrands()
    }

    fun getBrands() {
        viewModelScope.launch {
            carsRepository.fetchBrands().let { result ->
                _uiState.update { result.toState() }
            }
        }
    }

    fun <T: CarPickItem> onItemPressed(item: T) {
        when (item) {
            is Brand -> onBrandPressed(item)
            is Model -> onModelPressed(item)
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    private fun onBrandPressed(brand: Brand) {
        _carName = _carName.copy(brand = brand.name)
        viewModelScope.launch {
            carsRepository.fetchModelsByBrand(brand).let { result ->
                _uiState.update { result.toState() }
            }
        }
    }

    private fun onModelPressed(model: Model) {
        _carName = _carName.copy(model = model.name)
        viewModelScope.launch {
            _goBackEvent.update { true }
        }
    }

    fun getCarName(): CarName {
        return _carName
    }
}

class CarPickerViewModelFactory @Inject constructor(
    private val carsRepository: CarsRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarPickerViewModel::class.java)) {
            return CarPickerViewModel(carsRepository) as T
        }
        throw IllegalArgumentException("Unknown viewModel")
    }
}