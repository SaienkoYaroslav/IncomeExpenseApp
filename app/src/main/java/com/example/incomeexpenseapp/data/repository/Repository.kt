package com.example.incomeexpenseapp.data.repository

import com.example.incomeexpenseapp.data.db.CategoryWithTransactions
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllCategories(): Flow<List<CategoryEntity>>

    suspend fun insertCategory(category: CategoryEntity)

    fun getAllTransaction(): Flow<List<TransactionEntity>>

    fun getListTransactions(): List<TransactionEntity>

    suspend fun insertTransaction(transaction: TransactionEntity)

    fun getCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>>

    suspend fun deleteTransaction(transaction: TransactionEntity)

    suspend fun deleteCategory(category: CategoryEntity)

    suspend fun deleteAllCategories()

}