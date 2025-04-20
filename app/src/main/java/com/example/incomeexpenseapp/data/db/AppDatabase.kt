package com.example.incomeexpenseapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.incomeexpenseapp.data.db.dao.CategoryDao
import com.example.incomeexpenseapp.data.db.dao.TransactionDao
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
