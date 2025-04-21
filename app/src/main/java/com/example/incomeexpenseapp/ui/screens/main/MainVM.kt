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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: Repository
) : ViewModel() {

    private val _messageEvent = MutableStateFlow("")
    val messageEvent = _messageEvent

    private val _stateFlow = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val stateFlow: StateFlow<ScreenState> = _stateFlow

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        repository.getAllTransaction()
            .onEach { list ->
                _stateFlow.value = ScreenState.Success(transactions = list)
            }
            .catch { e ->
                _stateFlow.value = ScreenState.Error(e.message ?: context.getString(R.string.unknown_error))
            }
            .launchIn(viewModelScope)
    }

    fun deleteTx(item: TransactionEntity) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.deleteTransaction(item)
        }
        _messageEvent.emit(getString(context, R.string.trx_was_deleted))
    }

    fun onMessageShown() = viewModelScope.launch {
        _messageEvent.emit("")
    }

}

sealed class ScreenState {
    data object Loading : ScreenState()
    data class Success(
        val transactions: List<TransactionEntity>,
    ) : ScreenState()

    data class Error(val error: String) : ScreenState()
}