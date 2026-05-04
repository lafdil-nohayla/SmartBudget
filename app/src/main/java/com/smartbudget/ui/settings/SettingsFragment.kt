package com.smartbudget.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartbudget.databinding.FragmentSettingsBinding
import com.smartbudget.utils.CurrencyPreferences
import com.smartbudget.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var categoryAdapter: CategorySettingsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Categories RecyclerView
        categoryAdapter = CategorySettingsAdapter(onToggle = { viewModel.toggleCategory(it) })
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCategories.adapter = categoryAdapter

        viewModel.allCategories.observe(viewLifecycleOwner) { cats ->
            categoryAdapter.submitList(cats)
        }

        // Currency spinner setup
        setupCurrencySpinner()

        // Messages Toast
        viewModel.message.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        }

        // Selected currency observer — update spinner position
        viewModel.selectedCurrency.observe(viewLifecycleOwner) { code ->
            val index = CurrencyPreferences.AVAILABLE_CURRENCIES.indexOfFirst { it.first == code }
            if (index >= 0) binding.spinnerCurrency.setSelection(index)
        }

        // Load current currency from prefs
        viewModel.loadCurrency(requireContext())

        binding.btnAddCategory.setOnClickListener { showAddCategoryDialog() }

        binding.btnExportCsv.setOnClickListener {
            viewModel.exportCsv(
                requireContext(),
                DateUtils.getCurrentYear(),
                DateUtils.getCurrentMonth()
            )
        }
    }

    private fun setupCurrencySpinner() {
        val labels = CurrencyPreferences.AVAILABLE_CURRENCIES.map { it.second }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, labels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrency.adapter = adapter

        binding.spinnerCurrency.setOnTouchListener { _, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                showCurrencyPicker()
            }
            false
        }
    }

    private fun showCurrencyPicker() {
        val currencies = CurrencyPreferences.AVAILABLE_CURRENCIES
        val labels = currencies.map { it.second }.toTypedArray()
        val currentCode = CurrencyPreferences.getSelectedCurrency(requireContext())
        val currentIndex = currencies.indexOfFirst { it.first == currentCode }

        AlertDialog.Builder(requireContext())
            .setTitle("💱 Choisir la devise")
            .setSingleChoiceItems(labels, currentIndex) { dialog, which ->
                val selectedCode = currencies[which].first
                viewModel.saveCurrency(requireContext(), selectedCode)
                dialog.dismiss()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun showAddCategoryDialog() {
        val dialogView = layoutInflater.inflate(com.smartbudget.R.layout.dialog_add_category, null)
        AlertDialog.Builder(requireContext())
            .setTitle("Nouvelle catégorie")
            .setView(dialogView)
            .setPositiveButton("Ajouter") { _, _ ->
                val name = dialogView.findViewById<android.widget.EditText>(com.smartbudget.R.id.editCategoryName).text.toString()
                val icon = dialogView.findViewById<android.widget.EditText>(com.smartbudget.R.id.editCategoryIcon).text.toString().ifBlank { "📦" }
                val color = dialogView.findViewById<android.widget.EditText>(com.smartbudget.R.id.editCategoryColor).text.toString().ifBlank { "#607D8B" }
                viewModel.addCategory(name, icon, color)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
