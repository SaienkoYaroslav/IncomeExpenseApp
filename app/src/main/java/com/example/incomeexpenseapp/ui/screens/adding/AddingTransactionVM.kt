package com.example.incomeexpenseapp.ui.screens.adding

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity
import com.example.incomeexpenseapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddingTransactionVM @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository
) : ViewModel() {

    private val messageFlow: MutableStateFlow<Pair<Boolean, String>> = MutableStateFlow(Pair(false, ""))

    fun showMessage(message: String) {
        messageFlow.value = Pair(true, message)
    }

    val sumFlow = MutableStateFlow("")
    val sumErrorFlow = MutableStateFlow(false)

    val categories = repository.getAllCategories()

    private val today = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern(getString(context, R.string.date_format))
    val dateFlow: MutableStateFlow<String> =
        MutableStateFlow(today.format(dateFormatter))

    private suspend fun insertTransaction(category: CategoryEntity) {
        val transaction = TransactionEntity(
            sum = sumFlow.value,
            type = category.type,
            color = category.color,
            date = dateFlow.value,
            categoryId = category.id ?: -1,
            categoryName = category.category
        )
        repository.insertTransaction(transaction)
    }

    fun validateAndInsertTransaction(
        category: CategoryEntity,
        onError: () -> Unit,
        onSuccess: () -> Unit
    ) = viewModelScope.launch {
        val isEmpty = sumFlow.value.isBlank()
        sumErrorFlow.value = isEmpty

        if (category.id == -1) {
            showMessage(getString(context, R.string.select_a_category))
            return@launch
        }

        if (!isEmpty) {
            withContext(Dispatchers.IO) {
                insertTransaction(category)
            }
            onSuccess()
            clearFlows()
        } else {
            onError()
        }
    }

    private fun clearFlows() {
        sumFlow.value = ""
        sumErrorFlow.value = false
        dateFlow.value = today.format(dateFormatter)
    }

    fun deleteCategory(item: CategoryEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.deleteCategory(item)
        }
        showMessage(getString(context, R.string.trx_was_deleted))
    }

}