package dev.smai1e.carTrader.ui.composeUI

import android.graphics.Typeface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.smai1e.carTrader.R
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.edges.rememberFadingEdges
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf

private class BidsAmountProvider : PreviewParameterProvider<List<Int>> {
    override val values: Sequence<List<Int>> =
        sequenceOf(listOf(30_000, 45_000, 47_000, 60_000, 65_000, 90_000, 120_000))
}

@Composable
@Preview(showBackground = true)
fun ChartPreview(
    @PreviewParameter(BidsAmountProvider::class) bids: List<Int>
) {
    Chart(bids)
}

@Composable
fun Chart(bids: List<Int>) {

    val bidsAmountList = mutableListOf<FloatEntry>()
    bidsAmountList.add(
        FloatEntry(0f, 0f)
    )
    bids.forEachIndexed { index, bid ->
        bidsAmountList.add(
            FloatEntry(index.toFloat() + 1, bid.toFloat())
        )
    }

    ProvideChartStyle(rememberChartStyle(chartColors)) {
        Chart(
            chart = lineChart(
                pointPosition = LineChart.PointPosition.Start,
                axisValuesOverrider = axisValueOverrider
            ),

            startAxis = startAxis(
                guideline = null,
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                titleComponent = textComponent(
                    color = Color.White,
                    background = shapeComponent(Shapes.pillShape, color1),
                    padding = axisTitlePadding,
                    margins = startAxisTitleMargins,
                    typeface = Typeface.MONOSPACE,
                ),
                title = stringResource(R.string.y_axis),
            ),

            bottomAxis = bottomAxis(
                titleComponent = textComponent(
                    background = shapeComponent(Shapes.pillShape, color2),
                    color = Color.White,
                    padding = axisTitlePadding,
                    margins = bottomAxisTitleMargins,
                    typeface = Typeface.MONOSPACE,
                ),
                title = stringResource(R.string.x_axis),
            ),
            marker = rememberMarker(),
            fadingEdges = rememberFadingEdges(),
            model = entryModelOf(bidsAmountList)
        )
    }
}

private const val COLOR_1_CODE = 0xFF2662F4
private const val COLOR_2_CODE = 0xFF9DB591
private const val AXIS_VALUE_OVERRIDER_Y_FRACTION = 1.2f

private val color1 = Color(COLOR_1_CODE)
private val color2 = Color(COLOR_2_CODE)
private val chartColors = listOf(color1, color2)
private val axisValueOverrider =
    AxisValuesOverrider.adaptiveYValues(yFraction = AXIS_VALUE_OVERRIDER_Y_FRACTION, round = true)
private val axisTitleHorizontalPaddingValue = 8.dp
private val axisTitleVerticalPaddingValue = 2.dp
private val axisTitlePadding =
    dimensionsOf(axisTitleHorizontalPaddingValue, axisTitleVerticalPaddingValue)
private val axisTitleMarginValue = 4.dp
private val startAxisTitleMargins = dimensionsOf(end = axisTitleMarginValue)
private val bottomAxisTitleMargins = dimensionsOf(top = axisTitleMarginValue)