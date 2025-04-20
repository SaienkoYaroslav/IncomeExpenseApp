package com.example.incomeexpenseapp.ui.screens.analytic

import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.ui.components.MonthYearDropdown
import com.example.incomeexpenseapp.ui.components.Toggle
import com.example.incomeexpenseapp.ui.components.pie_chart.CategoryLegendGrid
import com.example.incomeexpenseapp.ui.components.pie_chart.CategoryPieChart
import com.example.incomeexpenseapp.ui.theme.Black
import com.example.incomeexpenseapp.ui.theme.Blue
import com.example.incomeexpenseapp.utils.monthYearString
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild

@Composable
fun AnalyticScreen(
    viewModel: AnalyticVM
) {

    val context = LocalContext.current
    val hazeState = remember { HazeState() }

    val systemBottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
    val systemTopPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
    val snackBarHostState = remember { SnackbarHostState() }
    val messageState = viewModel.messageFlow.collectAsStateWithLifecycle()
    var categoryType by remember { mutableStateOf(getString(context, R.string.income)) }
    var selectedMonthYear by remember { mutableStateOf(getString(context, R.string.all)) }

    val categoriesState by viewModel.categoriesWithTransactionsState.collectAsStateWithLifecycle(
        emptyList()
    )

    val transactions by viewModel.transactions.collectAsStateWithLifecycle(initialValue = emptyList())

    val finalCatWithTxsList by remember(
        categoriesState,
        selectedMonthYear,
        categoryType
    ) {
        mutableStateOf(
            if (selectedMonthYear == getString(context, R.string.all)) {
                categoriesState.filter { it.category.type == categoryType }
            } else {
                categoriesState
                    .filter { it.category.type == categoryType }
                    .map { cwt ->
                        val filteredTransactions = cwt.transactions.filter { transaction ->
                            transaction.monthYearString() == selectedMonthYear
                        }
                        cwt.copy(transactions = filteredTransactions)
                    }
                    .filter { it.transactions.isNotEmpty() }
            }
        )
    }

    LaunchedEffect(messageState.value.first) {
        if (messageState.value.first) {
            snackBarHostState.showSnackbar(
                message = messageState.value.second
            )
            viewModel.messageFlow.value = Pair(false, "")
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = systemBottomPadding + 70.dp),
                hostState = snackBarHostState,
            )
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Blue,
                            RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                        )
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(systemTopPadding))
                    CategoryPieChart(
                        modifier = Modifier,
                        categoriesWithTransactions = finalCatWithTxsList
                    )
                    Spacer(Modifier.height(10.dp))
                    Toggle(
                        options = listOf(stringResource(R.string.income), stringResource(R.string.expense)),
                        selectedOption = categoryType,
                        onOptionSelected = { categoryType = it }
                    )
                    Spacer(Modifier.height(10.dp))
                    MonthYearDropdown(
                        modifier = Modifier,
                        list = transactions,
                        selectedMonthYear = selectedMonthYear,
                        onMonthYearSelected = { selectedMonthYear = it }
                    )

                }
                if (finalCatWithTxsList.isNotEmpty()) {
                    CategoryLegendGrid(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp)
                            .haze(
                                state = hazeState,
                                style = HazeDefaults.style(
                                    backgroundColor = Black.copy(0.8f),
                                    blurRadius = 10.dp,
                                    noiseFactor = 0f
                                )
                            ),
                        categoriesWithTransactions = finalCatWithTxsList
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = stringResource(R.string.list_is_empty),
                        color = Color.Gray,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                Box(
                    modifier = Modifier
                        .hazeChild(state = hazeState)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(systemBottomPadding + 60.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .height(systemBottomPadding + 60.dp)
                        .background(brush = Brush.verticalGradient(listOf(Black.copy(alpha = 0.85f), Black)))
                )
            }

        }
    }
}