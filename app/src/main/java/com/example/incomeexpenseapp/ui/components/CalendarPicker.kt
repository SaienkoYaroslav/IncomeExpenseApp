package com.example.incomeexpenseapp.ui.components

import android.app.DatePickerDialog
import android.view.ContextThemeWrapper
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.ui.theme.Blue
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun CalendarPicker(
    modifier: Modifier = Modifier,
    dateFlow: MutableStateFlow<String>,
) {

    val context = LocalContext.current
    val contextWrapper = ContextThemeWrapper(context, R.style.CustomDatePickerDialog)

    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern(getString(context, R.string.date_format))
    val selectedDate by dateFlow.collectAsStateWithLifecycle("")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, Color.White, RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable {
                val currentDate = Calendar.getInstance()
                val year = currentDate.get(Calendar.YEAR)
                val month = currentDate.get(Calendar.MONTH)
                val day = currentDate.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    contextWrapper,
                    { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                        val selectedLocalDate =
                            LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
                        dateFlow.value =
                            selectedLocalDate.format(dateFormatter)
                    },
                    year,
                    month,
                    day
                )
//                datePickerDialog.datePicker.minDate = currentDate.timeInMillis
                datePickerDialog.show()
            }
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(0.1f))
        Text(
            modifier = Modifier.weight(0.9f),
            text = selectedDate.ifEmpty { today.format(dateFormatter) },
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier.weight(0.1f)
        ) {
            Icon(
                modifier = Modifier,
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Blue
            )
        }
    }
}

