package com.example.incomeexpenseapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTransactions(list: List<TransactionEntity>)

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transaction_table WHERE categoryId = :catId")
    suspend fun getTransactionsByCategory(catId: Int): List<TransactionEntity>

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}
