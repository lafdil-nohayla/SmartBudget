package com.smartbudget.ui.expenses

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smartbudget.data.local.entity.Category
import com.smartbudget.databinding.FragmentExpensesBinding
import com.smartbudget.utils.CurrencyPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpensesFragment : Fragment() {

    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpensesViewModel by viewModels()
    private lateinit var adapter: ExpenseAdapter
    private var categoriesList: List<Category> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        // Met à jour la devise du badge dans le formulaire uniquement
        adapter.setCurrency(CurrencyPreferences.getSelectedCurrency(requireContext()))
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter(
            currency = CurrencyPreferences.getSelectedCurrency(requireContext()),
            onEdit = { expense ->
                val action = ExpensesFragmentDirections.actionExpensesToAddExpense(expense.id)
                findNavController().navigate(action)
            },
            onDelete = { expense ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Supprimer la dépense")
                    .setMessage("Supprimer cette dépense de ${String.format("%.2f", expense.amount)} ${expense.currency} ?")
                    .setPositiveButton("Supprimer") { _, _ ->
                        viewModel.deleteExpense(expense)
                        Snackbar.make(binding.root, "Dépense supprimée", Snackbar.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Annuler", null)
                    .show()
            }
        )
        binding.recyclerViewExpenses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewExpenses.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.monthDisplayName.observe(viewLifecycleOwner) {
            binding.textMonthName.text = it
        }

        viewModel.expenses.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
            binding.recyclerViewExpenses.visibility = if (expenses.isEmpty()) View.GONE else View.VISIBLE
            binding.layoutEmpty.visibility       = if (expenses.isEmpty()) View.VISIBLE else View.GONE
        }

        // Total : affiche toujours la devise stockée dans les dépenses (MAD pour les données de démo)
        viewModel.totalForMonth.observe(viewLifecycleOwner) { total ->
            val expenseCurrency = viewModel.expenses.value?.firstOrNull()?.currency ?: "MAD"
            binding.textTotalAmount.text = String.format("%.2f %s", total ?: 0.0, expenseCurrency)
        }

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoriesList = categories
            adapter.setCategories(categories)
            setupCategorySpinner(categories)
        }
    }

    private fun setupClickListeners() {
        binding.btnPreviousMonth.setOnClickListener { viewModel.previousMonth() }
        binding.btnNextMonth.setOnClickListener { viewModel.nextMonth() }
        binding.fabAddExpense.setOnClickListener {
            val action = ExpensesFragmentDirections.actionExpensesToAddExpense(-1L)
            findNavController().navigate(action)
        }
    }

    private fun setupCategorySpinner(categories: List<Category>) {
        val items = mutableListOf("Toutes les catégories")
        items.addAll(categories.map { "${it.icon} ${it.name}" })
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = spinnerAdapter
        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                viewModel.setSelectedCategory(if (pos == 0) null else categories[pos - 1].id)
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
