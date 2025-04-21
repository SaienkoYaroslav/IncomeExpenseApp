package com.example.incomeexpenseapp.ui.screens.main

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity
import com.example.incomeexpenseapp.ui.components.BaseAlertDialog
import com.example.incomeexpenseapp.ui.components.MotionToolBar
import com.example.incomeexpenseapp.ui.theme.Black
import com.example.incomeexpenseapp.ui.theme.DarkGray
import com.example.incomeexpenseapp.utils.formatNumber
import com.example.incomeexpenseapp.utils.stringToColor
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import io.groovin.collapsingtoolbar.CollapsingOption
import io.groovin.collapsingtoolbar.CollapsingToolBarLayout
import io.groovin.collapsingtoolbar.rememberCollapsingToolBarState
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {

    val context = LocalContext.current
    val viewModel = hiltViewModel<MainVM>()
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val message by viewModel.messageEvent.collectAsStateWithLifecycle("")

    var categoryType by rememberSaveable { mutableStateOf(getString(context, R.string.income)) }

    when (state) {
        is ScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        is ScreenState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black),
                contentAlignment = Alignment.Center
            ) {
                Text(text = (state as ScreenState.Error).error, color = Color.White)
            }
        }

        is ScreenState.Success -> {
            val successState = state as ScreenState.Success
            val transactions = successState.transactions

            val filteredTransactions = remember(transactions, categoryType) {
                transactions.filter { it.type == categoryType }
            }

            val totalSum = remember(filteredTransactions) {
                filteredTransactions.sumOf { it.sum.toLong() }
            }

            MainContent(
                filteredTransactions = filteredTransactions,
                totalSum = totalSum,
                message = message,
                categoryType = categoryType,
                onCategoryTypeChange = { categoryType = it },
                onDeleteTx = { viewModel.deleteTx(it) },
                onMessageShown = { viewModel.onMessageShown() }
            )
        }
    }

}


@Composable
fun MainContent(
    filteredTransactions: List<TransactionEntity>,
    totalSum: Long,
    message: String,
    categoryType: String,
    onCategoryTypeChange: (String) -> Unit,
    onDeleteTx: (TransactionEntity) -> Unit,
    onMessageShown: () -> Unit
) {

    val hazeState = remember { HazeState() }

    val systemBottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
    val systemTopPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()

    val config = LocalConfiguration.current
    val screenHeightDp = config.screenHeightDp.dp
    val toolBarHeight by remember { mutableStateOf(screenHeightDp / 3) }

    val snackBarHostState = remember { SnackbarHostState() }

    val collapsingToolBarState = rememberCollapsingToolBarState(
        toolBarMaxHeight = toolBarHeight,
        toolBarMinHeight = systemTopPadding + 66.dp,
        collapsingOption = CollapsingOption.EnterAlwaysCollapsedAutoSnap
    )

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            snackBarHostState.showSnackbar(
                message = message
            )
            onMessageShown()
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
                CollapsingToolBarLayout(
                    modifier = Modifier,
                    state = collapsingToolBarState,
                    toolbar = {
                        MotionToolBar(
                            modifier = Modifier
                                .height(toolBarHeight)
                                .toolBarScrollable(),
                            progress = collapsedInfo.progress,
                            systemTopPadding = systemTopPadding,
                            totalSum = totalSum,
                            categoryType = categoryType,
                            onCategoryTypeChange = onCategoryTypeChange
                        )
                    }
                ) {
                    Spacer(Modifier.height(5.dp))
                    LazyColumn(
                        modifier = Modifier
                            .haze(
                                state = hazeState,
                                style = HazeDefaults.style(
                                    backgroundColor = Black.copy(0.8f),
                                    blurRadius = 10.dp,
                                    noiseFactor = 0f
                                )
                            )
                    ) {
                        if (filteredTransactions.isNotEmpty()) {
                            items(filteredTransactions) { transactionItem ->
                                TransItem(
                                    item = transactionItem,
                                    onDeleteTx = { deleteItem ->
                                        onDeleteTx(deleteItem)
                                    }
                                )
                            }
                            item {
                                Spacer(Modifier.height(systemBottomPadding + 70.dp))
                            }
                        } else {
                            item {
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
                    }
                }


            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Black.copy(alpha = 0.85f),
                                    Black
                                )
                            )
                        )
                )
            }

        }
    }
}

@Composable
fun TransItem(
    modifier: Modifier = Modifier,
    item: TransactionEntity,
    onDeleteTx: (TransactionEntity) -> Unit
) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }
    var openDeleteDialog by remember { mutableStateOf(false) }
    val sign =
        if (item.type == stringResource(R.string.income)) stringResource(R.string.plus) else stringResource(
            R.string.minus
        )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(DarkGray),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(10.dp)
                .background(stringToColor(item.color))
        )
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = item.categoryName,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = item.date,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = "$sign ${formatNumber(item.sum.toLong())}${stringResource(id = R.string.dollar)}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 20.sp
        )
        Spacer(Modifier.width(10.dp))
        IconButton(
            modifier = Modifier,
            onClick = {
                expanded = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = Color.White
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Color.White
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.delete),
                            color = Color.Red
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    },
                    onClick = {
                        expanded = false
                        openDeleteDialog = true
                    }
                )
            }
        }
    }
    if (openDeleteDialog) {
        BaseAlertDialog(
            onDismissRequest = {
                openDeleteDialog = false
            },
            confirmButtonClick = {
                openDeleteDialog = false
                scope.launch {
                    onDeleteTx(item)
                }
            }
        )
    }
}