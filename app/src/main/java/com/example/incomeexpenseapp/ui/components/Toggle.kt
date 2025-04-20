package com.example.incomeexpenseapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.incomeexpenseapp.ui.theme.Black

@Composable
fun Toggle(
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSelected = selectedOption == option
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) Black else Color.LightGray.copy(alpha = 0.8f),
                label = ""
            )
            val contentColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f), label = ""
            )
            Button(
                enabled = enable,
                onClick = { onOptionSelected(option) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor,
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 5.dp)
            ) {
                Text(
                    text = option,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    fontSize = 15.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}