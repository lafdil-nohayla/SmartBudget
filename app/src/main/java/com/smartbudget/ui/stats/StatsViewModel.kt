package com.smartbudget.ui.stats

import androidx.lifecycle.*
import com.smartbudget.data.local.entity.Category
import com.smartbudget.data.repository.CategoryRepository
import com.smartbudget.data.repository.ExpenseRepository
import com.smartbudget.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryStat(
    val category: Category,
    val total: Double,
    val percentage: Double
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _currentYear = MutableLiveData(DateUtils.getCurrentYear())
    private val _currentMonth = MutableLiveData(DateUtils.getCurrentMonth())

    val monthDisplayName: LiveData<String> = MediatorLiveData<String>().apply {
        fun update() {
            val y = _currentYear.value ?: return
            val m = _currentMonth.value ?: return
            value = DateUtils.getMonthDisplayName(y, m)
        }
        addSource(_currentYear) { update() }
        addSource(_currentMonth) { update() }
    }

    private val _stats = MutableLiveData<List<CategoryStat>>()
    val stats: LiveData<List<CategoryStat>> = _stats

    private val _totalMonth = MutableLiveData<Double>()
    val totalMonth: LiveData<Double> = _totalMonth

    private val _prevTotalMonth = MutableLiveData<Double>()
    val prevTotalMonth: LiveData<Double> = _prevTotalMonth

    // Devise tirée directement des dépenses existantes (ex: MAD)
    // pour rester compatible avec les données de démo
    private val _expenseCurrency = MutableLiveData<String>("MAD")
    val expenseCurrency: LiveData<String> = _expenseCurrency

    init {
        refreshStats()
    }

    fun refreshStats() {
        viewModelScope.launch {
            val y = _currentYear.value ?: return@launch
            val m = _currentMonth.value ?: return@launch

            val start = DateUtils.getStartOfMonth(y, m)
            val end = DateUtils.getEndOfMonth(y, m)

            // Récupère la devise depuis les dépenses réelles du mois
            val expenses = expenseRepository.getExpensesByMonthSync(start, end)
            _expenseCurrency.value = expenses.firstOrNull()?.currency ?: "MAD"

            val categoryTotals = expenseRepository.getCategoryTotalsForMonth(start, end)
            val allCategories = categoryRepository.getActiveCategoriesSync()
            val total = categoryTotals.sumOf { it.total }
            _totalMonth.value = total

            val prevM = if (m == 0) 11 else m - 1
            val prevY = if (m == 0) y - 1 else y
            val prevStart = DateUtils.getStartOfMonth(prevY, prevM)
            val prevEnd = DateUtils.getEndOfMonth(prevY, prevM)
            val prevTotals = expenseRepository.getCategoryTotalsForMonth(prevStart, prevEnd)
            _prevTotalMonth.value = prevTotals.sumOf { it.total }

            val statsList = categoryTotals.mapNotNull { ct ->
                val cat = allCategories.find { it.id == ct.categoryId } ?: return@mapNotNull null
                val pct = if (total > 0) (ct.total / total * 100) else 0.0
                CategoryStat(cat, ct.total, pct)
            }.sortedByDescending { it.total }

            _stats.value = statsList
        }
    }

    fun previousMonth() {
        val y = _currentYear.value ?: return
        val m = _currentMonth.value ?: return
        if (m == 0) { _currentYear.value = y - 1; _currentMonth.value = 11 }
        else _currentMonth.value = m - 1
        refreshStats()
    }

    fun nextMonth() {
        val y = _currentYear.value ?: return
        val m = _currentMonth.value ?: return
        if (m == 11) { _currentYear.value = y + 1; _currentMonth.value = 0 }
        else _currentMonth.value = m + 1
        refreshStats()
    }
}
