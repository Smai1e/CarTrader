package dev.smai1e.carTrader.domain.repositoryInterfaces

import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.models.Brand
import dev.smai1e.carTrader.domain.models.Model

interface CarsRepository {

    /**
     * Returns popular car brands as a [RequestResult].
     */
    suspend fun fetchPopularBrands(): RequestResult<List<Brand>, DataError>

    /**
     * Returns all car brands as a [RequestResult].
     */
    suspend fun fetchBrands(): RequestResult<List<Brand>, DataError>

    /**
     * Returns car models that match the specified [Brand]
     * as a [RequestResult].
     */
    suspend fun fetchModelsByBrand(brand: Brand): RequestResult<List<Model>, DataError>
}