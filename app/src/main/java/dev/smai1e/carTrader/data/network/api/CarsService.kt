package dev.smai1e.carTrader.data.network.api

import dev.smai1e.carTrader.data.network.models.response.BrandResponse
import dev.smai1e.carTrader.data.network.models.response.ModelResponse
import dev.smai1e.carTrader.data.network.models.response.CarResponse
import dev.smai1e.carTrader.data.network.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API for fetching cars.
 */
interface CarsService {

    /**
     * Get the car that match the specified [carId].
     */
    @GET("cars/{id}")
    suspend fun fetchCarById(
        @Path("id") carId: Long
    ): NetworkResult<CarResponse>

    /**
     * Get popular car brands.
     */
    @GET("brands/popular")
    suspend fun fetchPopularBrands(): NetworkResult<List<BrandResponse>>

    /**
     * Get all car brands.
     */
    @GET("brands")
    suspend fun fetchBrands(): NetworkResult<List<BrandResponse>>

    /**
     * Get car models that match the specified brand.
     */
    @GET("{brand}/models")
    suspend fun fetchModelsByBrand(
        @Path("brand") brand: String
    ): NetworkResult<List<ModelResponse>>
}