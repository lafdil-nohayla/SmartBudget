package com.smartbudget.data.repository

import androidx.lifecycle.LiveData
import com.smartbudget.data.local.dao.CategoryTotal
import com.smartbudget.data.local.dao.ExpenseDao
import com.smartbudget.data.local.entity.Expense
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao
) {
    fun getExpensesByMonth(startDate: Long, endDate: Long): LiveData<List<Expense>> =
        expenseDao.getExpensesByMonth(startDate, endDate)

    fun getExpensesByMonthAndCategory(startDate: Long, endDate: Long, categoryId: Long): LiveData<List<Expense>> =
        expenseDao.getExpensesByMonthAndCategory(startDate, endDate, categoryId)

    fun getTotalForMonth(startDate: Long, endDate: Long): LiveData<Double?> =
        expenseDao.getTotalForMonth(startDate, endDate)

    suspend fun getCategoryTotalsForMonth(startDate: Long, endDate: Long): List<CategoryTotal> =
        expenseDao.getCategoryTotalsForMonth(startDate, endDate)

    suspend fun getExpensesByMonthSync(startDate: Long, endDate: Long): List<Expense> =
        expenseDao.getExpensesByMonthSync(startDate, endDate)

    suspend fun insertExpense(expense: Expense): Long = expenseDao.insertExpense(expense)

    suspend fun updateExpense(expense: Expense) = expenseDao.updateExpense(expense)

    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)

    suspend fun getExpenseById(id: Long): Expense? = expenseDao.getExpenseById(id)
}
