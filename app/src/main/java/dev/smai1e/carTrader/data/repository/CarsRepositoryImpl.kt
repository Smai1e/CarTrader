package dev.smai1e.carTrader.data.repository

import dev.smai1e.carTrader.data.network.api.CarsService
import dev.smai1e.carTrader.data.toBrand
import dev.smai1e.carTrader.di.IoDispatcher
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.Brand
import dev.smai1e.carTrader.data.map
import dev.smai1e.carTrader.data.toModel
import dev.smai1e.carTrader.domain.repositoryInterfaces.CarsRepository
import dev.smai1e.carTrader.data.toRequestResult
import dev.smai1e.carTrader.domain.models.Model
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Implementation a [CarsRepository]
 * which uses [CarsService] as data source.
 */
class CarsRepositoryImpl @Inject constructor(
    private val carsService: CarsService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CarsRepository {

    /**
     * Returns popular car brands as a [RequestResult].
     */
    override suspend fun fetchPopularBrands(): RequestResult<List<Brand>, DataError> {
        return carsService.fetchPopularBrands()
            .toRequestResult()
            .map { dtos ->
                dtos.map { it.toBrand() }
            }
    }

    /**
     * Returns all car brands as a [RequestResult].
     */
    override suspend fun fetchBrands(): RequestResult<List<Brand>, DataError> {
        return carsService.fetchBrands()
            .toRequestResult()
            .map { dtos ->
                dtos.map { it.toBrand() }
            }
    }

    /**
     * Returns car models that match the specified [Brand]
     * as a [RequestResult].
     */
    override suspend fun fetchModelsByBrand(brand: Brand): RequestResult<List<Model>, DataError> {
        return carsService.fetchModelsByBrand(brand.name)
            .toRequestResult()
            .map { dtos ->
                dtos.map { it.toModel() }
            }
    }
}