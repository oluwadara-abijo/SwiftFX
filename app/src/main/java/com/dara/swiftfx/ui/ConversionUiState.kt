package com.dara.swiftfx.ui

data class ConversionUiState(
    val currencies: List<String> = emptyList(),
    val selectedCurrencyFrom: String = "",
    val selectedCurrencyTo: String = "",
    val amountTo: String = "0",
    val amountFrom: String = "",
    val exchangeRate: Float? = null,
    val timestamp: Long? = null,
    val isLoadingConversion: Boolean = false,
    val isLoadingGraph: Boolean = false,
    val errorMessage: String? = null,
    val historyDates: List<String> = listOf(),
    val historyRates: List<Double> = listOf(),
    val hasChangedCurrency: Boolean = false
)
