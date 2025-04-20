package com.example.incomeexpenseapp.ui.components.pie_chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.data.db.CategoryWithTransactions
import com.example.incomeexpenseapp.ui.theme.DarkGray
import com.example.incomeexpenseapp.utils.PieSliceData
import com.example.incomeexpenseapp.utils.formatNumber
import com.example.incomeexpenseapp.utils.mapCategoriesToSlices

@Composable
fun CategoryLegendGrid(
    modifier: Modifier = Modifier,
    categoriesWithTransactions: List<CategoryWithTransactions>
) {
    val systemBottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
    val slices = remember(categoriesWithTransactions) {
        mapCategoriesToSlices(categoriesWithTransactions)
    }
    val totalSum = slices.sumOf { it.totalSum }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(slices) { slice ->
            LegendItem(slice = slice, totalSum = totalSum)
        }
        item {
            Spacer(Modifier.height(systemBottomPadding + 60.dp))
        }
        item {
            Spacer(Modifier.height(systemBottomPadding + 60.dp))
        }
    }
}

@Composable
fun LegendItem(slice: PieSliceData, totalSum: Long) {
    val percentage = if (totalSum > 0) {
        (slice.totalSum.toFloat() / totalSum) * 100f
    } else 0f

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkGray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(15.dp)
                    .background(slice.color)
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = slice.categoryName,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = String.format(stringResource(R.string.string_format_percent), percentage),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "${formatNumber(slice.totalSum)} ${stringResource(R.string.dollar)}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
