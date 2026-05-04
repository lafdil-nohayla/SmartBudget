package com.smartbudget.ui.expenses

import androidx.lifecycle.*
import com.smartbudget.data.local.entity.Category
import com.smartbudget.data.local.entity.Expense
import com.smartbudget.data.repository.CategoryRepository
import com.smartbudget.data.repository.ExpenseRepository
import com.smartbudget.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _currentYear = MutableLiveData(DateUtils.getCurrentYear())
    private val _currentMonth = MutableLiveData(DateUtils.getCurrentMonth())

    val currentYear: LiveData<Int> = _currentYear
    val currentMonth: LiveData<Int> = _currentMonth

    val monthDisplayName: LiveData<String> = MediatorLiveData<String>().apply {
        fun update() {
            val y = _currentYear.value ?: return
            val m = _currentMonth.value ?: return
            value = DateUtils.getMonthDisplayName(y, m)
        }
        addSource(_currentYear) { update() }
        addSource(_currentMonth) { update() }
    }

    private val _selectedCategoryId = MutableLiveData<Long?>(null)
    val selectedCategoryId: LiveData<Long?> = _selectedCategoryId

    val expenses: LiveData<List<Expense>> = MediatorLiveData<List<Expense>>().apply {
        var currentExpenses: LiveData<List<Expense>>? = null
        fun refresh() {
            val y = _currentYear.value ?: return
            val m = _currentMonth.value ?: return
            val catId = _selectedCategoryId.value
            currentExpenses?.let { removeSource(it) }
            val start = DateUtils.getStartOfMonth(y, m)
            val end = DateUtils.getEndOfMonth(y, m)
            currentExpenses = if (catId != null) {
                expenseRepository.getExpensesByMonthAndCategory(start, end, catId)
            } else {
                expenseRepository.getExpensesByMonth(start, end)
            }
            addSource(currentExpenses!!) { value = it }
        }
        addSource(_currentYear) { refresh() }
        addSource(_currentMonth) { refresh() }
        addSource(_selectedCategoryId) { refresh() }
    }

    val totalForMonth: LiveData<Double?> = MediatorLiveData<Double?>().apply {
        var currentTotal: LiveData<Double?>? = null
        fun refresh() {
            val y = _currentYear.value ?: return
            val m = _currentMonth.value ?: return
            currentTotal?.let { removeSource(it) }
            val start = DateUtils.getStartOfMonth(y, m)
            val end = DateUtils.getEndOfMonth(y, m)
            currentTotal = expenseRepository.getTotalForMonth(start, end)
            addSource(currentTotal!!) { value = it }
        }
        addSource(_currentYear) { refresh() }
        addSource(_currentMonth) { refresh() }
    }

    val categories: LiveData<List<Category>> = categoryRepository.getActiveCategories()

    fun previousMonth() {
        val y = _currentYear.value ?: return
        val m = _currentMonth.value ?: return
        if (m == 0) {
            _currentYear.value = y - 1
            _currentMonth.value = 11
        } else {
            _currentMonth.value = m - 1
        }
    }

    fun nextMonth() {
        val y = _currentYear.value ?: return
        val m = _currentMonth.value ?: return
        if (m == 11) {
            _currentYear.value = y + 1
            _currentMonth.value = 0
        } else {
            _currentMonth.value = m + 1
        }
    }

    fun setSelectedCategory(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            expenseRepository.deleteExpense(expense)
        }
    }
}
