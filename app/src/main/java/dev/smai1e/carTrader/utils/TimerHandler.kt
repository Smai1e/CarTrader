package dev.smai1e.carTrader.utils

import android.os.CountDownTimer
import dev.smai1e.carTrader.ui.models.TimeProgressState
import dev.smai1e.carTrader.di.DefaultDispatcher
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.errorTypes.TimerException
import dev.smai1e.carTrader.ui.models.AuctionUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

class TimerHandler(
    private val externalScope: CoroutineScope,
    private val auction: AuctionUI,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    private val _flow = MutableSharedFlow<RequestResult<TimeProgressState, TimerException>>()
    val flow: SharedFlow<RequestResult<TimeProgressState, TimerException>> = _flow.asSharedFlow()

    private val openDate = auction.openDate.parseToZonedDateTime()
    private var closeDate = auction.closeDate.parseToZonedDateTime()
    private val auctionDuration = Duration.between(openDate, closeDate)
    private var currentDate = ZonedDateTime.now(ZoneId.systemDefault())
    private val timeMs = Duration.between(
        currentDate,
        closeDate
    ).seconds * 1000

    private val timer = object : CountDownTimer(timeMs, TICK_INTERVAL_MS) {
        override fun onTick(p0: Long) { tick() }
        override fun onFinish() {}
    }

    init {
        try {
            timer.start()
        } catch (e: Exception) {
            timer.cancel()
            externalScope.launch {
                RequestResult.Error<TimeProgressState, TimerException>(
                    TimerException(e)
                ).let {
                    _flow.emit(it)
                }
            }
        }
    }

    private fun tick() {
        externalScope.launch(defaultDispatcher) {
            closeDate = auction.closeDate.parseToZonedDateTime()
            currentDate = ZonedDateTime.now(ZoneId.systemDefault())
            val timeLeft = Duration.between(
                currentDate,
                closeDate
            )
            val isActive = currentDate.isAfter(openDate) && currentDate.isBefore(closeDate)

            RequestResult.Success<TimeProgressState, TimerException>(
                TimeProgressState(auctionDuration, timeLeft, isActive)
            ).let {
                _flow.emit(it)
            }
        }
    }

    private companion object {
        const val TICK_INTERVAL_MS = 1000L
    }
}