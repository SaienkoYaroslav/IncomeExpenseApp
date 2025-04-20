package com.example.incomeexpenseapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat.getString
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.ui.theme.AlmostWhiteColor
import com.example.incomeexpenseapp.ui.theme.Blue
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorPickerDialog(
    onDismiss: () -> Unit,
    selectedColor: Color,
    onSelectColor: (Color) -> Unit,
) {

    val context = LocalContext.current
    val controller = rememberColorPickerController()

    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = selectedColor
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.select_color_with_contrast),
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        var color = colorEnvelope.color
                        if (colorEnvelope.hexCode == getString(context, R.string.black_color)) {
                            color = AlmostWhiteColor
                        }
                        onSelectColor(color)

                    }
                )

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = Color.White

                    )
                ) {
                    Text(stringResource(R.string.ok), color = Color.White)
                }
            }
        }
    }
}