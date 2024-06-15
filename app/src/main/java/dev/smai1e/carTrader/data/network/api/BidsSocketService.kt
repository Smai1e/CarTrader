package dev.smai1e.carTrader.data.network.api

import dev.smai1e.carTrader.BuildConfig
import dev.smai1e.carTrader.data.network.NetworkResult
import dev.smai1e.carTrader.data.network.models.response.BidResponse
import kotlinx.coroutines.flow.Flow


interface BidsSocketService {

    /**
     * Connect to websocket.
     */
    suspend fun initSession(auctionId: Long): NetworkResult<Unit>

    /**
     * Get stream of bids.
     */
    fun observeBids(): Flow<BidResponse>

    /**
     * Stop receiving bids.
     */
    suspend fun closeSession()

    companion object {
        const val WEBSOCKET_ERROR_CONNECTION_MESSAGE = "Couldn't establish a connection"
    }

    sealed class Endpoints(val url: String) {
        data object BidsSocket : Endpoints("${BuildConfig.WEBSOCKET_URL}/bids")
    }
}