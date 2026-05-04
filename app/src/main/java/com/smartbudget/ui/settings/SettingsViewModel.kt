package com.smartbudget.ui.settings

import android.content.Context
import androidx.lifecycle.*
import com.smartbudget.data.local.entity.Category
import com.smartbudget.data.repository.CategoryRepository
import com.smartbudget.data.repository.ExpenseRepository
import com.smartbudget.utils.CurrencyPreferences
import com.smartbudget.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    val allCategories: LiveData<List<Category>> = categoryRepository.getAllCategories()

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _selectedCurrency = MutableLiveData<String>()
    val selectedCurrency: LiveData<String> = _selectedCurrency

    fun loadCurrency(context: Context) {
        _selectedCurrency.value = CurrencyPreferences.getSelectedCurrency(context)
    }

    fun saveCurrency(context: Context, currencyCode: String) {
        CurrencyPreferences.setSelectedCurrency(context, currencyCode)
        _selectedCurrency.value = currencyCode
        _message.value = "Devise changée en $currencyCode"
    }

    fun toggleCategory(category: Category) {
        viewModelScope.launch {
            if (category.isActive) {
                val count = categoryRepository.countExpensesForCategory(category.id)
                if (count > 0) {
                    _message.value = "Impossible : ${count} dépense(s) associée(s) à cette catégorie"
                    return@launch
                }
            }
            categoryRepository.updateCategory(category.copy(isActive = !category.isActive))
        }
    }

    fun addCategory(name: String, icon: String, color: String) {
        viewModelScope.launch {
            if (name.isBlank()) { _message.value = "Nom requis"; return@launch }
            val existing = categoryRepository.getCategoryByName(name)
            if (existing != null) { _message.value = "Catégorie déjà existante"; return@launch }
            categoryRepository.insertCategory(Category(name = name, icon = icon, color = color))
            _message.value = "Catégorie ajoutée ✓"
        }
    }

    fun exportCsv(context: android.content.Context, year: Int, month: Int) {
        viewModelScope.launch {
            val start = DateUtils.getStartOfMonth(year, month)
            val end = DateUtils.getEndOfMonth(year, month)
            val expenses = expenseRepository.getExpensesByMonthSync(start, end)
            val categories = categoryRepository.getActiveCategoriesSync()
            val monthLabel = DateUtils.formatMonthKey(year, month)
            val file = com.smartbudget.utils.CsvUtils.exportToCsv(context, expenses, categories, monthLabel)
            if (file != null) _message.value = "Exporté : ${file.name}"
            else _message.value = "Erreur lors de l'export"
        }
    }
}
