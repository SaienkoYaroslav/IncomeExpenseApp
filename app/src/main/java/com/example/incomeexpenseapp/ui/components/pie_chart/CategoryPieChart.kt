package com.example.incomeexpenseapp.ui.components.pie_chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.data.db.CategoryWithTransactions
import com.example.incomeexpenseapp.utils.PieSliceData
import com.example.incomeexpenseapp.utils.mapCategoriesToSlices
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CategoryPieChart(
    modifier: Modifier = Modifier,
    categoriesWithTransactions: List<CategoryWithTransactions>
) {

    val context = LocalContext.current

    val slices = remember(categoriesWithTransactions) {
        mapCategoriesToSlices(categoriesWithTransactions)
    }

    val grandTotal = slices.sumOf { it.totalSum }

    val config = LocalConfiguration.current
    val screenHeightDp = config.screenHeightDp.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (slices.isNotEmpty()) {

            PieChart(
                modifier = Modifier.size(screenHeightDp / 3),
                data = slices,
                totalSum = grandTotal
            )
        } else {

            Canvas(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(screenHeightDp / 3)
            ) {
                drawCircle(color = Color.Gray)
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 34f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                    val text = getString(context, R.string.no_data_available)
                    drawText(
                        text,
                        center.x,
                        center.y,
                        paint
                    )
                }
            }
        }
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    data: List<PieSliceData>,
    totalSum: Long
) {
    val context = LocalContext.current

    Canvas(modifier = modifier) {
        var startAngle = 0f

        data.forEach { slice ->
            val sliceValue = slice.totalSum.toFloat()
            val sweepAngle = if (totalSum > 0) {
                (sliceValue / totalSum) * 360f
            } else 0f

            drawArc(
                color = slice.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )

            if (sweepAngle > 0f) {
                val percentage = sliceValue / totalSum * 100f
                val halfAngle = startAngle + sweepAngle / 2f
                val halfAngleRad = Math.toRadians(halfAngle.toDouble())
                val radius = size.minDimension / 2f
                val textRadius = radius * 0.6f

                val centerOffsetX = center.x + (textRadius * cos(halfAngleRad)).toFloat()
                val centerOffsetY = center.y + (textRadius * sin(halfAngleRad)).toFloat()

                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 34f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                    }
                    val textToDraw = String.format(getString(context, R.string.string_format_percent), percentage)
                    drawText(textToDraw, centerOffsetX, centerOffsetY, paint)
                }
            }
            startAngle += sweepAngle
        }
    }
}
