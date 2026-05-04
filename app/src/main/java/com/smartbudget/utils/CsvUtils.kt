package com.smartbudget.utils

import android.content.Context
import android.os.Environment
import com.smartbudget.data.local.entity.Category
import com.smartbudget.data.local.entity.Expense
import java.io.File
import java.io.FileWriter

object CsvUtils {
    fun exportToCsv(
        context: Context,
        expenses: List<Expense>,
        categories: List<Category>,
        monthLabel: String
    ): File? {
        return try {
            val categoryMap = categories.associateBy { it.id }
            val fileName = "smartbudget_${monthLabel.replace(" ", "_")}.csv"
            val dir = context.getExternalFilesDir(null) ?: context.filesDir
            val file = File(dir, fileName)

            FileWriter(file).use { writer ->
                writer.append("Date,Montant,Devise,Catégorie,Note,Méthode Paiement\n")
                expenses.forEach { expense ->
                    val catName = categoryMap[expense.categoryId]?.name ?: "Autre"
                    writer.append("${DateUtils.formatDate(expense.date)},")
                    writer.append("${expense.amount},")
                    writer.append("${expense.currency},")
                    writer.append("$catName,")
                    writer.append("\"${expense.note}\",")
                    writer.append("${expense.paymentMethod}\n")
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
