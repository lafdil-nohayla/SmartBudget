package com.smartbudget.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.smartbudget.data.local.entity.MonthlyBudget

@Dao
interface MonthlyBudgetDao {
    @Query("SELECT * FROM monthly_budgets WHERE month = :month")
    fun getBudgetsForMonth(month: String): LiveData<List<MonthlyBudget>>

    @Query("SELECT * FROM monthly_budgets WHERE month = :month AND categoryId = :categoryId LIMIT 1")
    suspend fun getBudgetForMonthAndCategory(month: String, categoryId: Long): MonthlyBudget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: MonthlyBudget): Long

    @Update
    suspend fun updateBudget(budget: MonthlyBudget)

    @Delete
    suspend fun deleteBudget(budget: MonthlyBudget)
}
