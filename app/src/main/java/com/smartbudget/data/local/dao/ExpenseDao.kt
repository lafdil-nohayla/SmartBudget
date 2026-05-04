package com.smartbudget.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.smartbudget.data.local.entity.Expense

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesByMonth(startDate: Long, endDate: Long): LiveData<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    suspend fun getExpensesByMonthSync(startDate: Long, endDate: Long): List<Expense>

    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate AND categoryId = :categoryId ORDER BY date DESC")
    fun getExpensesByMonthAndCategory(startDate: Long, endDate: Long, categoryId: Long): LiveData<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :startDate AND date <= :endDate")
    fun getTotalForMonth(startDate: Long, endDate: Long): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :startDate AND date <= :endDate AND categoryId = :categoryId")
    suspend fun getTotalByCategoryAndMonth(startDate: Long, endDate: Long, categoryId: Long): Double?

    @Query("SELECT categoryId, SUM(amount) as total FROM expenses WHERE date >= :startDate AND date <= :endDate GROUP BY categoryId ORDER BY total DESC")
    suspend fun getCategoryTotalsForMonth(startDate: Long, endDate: Long): List<CategoryTotal>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): Expense?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC LIMIT 1")
    suspend fun getLatestExpense(): Expense?
}

data class CategoryTotal(
    val categoryId: Long,
    val total: Double
)
