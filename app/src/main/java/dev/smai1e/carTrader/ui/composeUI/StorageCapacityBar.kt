package dev.smai1e.carTrader.ui.composeUI

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.domain.models.Memory
import dev.smai1e.carTrader.domain.models.StorageSpaceState
import dev.smai1e.carTrader.ui.models.TimeProgressState
import java.time.Duration

private class StorageSpaceStateProvider : PreviewParameterProvider<StorageSpaceState> {
    override val values = sequenceOf(
        StorageSpaceState(
            occupiedSpace = Memory(75L),
            totalSpace = Memory(100L),
            cache = Memory(10L)
        )
    )
}

@Preview(
    showBackground = true,
    widthDp = 300,
    heightDp = 240
)
@Composable
fun StorageSpaceBarPreview(
    @PreviewParameter(StorageSpaceStateProvider::class) state: StorageSpaceState
) {
    StorageSpaceBar(state)
}

@Composable
fun StorageSpaceBar(state: StorageSpaceState) {
    val indicatorValue = getPercent(state)
    val occupiedSpace = state.occupiedSpace
    val totalSpace = state.totalSpace

    var allowedIndicatorValue by remember { mutableStateOf(MAX_INDICATOR_VALUE) }
    allowedIndicatorValue = if (indicatorValue <= MAX_INDICATOR_VALUE) {
        indicatorValue
    } else {
        MAX_INDICATOR_VALUE
    }

    var animatedFirstValue by remember { mutableStateOf(0f) }
    var animatedSecondValue by remember { mutableStateOf(0f) }
    var animatedIndicatorValue by remember { mutableStateOf(0f) }

    LaunchedEffect(key1 = occupiedSpace.value) {
        animatedFirstValue = occupiedSpace.value
    }
    LaunchedEffect(key1 = totalSpace.value) {
        animatedSecondValue = totalSpace.value
    }
    LaunchedEffect(key1 = allowedIndicatorValue) {
        animatedIndicatorValue = allowedIndicatorValue.toFloat()
    }

    val percentage = (animatedIndicatorValue / MAX_INDICATOR_VALUE) * 100

    val sweetAngle by animateFloatAsState(
        targetValue = (3.6 * percentage).toFloat(),
        animationSpec = tween(ANIMATE_DURATION),
        label = stringResource(R.string.empty)
    )
    val firstReceivedValue by animateFloatAsState(
        targetValue = animatedFirstValue,
        animationSpec = tween(ANIMATE_DURATION),
        label = stringResource(R.string.empty)
    )
    val secondReceivedValue by animateFloatAsState(
        targetValue = animatedSecondValue,
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProgressBar(sweetAngle)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EmbeddedElement(
                firstValue = firstReceivedValue,
                secondValue = secondReceivedValue,
                firstMeasureUnit = occupiedSpace.measure.toString(),
                secondMeasureUnit = totalSpace.measure.toString(),
                textColor = animatedBigTextColor
            )
        }
    }
}

@Composable
fun ProgressBar(
    sweetAngle: Float
) {
    Box(
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
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(iconSize),
            painter = painterResource(R.drawable.ic_storage),
            contentDescription = null
        )
    }
}

@Composable
fun EmbeddedElement(
    firstValue: Float,
    secondValue: Float,
    firstMeasureUnit: String,
    secondMeasureUnit: String,
    textColor: Color,
) {

    Text(
        text = stringResource(
            R.string.occupied_space,
            String.format("%.1f", firstValue).plus(firstMeasureUnit)
        ),
        color = textColor,
        fontSize = fontSizeValue,
        textAlign = textAlignValue
    )
    Text(
        text = stringResource(
            R.string.total_space,
            String.format("%.1f", secondValue).plus(secondMeasureUnit)
        ),
        color = textColor,
        fontSize = fontSizeValue,
        textAlign = textAlignValue
    )
}

private fun getPercent(state: StorageSpaceState): Int {
    val percent =
        state.occupiedSpace.byteCount.toFloat() / state.totalSpace.byteCount.toFloat() * 100
    return percent.toInt()
}

private const val COLOR_1_CODE = 0xFF2662F4
private const val COLOR_2_CODE = 0xFF000000
private const val MAX_INDICATOR_VALUE: Int = 100
private const val BACKGROUND_INDICATOR_STROKE_WIDTH: Float = 60f
private const val FOREGROUND_INDICATOR_STROKE_WIDTH: Float = 60f
private const val ANIMATE_DURATION = 1000

private val foregroundIndicatorColor: Color = Color(COLOR_1_CODE)
private val backgroundIndicatorColor: Color = Color(COLOR_2_CODE).copy(alpha = 0.1f)
private val bigTextColor: Color = Color(COLOR_2_CODE)
private val canvasSize: Dp = 200.dp
private val iconSize = canvasSize / 2
private val fontSizeValue: TextUnit = 16.sp
private val textAlignValue: TextAlign = TextAlign.Center