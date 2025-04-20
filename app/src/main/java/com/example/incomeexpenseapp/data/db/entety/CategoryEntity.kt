package com.example.incomeexpenseapp.data.db.entety

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val category: String,
    val color: String,
    val type: String
)