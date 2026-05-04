package com.smartbudget.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.FRENCH)
    private val shortMonthFormat = SimpleDateFormat("MMM", Locale.FRENCH)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
    private val monthKeyFormat = SimpleDateFormat("yyyy-MM", Locale.FRENCH)

    fun getMonthDisplayName(year: Int, month: Int): String {
        val cal = Calendar.getInstance()
        cal.set(year, month, 1)
        return monthYearFormat.format(cal.time).replaceFirstChar { it.uppercase() }
    }

    fun getStartOfMonth(year: Int, month: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(year, month, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun getEndOfMonth(year: Int, month: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(year, month, 1, 23, 59, 59)
        cal.set(Calendar.MILLISECOND, 999)
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        return cal.timeInMillis
    }

    fun formatDate(millis: Long): String {
        return dateFormat.format(Date(millis))
    }

    fun formatMonthKey(year: Int, month: Int): String {
        val cal = Calendar.getInstance()
        cal.set(year, month, 1)
        return monthKeyFormat.format(cal.time)
    }

    fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
    fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH)
}
