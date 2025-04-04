package com.dara.swiftfx.ui

data class ConversionUiState(
    val currencies: List<String> = emptyList(),
    val selectedCurrencyFrom: String = "",
    val selectedCurrencyTo: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
