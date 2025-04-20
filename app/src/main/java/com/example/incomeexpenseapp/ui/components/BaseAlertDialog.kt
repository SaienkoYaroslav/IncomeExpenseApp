package com.example.incomeexpenseapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.incomeexpenseapp.R

@Composable
fun BaseAlertDialog(
    text: String = stringResource(R.string.are_you_sure_to_delete),
    confirmText: String = stringResource(R.string.delete),
    onDismissRequest: () -> Unit,
    confirmButtonClick: () -> Unit,
) {

    AlertDialog(
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.caution),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        },
        containerColor = Color.White,
        confirmButton = {
            TextButton(
                onClick = confirmButtonClick
            ) {
                Text(
                    text = confirmText,
                    color = Color.Red
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Color.Black
                )
            }
        }
    )
}