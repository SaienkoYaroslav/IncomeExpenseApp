package com.example.incomeexpenseapp.data.db

import androidx.room.Embedded
import androidx.room.Relation
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import com.example.incomeexpenseapp.data.db.entety.TransactionEntity

data class CategoryWithTransactions(
    @Embedded val category: CategoryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val transactions: List<TransactionEntity>
)
