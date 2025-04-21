package com.example.incomeexpenseapp.data.repository

import com.example.incomeexpenseapp.data.db.CategoryWithTransactions
import com.example.incomeexpenseapp.data.db.dao.CategoryDao
import com.example.incomeexpenseapp.data.db.dao.TransactionDao
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity
import com.example.incomeexpenseapp.data.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
): Repository {

    override fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    override suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    override fun getAllTransaction(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    override fun getListTransactions(): List<TransactionEntity> {
        return transactionDao.getListTransactions()
    }

    override suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    override fun getCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>> {
        return categoryDao.getCategoriesWithTransactions()
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun deleteAllCategories() {
        categoryDao.deleteAllCategories()
    }

}