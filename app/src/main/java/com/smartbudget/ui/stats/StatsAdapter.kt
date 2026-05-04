package com.smartbudget.ui.stats

import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartbudget.R

class StatsAdapter : ListAdapter<CategoryStat, StatsAdapter.ViewHolder>(DiffCallback()) {

    private var currency: String = "MAD"

    fun setCurrency(currency: String) {
        this.currency = currency
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_stat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), currency)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(stat: CategoryStat, currency: String) {
            itemView.findViewById<TextView>(R.id.textCategoryIcon).text = stat.category.icon
            itemView.findViewById<TextView>(R.id.textCategoryName).text = stat.category.name
            itemView.findViewById<TextView>(R.id.textCategoryTotal).text =
                String.format("%.2f %s", stat.total, currency)
            itemView.findViewById<TextView>(R.id.textCategoryPercent).text =
                String.format("%.1f%%", stat.percentage)
            val progressBar = itemView.findViewById<ProgressBar>(R.id.progressCategory)
            progressBar.progress = stat.percentage.toInt()
            try {
                val colorView = itemView.findViewById<View>(R.id.viewCategoryColor)
                colorView.setBackgroundColor(Color.parseColor(stat.category.color))
            } catch (e: Exception) {}
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<CategoryStat>() {
        override fun areItemsTheSame(old: CategoryStat, new: CategoryStat) = old.category.id == new.category.id
        override fun areContentsTheSame(old: CategoryStat, new: CategoryStat) = old == new
    }
}
