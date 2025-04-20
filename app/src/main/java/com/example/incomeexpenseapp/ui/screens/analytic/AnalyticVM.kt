package com.example.incomeexpenseapp.ui.screens.analytic

import androidx.lifecycle.ViewModel
import com.example.incomeexpenseapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AnalyticVM @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val messageFlow: MutableStateFlow<Pair<Boolean, String>> = MutableStateFlow(Pair(false, ""))

    fun showMessage(message: String) {
        messageFlow.value = Pair(true, message)
    }

    val categoriesWithTransactionsState = repository.getCategoriesWithTransactions()

    val transactions = repository.getAllTransaction()

}