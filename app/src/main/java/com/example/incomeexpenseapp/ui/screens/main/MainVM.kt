package com.example.incomeexpenseapp.ui.screens.main

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.incomeexpenseapp.R
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity
import com.example.incomeexpenseapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository
) : ViewModel() {

    val messageFlow: MutableStateFlow<Pair<Boolean, String>> = MutableStateFlow(Pair(false, ""))

    private fun showMessage(message: String) {
        messageFlow.value = Pair(true, message)
    }

    val transactions = repository.getAllTransaction()

    fun deleteTx(item: TransactionEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.deleteTransaction(item)
        }
        showMessage(getString(context, R.string.cat_was_deleted))
    }

}