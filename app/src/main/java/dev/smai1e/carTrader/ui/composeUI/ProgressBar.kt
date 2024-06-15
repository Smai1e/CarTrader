package dev.smai1e.carTrader.ui.composeUI

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.ui.models.TimeProgressState
import dev.smai1e.carTrader.utils.day
import dev.smai1e.carTrader.utils.hour
import dev.smai1e.carTrader.utils.minute
import dev.smai1e.carTrader.utils.second
import java.time.Duration

private class TimeStateProvider : PreviewParameterProvider<TimeProgressState> {
    override val values = sequenceOf(
        TimeProgressState(
            Duration.ofDays(30),
            Duration.ofHours(220),
            true
        )
    )
}

@Preview(
    showBackground = true,
    heightDp = 160,
    widthDp = 160
)
@Composable
fun ProgresBarPreview(
    @PreviewParameter(TimeStateProvider::class) timeState: TimeProgressState
) {
    CircleProgressBar(timeState)
}


@Composable
fun CircleProgressBar(
    timeState: TimeProgressState = TimeProgressState(
        Duration.ZERO,
        Duration.ZERO,
        false
    )
) {

    val progress = getProgress(timeState)
    val firstTitle = progress.firstTitle
    val firstValue = progress.firstValue
    val secondTitle = progress.secondTitle
    val secondValue = progress.secondValue
    val indicatorValue = progress.progressValue

    var allowedIndicatorValue by remember { mutableStateOf(MAX_INDICATOR_VALUE) }
    allowedIndicatorValue = if (indicatorValue <= MAX_INDICATOR_VALUE) {
        indicatorValue
    } else {
        MAX_INDICATOR_VALUE
    }

    var animatedFirstValue by remember { mutableStateOf(0f) }
    var animatedSecondValue by remember { mutableStateOf(0f) }

    var animatedIndicatorValue by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }
    LaunchedEffect(key1 = firstValue) {
        animatedFirstValue = firstValue.toFloat()
    }
    LaunchedEffect(key1 = secondValue) {
        animatedSecondValue = secondValue.toFloat()
    }

    val percentage = (animatedIndicatorValue / MAX_INDICATOR_VALUE) * 100

    val sweetAngle by animateFloatAsState(
        targetValue = (3.6 * percentage).toFloat(),
        animationSpec = tween(ANIMATE_DURATION),
        label = stringResource(R.string.empty)
    )

    val firstReceivedValue by animateIntAsState(
        targetValue = animatedFirstValue.toInt(),
        animationSpec = tween(ANIMATE_DURATION),
        label = stringResource(R.string.empty)
    )
    val secondReceivedValue by animateIntAsState(
        targetValue = animatedSecondValue.toInt(),
        animationSpec = tween(ANIMATE_DURATION),
        label = stringResource(R.string.empty)
    )

    val animatedBigTextColor by animateColorAsState(
        targetValue = if (allowedIndicatorValue == 0)
            MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
        else
            bigTextColor,
        animationSpec = tween(ANIMATE_DURATION),
        label = stringResource(R.string.empty)
    )

    Column(
        modifier = Modifier
            .size(canvasSize)
            .drawBehind {
                val componentSize = size / 1.1f

                backgroundIndicator(
                    componentSize = componentSize,
                    indicatorColor = backgroundIndicatorColor,
                    indicatorStrokeWidth = BACKGROUND_INDICATOR_STROKE_WIDTH
                )
                foregroundIndicator(
                    sweepAngle = sweetAngle,
                    componentSize = componentSize,
                    indicatorColor = foregroundIndicatorColor,
                    indicatorStrokeWidth = FOREGROUND_INDICATOR_STROKE_WIDTH
                )
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmbeddedElements(
            firstTitle = firstTitle,
            firstValue = firstReceivedValue,
            secondTitle = secondTitle,
            secondValue = secondReceivedValue,
            textColor = animatedBigTextColor
        )
    }
}

