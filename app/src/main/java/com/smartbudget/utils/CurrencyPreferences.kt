package com.smartbudget.utils

import android.content.Context
import android.content.SharedPreferences

object CurrencyPreferences {

    private const val PREFS_NAME = "smartbudget_prefs"
    private const val KEY_CURRENCY = "selected_currency"
    private const val DEFAULT_CURRENCY = "MAD"

    val AVAILABLE_CURRENCIES = listOf(
        "MAD" to "🇲🇦 Dirham marocain (MAD)",
        "EUR" to "🇪🇺 Euro (EUR)",
        "USD" to "🇺🇸 Dollar américain (USD)",
        "GBP" to "🇬🇧 Livre sterling (GBP)",
        "SAR" to "🇸🇦 Riyal saoudien (SAR)",
        "AED" to "🇦🇪 Dirham émirati (AED)",
        "CAD" to "🇨🇦 Dollar canadien (CAD)",
        "CHF" to "🇨🇭 Franc suisse (CHF)"
    )

    private fun getPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getSelectedCurrency(context: Context): String =
        getPrefs(context).getString(KEY_CURRENCY, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY

    fun setSelectedCurrency(context: Context, currency: String) =
        getPrefs(context).edit().putString(KEY_CURRENCY, currency).apply()

    fun getCurrencyLabel(context: Context): String {
        val code = getSelectedCurrency(context)
        return AVAILABLE_CURRENCIES.find { it.first == code }?.second ?: code
    }
}
