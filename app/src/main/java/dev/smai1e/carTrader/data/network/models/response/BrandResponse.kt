package dev.smai1e.carTrader.data.network.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Brand entity for responding from API.
 */
@Serializable
data class BrandResponse(

    @SerialName("name")
    val name: String,

    @SerialName("logo_url")
    val logoUrl: String
)
