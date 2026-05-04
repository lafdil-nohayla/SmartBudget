package com.smartbudget.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.smartbudget.data.local.entity.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name ASC")
    suspend fun getActiveCategoriesSync(): List<Category>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT COUNT(*) FROM expenses WHERE categoryId = :categoryId")
    suspend fun countExpensesForCategory(categoryId: Long): Int

    @Query("SELECT * FROM categories WHERE name = :name LIMIT 1")
    suspend fun getCategoryByName(name: String): Category?
}
