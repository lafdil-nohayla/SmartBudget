package com.smartbudget.ui.settings

import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartbudget.R
import com.smartbudget.data.local.entity.Category

class CategorySettingsAdapter(
    private val onToggle: (Category) -> Unit
) : ListAdapter<Category, CategorySettingsAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_settings, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onToggle)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(category: Category, onToggle: (Category) -> Unit) {
            itemView.findViewById<TextView>(R.id.textCategoryIcon).text = category.icon
            itemView.findViewById<TextView>(R.id.textCategoryName).text = category.name
            val toggle = itemView.findViewById<Switch>(R.id.switchCategoryActive)
            toggle.setOnCheckedChangeListener(null)
            toggle.isChecked = category.isActive
            toggle.setOnCheckedChangeListener { _, _ -> onToggle(category) }
            try {
                itemView.findViewById<View>(R.id.viewColor).setBackgroundColor(Color.parseColor(category.color))
            } catch (e: Exception) {}
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(old: Category, new: Category) = old.id == new.id
        override fun areContentsTheSame(old: Category, new: Category) = old == new
    }
}
