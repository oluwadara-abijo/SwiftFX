package com.dara.swiftfx.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dara.swiftfx.data.ConversionRepository
import com.dara.swiftfx.data.network.utils.ErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val repository: ConversionRepository,
    private val errorHandler: ErrorHandler,
) : ViewModel() {

    private val _uiState = mutableStateOf(ConversionUiState())
    val uiState: State<ConversionUiState> = _uiState

    init {
        getCurrencies()
    }

    private fun getCurrencies() {
        updateState(isLoadingConversion = true)

        viewModelScope.launch {
            val result = repository.getCurrencies()
            result.fold(
                onSuccess = { symbolsList ->
                    updateState(currencies = symbolsList)
                },
                onFailure = { exception ->
                    updateState(
                        errorMessage = errorHandler.getErrorMessage(exception)
                    )
                })
        }
    }

    fun updateCurrencyFrom(currency: String) {
        updateState(selectedCurrencyFrom = currency)
    }

    fun updateCurrencyTo(currency: String) {
        updateState(selectedCurrencyTo = currency)
    }

    fun updateAmountFrom(amount: String) {
        updateState(amountFrom = amount)
    }

    private fun updateAmountTo(amount: String) {
        updateState(amountTo = amount)
    }

    // Updates the current state of the UI
    private fun updateState(
        currencies: List<String>? = null,
        selectedCurrencyFrom: String? = null,
        selectedCurrencyTo: String? = null,
        amountFrom: String? = null,
        amountTo: String? = null,
        exchangeRate: Float? = null,
        timestamp: Long? = null,
        isLoadingConversion: Boolean? = null,
        isLoadingGraph: Boolean? = null,
        errorMessage: String? = null,
        historyDates: List<String>? = null,
        historyRates: List<Double>? = null,
        hasChangedCurrency: Boolean? = null,
    ) {
        _uiState.value = _uiState.value.copy(
            currencies = currencies ?: _uiState.value.currencies,
            selectedCurrencyFrom = selectedCurrencyFrom ?: _uiState.value.selectedCurrencyFrom,
            selectedCurrencyTo = selectedCurrencyTo ?: _uiState.value.selectedCurrencyTo,
            exchangeRate = exchangeRate ?: _uiState.value.exchangeRate,
            amountFrom = amountFrom ?: _uiState.value.amountFrom,
            amountTo = amountTo ?: _uiState.value.amountTo,
            timestamp = timestamp ?: _uiState.value.timestamp,
            isLoadingConversion = isLoadingConversion ?: _uiState.value.isLoadingConversion,
            isLoadingGraph = isLoadingGraph ?: _uiState.value.isLoadingGraph,
            errorMessage = errorMessage ?: _uiState.value.errorMessage,
            historyDates = historyDates ?: _uiState.value.historyDates,
            historyRates = historyRates ?: _uiState.value.historyRates,
            hasChangedCurrency = hasChangedCurrency ?: _uiState.value.hasChangedCurrency,
        )
    }
}
