package com.smartbudget.ui.add

import android.content.Context
import androidx.lifecycle.*
import com.smartbudget.data.local.entity.Category
import com.smartbudget.data.local.entity.Expense
import com.smartbudget.data.repository.CategoryRepository
import com.smartbudget.data.repository.ExpenseRepository
import com.smartbudget.utils.CurrencyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val categories: LiveData<List<Category>> = categoryRepository.getActiveCategories()

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _currentExpense = MutableLiveData<Expense?>()
    val currentExpense: LiveData<Expense?> = _currentExpense

    private val _currency = MutableLiveData<String>("MAD")
    val currency: LiveData<String> = _currency

    fun loadCurrency(context: Context) {
        _currency.value = CurrencyPreferences.getSelectedCurrency(context)
    }

    fun loadExpense(id: Long) {
        if (id <= 0) return
        viewModelScope.launch {
            _currentExpense.value = expenseRepository.getExpenseById(id)
        }
    }

    fun saveExpense(amount: Double?, categoryId: Long?, date: Long?, note: String, paymentMethod: String) {
        if (amount == null || amount <= 0) {
            _errorMessage.value = "Le montant doit être positif"
            return
        }
        if (categoryId == null) {
            _errorMessage.value = "Veuillez sélectionner une catégorie"
            return
        }
        if (date == null) {
            _errorMessage.value = "Veuillez sélectionner une date"
            return
        }
        val currencyCode = _currency.value ?: "MAD"
        viewModelScope.launch {
            val existing = _currentExpense.value
            if (existing != null) {
                expenseRepository.updateExpense(
                    existing.copy(
                        amount = amount,
                        currency = currencyCode,
                        categoryId = categoryId,
                        date = date,
                        note = note,
                        paymentMethod = paymentMethod,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            } else {
                expenseRepository.insertExpense(
                    Expense(
                        amount = amount,
                        currency = currencyCode,
                        categoryId = categoryId,
                        date = date,
                        note = note,
                        paymentMethod = paymentMethod
                    )
                )
            }
            _saveSuccess.value = true
        }
    }
}
