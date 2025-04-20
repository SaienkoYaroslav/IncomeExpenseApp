package com.example.incomeexpenseapp.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.incomeexpenseapp.ui.theme.Blue

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Black,
            fontSize = 16.sp
        )
    }
}