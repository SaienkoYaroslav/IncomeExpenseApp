package com.example.incomeexpenseapp.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import com.example.incomeexpenseapp.data.db.CategoryWithTransactions
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class PieSliceData(
    val categoryName: String,
    val totalSum: Long,
    val color: Color
)

fun mapCategoriesToSlices(data: List<CategoryWithTransactions>): List<PieSliceData> {
    return data.map { cwt ->
        // Підрахунок усіх сум із transaction
        val totalSum = cwt.transactions.sumOf { it.sum.toLongOrNull() ?: 0L }
        // Колір із category.color (наприклад, "#FF0000")
        val sliceColor = stringToColor(cwt.category.color)
        PieSliceData(
            categoryName = cwt.category.category,
            totalSum = totalSum,
            color = sliceColor
        )
    }
}

fun TransactionEntity.monthYearString(locale: Locale = Locale("ru")): String? {
    val parts = date.split(".") // "05.02.2025" => ["05","02","2025"]
    if (parts.size == 3) {
        val month = parts[1].toIntOrNull() // "02" -> 2
        val year = parts[2].toIntOrNull()  // "2025" -> 2025
        if (month != null && year != null) {
            return try {
                // day = 1, тому що фільтруємо тільки по місяцю і року
                val localDate = LocalDate.of(year, month, 1)
                val formatter = DateTimeFormatter.ofPattern("LLLL yyyy", locale)
                // "февраль 2025" => replaceFirstChar => "Февраль 2025"
                localDate.format(formatter).replaceFirstChar { it.uppercase() }
            } catch (e: DateTimeException) {
                null
            }
        }
    }
    return null
}

fun ViewModel.updateText(newText: String, oldText: MutableStateFlow<String>) {
    oldText.value = newText
}

fun filterIntegerInput(input: String): String {
    val digits = input.filter { it.isDigit() }
    val result = StringBuilder()
    for (c in digits) {
        if (result.isEmpty() && c == '0') {
        } else {
            result.append(c)
        }
    }
    if (result.length > 7) {
        result.setLength(7)
    }
    return result.toString()
}

fun formatNumber(number: Long): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
    }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(number)
}

fun formatDouble(number: Double): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
    }
    val formatter = DecimalFormat("#,###", symbols)
    return formatter.format(number)
}

fun colorToString(color: Color): String {
    return "#%02x%02x%02x%02x".format(
        (color.alpha * 255).toInt(),
        (color.red * 255).toInt(),
        (color.green * 255).toInt(),
        (color.blue * 255).toInt()
    )
}

fun stringToColor(value: String): Color {
    return try {
        if (value.startsWith("#")) {
            Color(value.toColorInt())
        } else {
            Color(android.graphics.Color.parseColor(value))
        }
    } catch (e: Exception) {
        Color.Gray
    }
}