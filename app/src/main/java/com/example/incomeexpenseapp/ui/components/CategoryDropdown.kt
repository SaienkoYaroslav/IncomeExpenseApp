package com.example.incomeexpenseapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import com.example.incomeexpenseapp.ui.screens.adding.AddingTransactionVM
import kotlinx.coroutines.launch

@Composable
fun CategoryDropdown(
    modifier: Modifier = Modifier,
    categories: List<CategoryEntity>,
    selectedCategory: CategoryEntity,
    viewModel: AddingTransactionVM,
    onCategorySelected: (CategoryEntity) -> Unit,
) {

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var openDeleteDialog by remember { mutableStateOf(false) }
    var deleteItem: CategoryEntity? by remember { mutableStateOf(null) }

    Box(
        modifier = modifier

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                .background(Color.White)
                .clickable { expanded = !expanded }
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCategory.category,
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier,
                tint = Color.Black
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.95f),
            containerColor = Color.White
        ) {
            categories.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.category,
                            color = Color.Black,
                            maxLines = 1
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier,
                            onClick = {
                                deleteItem = option
                                openDeleteDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    },
                    onClick = {
                        onCategorySelected(option)
                        expanded = false
                    },
                )
            }
        }
        if (openDeleteDialog) {
            BaseAlertDialog(
                text = stringResource(R.string.dialog_delete_category_message),
                onDismissRequest = {
                    openDeleteDialog = false
                },
                confirmButtonClick = {
                    openDeleteDialog = false
                    if (deleteItem != null) {
                        scope.launch {
                            viewModel.deleteCategory(deleteItem!!)
                        }
                    } else {
                        viewModel.showMessage(getString(context, R.string.something_went_wrong))
                    }

                }
            )
        }
    }
}