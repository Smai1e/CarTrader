package dev.smai1e.carTrader.data.network.api

import dev.smai1e.carTrader.data.network.NetworkResult
import dev.smai1e.carTrader.data.network.api.BidsSocketService.Companion.WEBSOCKET_ERROR_CONNECTION_MESSAGE
import dev.smai1e.carTrader.data.network.models.response.BidResponse
import dev.smai1e.carTrader.di.IoDispatcher
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Implementation a [BidsSocketService] which uses
 * [HttpClient] as data source.
 */
class BidsSocketServiceImpl @Inject constructor(
    private val client: HttpClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BidsSocketService {

    private var socket: WebSocketSession? = null

    /**
     * Connect to websocket.
     */
    override suspend fun initSession(auctionId: Long): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            return@withContext try {
                socket = client.webSocketSession {
                    url("${BidsSocketService.Endpoints.BidsSocket.url}?auction_id=$auctionId")
                }
                if (socket?.isActive == true) {
                    NetworkResult.ApiSuccess(Unit)
                } else {
                    NetworkResult.ApiError(code = 401, message = WEBSOCKET_ERROR_CONNECTION_MESSAGE)
                }
            } catch (e: Exception) {
                 NetworkResult.ApiException(e)
            }
        }
    }

    /**
     * Get stream of bids.
     */
    override fun observeBids(): Flow<BidResponse> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val receivedText = (it as? Frame.Text)?.readText()!!
                    Json.decodeFromString<BidResponse>(receivedText)
                }
                ?.flowOn(ioDispatcher) ?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }

    /**
     * Stop receiving bids.
     */
    override suspend fun closeSession() {
        withContext(ioDispatcher) {
            socket?.close()
        }
    }
}