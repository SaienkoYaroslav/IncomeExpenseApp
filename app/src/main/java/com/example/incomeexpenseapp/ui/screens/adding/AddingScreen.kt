package com.example.incomeexpenseapp.ui.screens.adding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import com.example.incomeexpenseapp.ui.components.BannerAdMob
import com.example.incomeexpenseapp.ui.components.BaseButton
import com.example.incomeexpenseapp.ui.components.BaseTextField
import com.example.incomeexpenseapp.ui.components.CalendarPicker
import com.example.incomeexpenseapp.ui.components.CategoryDropdown
import com.example.incomeexpenseapp.ui.components.ColorPickerDialog
import com.example.incomeexpenseapp.ui.components.Toggle
import com.example.incomeexpenseapp.ui.theme.Black
import com.example.incomeexpenseapp.ui.theme.Blue
import com.example.incomeexpenseapp.utils.updateText
import kotlinx.coroutines.launch

@Composable
fun AddingScreen(
    viewModel: AddingCategoryVM,
    viewModelTrans: AddingTransactionVM
) {

    val context = LocalContext.current
    val systemBottomPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
    val systemTopPadding = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()
    val snackBarHostState = remember { SnackbarHostState() }
    val messageState = viewModel.messageFlow.collectAsStateWithLifecycle()

    val tabTitles = listOf(getString(context, R.string.add_category), getString(context, R.string.add_transaction))

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Black,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color.White
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = systemTopPadding)
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                modifier = Modifier.weight(1f),
                state = pagerState
            ) { pageIndex ->
                when (pageIndex) {
                    0 -> {
                        AddCategoryContent(
                            viewModel = viewModel
                        )
                    }

                    1 -> {
                        AddTransactionContent(
                            viewModel = viewModelTrans,
                            msgVM = viewModel
                        )
                    }
                }
            }
            BannerAdMob()

        }
    }
}

@Composable
fun AddCategoryContent(
    modifier: Modifier = Modifier,
    viewModel: AddingCategoryVM
) {

    val context = LocalContext.current
    var categoryType by remember { mutableStateOf(getString(context, R.string.income)) }
    val categoryError by viewModel.categoryErrorFlow.collectAsStateWithLifecycle()
    val selectedColor by viewModel.flowSelectedColor.collectAsStateWithLifecycle()

    var showColorPickerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Blue, RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Toggle(
                options = listOf(stringResource(R.string.income), stringResource(R.string.expense)),
                selectedOption = categoryType,
                onOptionSelected = { categoryType = it }
            )
            Spacer(Modifier.padding(5.dp))
            BaseTextField(
                modifier = Modifier,
                label = stringResource(R.string.category_name),
                stateTextFlow = viewModel.categoryFlow,
                isError = categoryError,
                errorMessage = stringResource(R.string.field_cant_be_empty)
            ) { viewModel.updateText(it, viewModel.categoryFlow) }
            Spacer(Modifier.padding(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(selectedColor, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        showColorPickerDialog = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.choose_color),
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(Modifier.padding(5.dp))
        }
        Spacer(Modifier.height(10.dp))
        BaseButton(
            modifier = Modifier.fillMaxWidth(0.9f),
            text = stringResource(R.string.add)
        ) {
            if (selectedColor != Black && selectedColor != Color.White) {
                viewModel.validateAndInsertCategory(
                    type = categoryType,
                    onError = {
                        viewModel.showMessage(getString(context, R.string.field_cant_be_empty))
                    },
                    onSuccess = {
                        viewModel.showMessage(getString(context, R.string.category_added))
                    }
                )
            } else {
                viewModel.showMessage(getString(context, R.string.select_color))
            }
        }
    }

    if (showColorPickerDialog) {
        ColorPickerDialog(
            onDismiss = { showColorPickerDialog = false },
            selectedColor = selectedColor,
            onSelectColor = { viewModel.flowSelectedColor.value = it }
        )
    }

}

@Composable
fun AddTransactionContent(
    modifier: Modifier = Modifier,
    viewModel: AddingTransactionVM,
    msgVM: AddingCategoryVM
) {

    val context = LocalContext.current

    val categories by viewModel.categories.collectAsStateWithLifecycle(emptyList())
    val sumError by viewModel.sumErrorFlow.collectAsStateWithLifecycle()
    var categoryType by remember { mutableStateOf(getString(context, R.string.income)) }
    val filteredCategories by remember(categoryType, categories) {
        mutableStateOf(
            categories.filter { it.type == categoryType }
        )
    }
    var selectedCategory by remember(filteredCategories) {
        mutableStateOf(
            filteredCategories.firstOrNull() ?: CategoryEntity(-1, getString(context, R.string.no_categories_yet), "", "")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Blue, RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Toggle(
                options = listOf(stringResource(R.string.income), stringResource(R.string.expense)),
                selectedOption = categoryType,
                onOptionSelected = { categoryType = it }
            )
            Spacer(Modifier.height(10.dp))
            CategoryDropdown(
                categories = filteredCategories,
                selectedCategory = selectedCategory,
                viewModel = viewModel,
                onCategorySelected = { selectedCategory = it }
            )
            Spacer(Modifier.height(10.dp))
            BaseTextField(
                modifier = Modifier,
                label = getString(context, R.string.sum),
                maxChar = 7,
                keyboardType = KeyboardType.Number,
                stateTextFlow = viewModel.sumFlow,
                isError = sumError,
                errorMessage = getString(context, R.string.field_cant_be_empty)
            ) { viewModel.updateText(it, viewModel.sumFlow) }
            Spacer(Modifier.height(10.dp))
            CalendarPicker(dateFlow = viewModel.dateFlow)
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.height(10.dp))
        BaseButton(
            modifier = Modifier.fillMaxWidth(0.9f),
            text = stringResource(R.string.add)
        ) {
            viewModel.validateAndInsertTransaction(
                category = selectedCategory,
                onError = {
                    msgVM.showMessage(getString(context, R.string.field_cant_be_empty))
                },
                onSuccess = {
                    msgVM.showMessage(getString(context, R.string.transaction_added))
                }
            )
        }
    }

}