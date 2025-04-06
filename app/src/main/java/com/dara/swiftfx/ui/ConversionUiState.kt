package com.dara.swiftfx.ui

data class ConversionUiState(
    val currencies: List<String> = emptyList(),
    val selectedCurrencyFrom: String = "EUR",
    val selectedCurrencyTo: String = "",
    val amountTo: String = "0",
    val amountFrom: String = "",
    val exchangeRate: Float? = null,
    val timestamp: Long? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
