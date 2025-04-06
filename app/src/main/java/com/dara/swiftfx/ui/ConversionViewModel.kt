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
        getSymbols()
    }

    private fun getSymbols() {
        updateState(isLoading = true)

        viewModelScope.launch {
            val result = repository.getSymbols()
            result.fold(
                onSuccess = { symbolsList ->
                    updateState(symbols = symbolsList, isLoading = false)
                },
                onFailure = { exception ->
                    updateState(
                        isLoading = false,
                        errorMessage = errorHandler.getErrorMessage(exception)
                    )
                })
        }
    }

    fun getExchangeRate(base: String, symbols: String) {
        updateState(isLoading = true)

        viewModelScope.launch {
            val result = repository.getExchangeRate(base, symbols)
            result.fold(
                onSuccess = { response ->
                    updateState(
                        exchangeRate = response.rates.values.first(),
                        timestamp = response.timestamp,
                        isLoading = false
                    )
                    updateAmountTo(
                        amount = (uiState.value.exchangeRate?.times(uiState.value.amountFrom.toFloat())
                            .toString()),
                    )
                },
                onFailure = { exception ->
                    updateState(
                        isLoading = false,
                        errorMessage = errorHandler.getErrorMessage(exception)
                    )
                })
        }
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
        symbols: List<String>? = null,
        selectedCurrencyTo: String? = null,
        amountFrom: String? = null,
        amountTo: String? = null,
        exchangeRate: Float? = null,
        timestamp: Long? = null,
        isLoading: Boolean? = null,
        errorMessage: String? = null,
    ) {
        _uiState.value = _uiState.value.copy(
            currencies = symbols ?: _uiState.value.currencies,
            selectedCurrencyTo = selectedCurrencyTo ?: _uiState.value.selectedCurrencyTo,
            exchangeRate = exchangeRate ?: _uiState.value.exchangeRate,
            amountFrom = amountFrom ?: _uiState.value.amountFrom,
            amountTo = amountTo ?: _uiState.value.amountTo,
            timestamp = timestamp ?: _uiState.value.timestamp,
            isLoading = isLoading ?: _uiState.value.isLoading,
            errorMessage = errorMessage ?: _uiState.value.errorMessage,
        )
    }
}
