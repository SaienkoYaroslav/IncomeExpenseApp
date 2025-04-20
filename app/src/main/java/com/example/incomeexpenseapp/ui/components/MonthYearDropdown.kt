package com.example.incomeexpenseapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.distinct
import kotlin.collections.forEach
import kotlin.collections.mapNotNull
import kotlin.collections.sorted
import kotlin.text.replaceFirstChar
import kotlin.text.split
import kotlin.text.toIntOrNull
import kotlin.text.uppercase

@Composable
fun MonthYearDropdown(
    modifier: Modifier = Modifier,
    list: List<TransactionEntity>,
    selectedMonthYear: String,
    onMonthYearSelected: (String) -> Unit,
) {

    val context = LocalContext.current

    val monthYearOptions = remember(list) {
        list.mapNotNull { transaction ->
            val parts = transaction.date.split(getString(context, R.string.point))
            if (parts.size == 3) {
                val month = parts[1].toIntOrNull()
                val year = parts[2].toIntOrNull()
                if (month != null && year != null) {
                    val localDate = LocalDate.of(year, month, 1)
                    val formatter = DateTimeFormatter.ofPattern(getString(context, R.string.month_pattern), Locale(getString(context, R.string.month_language)))
                    localDate.format(formatter).replaceFirstChar { it.uppercase() }
                } else null
            } else null
        }.distinct().sorted()
    }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier

    ) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedMonthYear,
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
            modifier = Modifier,
            containerColor = Color.White
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.all),
                        color = Color.Black,
                        maxLines = 1
                    )
                },
                onClick = {
                    onMonthYearSelected(getString(context, R.string.all))
                    expanded = false
                },
            )
            monthYearOptions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = Color.Black,
                            maxLines = 1
                        )
                    },
                    onClick = {
                        onMonthYearSelected(option)
                        expanded = false
                    },
                )
            }
        }
    }
}