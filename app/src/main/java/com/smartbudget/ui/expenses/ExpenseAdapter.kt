package com.smartbudget.ui.expenses

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartbudget.R
import com.smartbudget.data.local.entity.Category
import com.smartbudget.data.local.entity.Expense
import com.smartbudget.utils.DateUtils

class ExpenseAdapter(
    private var currency: String = "MAD",
    private val onEdit: (Expense) -> Unit,
    private val onDelete: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ViewHolder>(DiffCallback()) {

    private var categories: List<Category> = emptyList()

    fun setCategories(categories: List<Category>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    fun setCurrency(currency: String) {
        this.currency = currency
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), categories, currency, onEdit, onDelete)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            expense: Expense,
            categories: List<Category>,
            currency: String,
            onEdit: (Expense) -> Unit,
            onDelete: (Expense) -> Unit
        ) {
            val category = categories.find { it.id == expense.categoryId }
            // Affiche la devise sauvegardée dans la dépense, sinon celle active
            val displayCurrency = if (expense.currency.isNotBlank()) expense.currency else currency
            itemView.findViewById<TextView>(R.id.textAmount).text =
                String.format("%.2f %s", expense.amount, displayCurrency)
            itemView.findViewById<TextView>(R.id.textCategory).text =
                "${category?.icon ?: "📦"} ${category?.name ?: "Autre"}"
            itemView.findViewById<TextView>(R.id.textDate).text =
                DateUtils.formatDate(expense.date)
            val noteView = itemView.findViewById<TextView>(R.id.textNote)
            if (expense.note.isNotEmpty()) {
                noteView.text = expense.note
                noteView.visibility = View.VISIBLE
            } else {
                noteView.visibility = View.GONE
            }
            itemView.setOnClickListener { onEdit(expense) }
            itemView.setOnLongClickListener { onDelete(expense); true }
            itemView.findViewById<View>(R.id.btnEdit).setOnClickListener { onEdit(expense) }
            itemView.findViewById<View>(R.id.btnDelete).setOnClickListener { onDelete(expense) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}