fun DrawScope.backgroundIndicator(
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
) {
    drawArc(
        size = componentSize,
        color = indicatorColor,
        startAngle = 150f,
        sweepAngle = 360f,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

fun DrawScope.foregroundIndicator(
    sweepAngle: Float,
    componentSize: Size,
    indicatorColor: Color,
    indicatorStrokeWidth: Float
) {
    drawArc(
        size = componentSize,
        brush = Brush.radialGradient(listOf(backgroundIndicatorColor, indicatorColor)),
        startAngle = 270f,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(
            width = indicatorStrokeWidth,
            cap = StrokeCap.Round
        ),
        topLeft = Offset(
            x = (size.width - componentSize.width) / 2f,
            y = (size.height - componentSize.height) / 2f
        )
    )
}

@Composable
fun EmbeddedElements(
    firstTitle: String,
    firstValue: Int,
    secondTitle: String,
    secondValue: Int,
    textColor: Color,
) {

    Row {
        Text(
            text = firstTitle,
            color = textColor,
            fontSize = fontSizeValue,
            textAlign = textAlignValue,
            fontWeight = fontWeightValue
        )
        Text(
            text = firstValue.toString(),
            color = textColor,
            fontSize = fontSizeValue,
            textAlign = textAlignValue,
            fontWeight = fontWeightValue
        )
    }
    Row {
        Text(
            text = secondTitle,
            color = textColor,
            fontSize = fontSizeValue,
            textAlign = textAlignValue,
            fontWeight = fontWeightValue
        )
        Text(
            text = secondValue.toString(),
            color = textColor,
            fontSize = fontSizeValue,
            textAlign = textAlignValue,
            fontWeight = fontWeightValue
        )
    }
}

data class ProgressBarState(
    val firstTitle: String,
    val firstValue: Int,
    val secondTitle: String,
    val secondValue: Int,
    val progressValue: Int
)

private fun getPercent(timeLeft: Duration, auctionDuration: Duration): Int {
    val percent = timeLeft.seconds.toDouble() / auctionDuration.seconds.toDouble() * 100
    return percent.toInt()
}

@Composable
private fun getProgress(
    state: TimeProgressState
): ProgressBarState {

    if (state.timeLeft.seconds <= 0)
        return ProgressBarState(
            firstTitle = stringResource(R.string.progress_bar_minutes),
            firstValue = 0,
            secondTitle = stringResource(R.string.progress_bar_seconds),
            secondValue = 0,
            progressValue = 0
        )

    val day = state.timeLeft.day()
    val hour = state.timeLeft.hour()
    val minute = state.timeLeft.minute()
    val second = state.timeLeft.second()

    val indicatorValue =
        if (state.isActive) getPercent(state.timeLeft, state.auctionDuration) else 0

    return if (day > 0) {
        ProgressBarState(
            firstTitle = stringResource(R.string.progress_bar_days),
            firstValue = day.toInt(),
            secondTitle = stringResource(R.string.progress_bar_hours),
            secondValue = hour.toInt(),
            progressValue = indicatorValue
        )
    } else if (hour > 0) {
        ProgressBarState(
            firstTitle = stringResource(R.string.progress_bar_hours),
            firstValue = hour.toInt(),
            secondTitle = stringResource(R.string.progress_bar_minutes),
            secondValue = minute.toInt(),
            progressValue = indicatorValue
        )
    } else {
        ProgressBarState(
            firstTitle = stringResource(R.string.progress_bar_minutes),
            firstValue = minute.toInt(),
            secondTitle = stringResource(R.string.progress_bar_seconds),
            secondValue = second.toInt(),
            progressValue = indicatorValue
        )
    }
}

private const val COLOR_1_CODE = 0xFF2662F4
private const val COLOR_2_CODE = 0xFF000000
private const val MAX_INDICATOR_VALUE: Int = 100
private const val BACKGROUND_INDICATOR_STROKE_WIDTH: Float = 50f
private const val FOREGROUND_INDICATOR_STROKE_WIDTH: Float = 50f
private const val ANIMATE_DURATION = 1000

private val foregroundIndicatorColor: Color = Color(COLOR_1_CODE)
private val backgroundIndicatorColor: Color = Color(COLOR_2_CODE).copy(alpha = 0.1f)
private val bigTextColor: Color = Color(COLOR_2_CODE)
private val canvasSize: Dp = 130.dp
private val fontSizeValue: TextUnit = 18.sp
private val textAlignValue: TextAlign = TextAlign.Center
private val fontWeightValue: FontWeight = FontWeight.Bold