package com.smartbudget.ui.stats

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartbudget.databinding.FragmentStatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatsViewModel by viewModels()
    private lateinit var statsAdapter: StatsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statsAdapter = StatsAdapter()
        binding.recyclerViewStats.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewStats.adapter = statsAdapter

        viewModel.monthDisplayName.observe(viewLifecycleOwner) {
            binding.textMonthName.text = it
        }

        // La devise vient directement des dépenses existantes → toujours MAD pour les données de démo
        viewModel.expenseCurrency.observe(viewLifecycleOwner) { currency ->
            statsAdapter.setCurrency(currency)
        }

        viewModel.totalMonth.observe(viewLifecycleOwner) { total ->
            val currency = viewModel.expenseCurrency.value ?: "MAD"
            binding.textTotalMonth.text = String.format("%.2f %s", total, currency)
        }

        viewModel.prevTotalMonth.observe(viewLifecycleOwner) { prev ->
            val currency = viewModel.expenseCurrency.value ?: "MAD"
            val curr = viewModel.totalMonth.value ?: 0.0
            val diff = curr - prev
            val sign = if (diff >= 0) "+" else ""
            binding.textComparison.text =
                "vs mois précédent : ${sign}${String.format("%.2f", diff)} $currency"
        }

        viewModel.stats.observe(viewLifecycleOwner) { stats ->
            val currency = viewModel.expenseCurrency.value ?: "MAD"
            statsAdapter.submitList(stats)
            if (stats.isNotEmpty()) {
                val top = stats.first()
                binding.textTopCategory.text =
                    "Top : ${top.category.icon} ${top.category.name} — ${String.format("%.2f", top.total)} $currency"
            }
        }

        binding.btnPreviousMonth.setOnClickListener { viewModel.previousMonth() }
        binding.btnNextMonth.setOnClickListener { viewModel.nextMonth() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshStats()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
