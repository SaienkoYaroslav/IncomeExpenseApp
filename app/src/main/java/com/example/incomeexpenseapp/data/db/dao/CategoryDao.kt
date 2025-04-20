package com.example.incomeexpenseapp.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.incomeexpenseapp.data.db.CategoryWithTransactions
import com.example.incomeexpenseapp.data.db.entety.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(list: List<CategoryEntity>)

    @Query("SELECT * FROM category_table")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM category_table WHERE id = :catId")
    suspend fun deleteCategoryById(catId: Int)

    @Transaction
    @Query("SELECT * FROM category_table")
    fun getCategoriesWithTransactions(): Flow<List<CategoryWithTransactions>>

    @Query("DELETE FROM category_table")
    fun deleteAllCategories()

}