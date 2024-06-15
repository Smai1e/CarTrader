package dev.smai1e.carTrader.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Model entity for responding from API.
 */
@Serializable
data class ModelResponse(
    @SerialName("name")
    val name: String
)