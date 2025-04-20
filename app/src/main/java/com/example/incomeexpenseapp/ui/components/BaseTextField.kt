package com.example.incomeexpenseapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.utils.filterIntegerInput
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun BaseTextField(
    modifier: Modifier = Modifier,
    label: String,
    maxChar: Int = 15,
    textColor: Color = Color.White,
    singleLine: Boolean = true,
    stateTextFlow: MutableStateFlow<String>,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null,
    updateText: (String) -> Unit
) {

    val context = LocalContext.current
    val text by stateTextFlow.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = text,
        onValueChange = {
            if (label == getString(context, R.string.sum)) {
                val filteredText = filterIntegerInput(it)
                if (filteredText.length <= maxChar) updateText(filteredText)
            } else {
                if (it.length <= maxChar) updateText(it)
            }
        },
        shape = RoundedCornerShape(10.dp),
        isError = isError && text.isBlank(),
        supportingText = {
            if (isError && errorMessage != null && text.isBlank()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 12.sp)
            }
        },
        textStyle = TextStyle(
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium
        ),
        label = {
            Text(
                modifier = Modifier,
                text = label,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp,
                fontSize = 16.sp
            )
        },
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            cursorColor = Color.White,
            selectionColors = TextSelectionColors(
                Color.White,
                Color.White
            ),
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None
    )

}


@Preview
@Composable
private fun Preview() {
    BaseTextField(
        label = "",
        stateTextFlow = MutableStateFlow("")
    ) {}
}

