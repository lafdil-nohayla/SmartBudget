package com.smartbudget.data.repository

import androidx.lifecycle.LiveData
import com.smartbudget.data.local.dao.CategoryDao
import com.smartbudget.data.local.entity.Category
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun getAllCategories(): LiveData<List<Category>> = categoryDao.getAllCategories()

    fun getActiveCategories(): LiveData<List<Category>> = categoryDao.getActiveCategories()

    suspend fun getActiveCategoriesSync(): List<Category> = categoryDao.getActiveCategoriesSync()

    suspend fun getCategoryById(id: Long): Category? = categoryDao.getCategoryById(id)

    suspend fun insertCategory(category: Category): Long = categoryDao.insertCategory(category)

    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)

    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    suspend fun countExpensesForCategory(categoryId: Long): Int =
        categoryDao.countExpensesForCategory(categoryId)

    suspend fun getCategoryByName(name: String): Category? = categoryDao.getCategoryByName(name)
}
