package com.example.incomeexpenseapp.data.db.entety

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaction_table",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val categoryId: Int,
    val date: String,
    val sum: String,
    val type: String,
    val categoryName: String,
    val color: String,
)