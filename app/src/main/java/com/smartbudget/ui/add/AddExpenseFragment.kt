package com.smartbudget.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.smartbudget.data.local.entity.Category
import com.smartbudget.databinding.FragmentAddExpenseBinding
import com.smartbudget.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddExpenseFragment : Fragment() {

    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddExpenseViewModel by viewModels()
    private val args: AddExpenseFragmentArgs by navArgs()

    private var selectedDate: Long = System.currentTimeMillis()
    private var categories: List<Category> = emptyList()
    private var prefilled = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expenseId = args.expenseId
        if (expenseId > 0) {
            viewModel.loadExpense(expenseId)
            binding.toolbar.title = "Modifier la dépense"
        } else {
            binding.toolbar.title = "Nouvelle dépense"
        }

        // Charger la devise depuis les préférences à chaque ouverture
        viewModel.loadCurrency(requireContext())

        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        updateDateDisplay()
        binding.btnPickDate.setOnClickListener { showDatePicker() }
        setupPaymentMethods()

        // Observer la devise — met à jour le label du champ montant en temps réel
        viewModel.currency.observe(viewLifecycleOwner) { currencyCode ->
            binding.textCurrencyLabel.text = currencyCode
        }

        viewModel.categories.observe(viewLifecycleOwner) { cats ->
            categories = cats
            setupCategorySpinner(cats)
        }

        viewModel.currentExpense.observe(viewLifecycleOwner) { expense ->
            if (expense != null && !prefilled) {
                prefilled = true
                binding.editAmount.setText(expense.amount.toString())
                selectedDate = expense.date
                updateDateDisplay()
                binding.editNote.setText(expense.note)
                val catIndex = categories.indexOfFirst { it.id == expense.categoryId }
                if (catIndex >= 0) binding.spinnerCategory.setSelection(catIndex)
                val methods = listOf("Espèces", "Carte bancaire", "Virement")
                val methodIndex = methods.indexOf(expense.paymentMethod)
                if (methodIndex >= 0) binding.spinnerPaymentMethod.setSelection(methodIndex)
            }
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Dépense enregistrée !", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { msg ->
            msg?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
        }

        binding.btnSave.setOnClickListener { saveExpense() }
    }

    private fun setupCategorySpinner(cats: List<Category>) {
        val items = cats.map { "${it.icon} ${it.name}" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter
    }

    private fun setupPaymentMethods() {
        val methods = listOf("Espèces", "Carte bancaire", "Virement")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, methods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = adapter
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = selectedDate
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                cal.set(year, month, day, 12, 0, 0)
                selectedDate = cal.timeInMillis
                updateDateDisplay()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateDisplay() {
        binding.textSelectedDate.text = DateUtils.formatDate(selectedDate)
    }

    private fun saveExpense() {
        val amountStr = binding.editAmount.text.toString().trim()
        val amount = amountStr.toDoubleOrNull()
        val categoryId = categories.getOrNull(binding.spinnerCategory.selectedItemPosition)?.id
        val note = binding.editNote.text.toString().trim()
        val paymentMethods = listOf("Espèces", "Carte bancaire", "Virement")
        val paymentMethod = paymentMethods.getOrElse(binding.spinnerPaymentMethod.selectedItemPosition) { "Espèces" }
        viewModel.saveExpense(amount, categoryId, selectedDate, note, paymentMethod)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
