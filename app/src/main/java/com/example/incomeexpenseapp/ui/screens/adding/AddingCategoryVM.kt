package com.example.incomeexpenseapp.ui.screens.adding

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import com.example.incomeexpenseapp.data.repository.Repository
import com.example.incomeexpenseapp.ui.theme.Black
import com.example.incomeexpenseapp.utils.colorToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddingCategoryVM @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val messageFlow: MutableStateFlow<Pair<Boolean, String>> = MutableStateFlow(Pair(false, ""))

    fun showMessage(message: String) {
        messageFlow.value = Pair(true, message)
    }

    val categoryFlow = MutableStateFlow("")
    val categoryErrorFlow = MutableStateFlow(false)

    val flowSelectedColor: MutableStateFlow<Color> = MutableStateFlow(Black)

    private suspend fun insertCategory(type: String) {
        val category = CategoryEntity(
            category = categoryFlow.value,
            color = colorToString(flowSelectedColor.value),
            type = type
        )
        repository.insertCategory(category = category)
    }

    fun validateAndInsertCategory(
        type: String,
        onError: () -> Unit,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {
        val isEmpty = categoryFlow.value.isBlank()
        categoryErrorFlow.value = isEmpty

        if (!isEmpty) {
            withContext(Dispatchers.IO) {
                insertCategory(type)
            }
            onSuccess()
            clearFlows()
        } else {
            onError()
        }
    }

    private fun clearFlows(){
        categoryFlow.value = ""
        categoryErrorFlow.value = false
        flowSelectedColor.value = Black

    }
}