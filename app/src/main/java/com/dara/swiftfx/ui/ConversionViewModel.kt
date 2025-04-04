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

    fun updateCurrencyFrom(currency: String) {
        updateState(selectedCurrencyFrom = currency)
    }

    fun updateCurrencyTo(currency: String) {
        updateState(selectedCurrencyTo = currency)
    }

    // Updates the current state of the UI
    private fun updateState(
        symbols: List<String>? = null,
        selectedCurrencyFrom: String? = null,
        selectedCurrencyTo: String? = null,
        isLoading: Boolean? = null,
        errorMessage: String? = null,
    ) {
        _uiState.value = _uiState.value.copy(
            currencies = symbols ?: _uiState.value.currencies,
            selectedCurrencyFrom = selectedCurrencyFrom ?: _uiState.value.selectedCurrencyFrom,
            selectedCurrencyTo = selectedCurrencyTo ?: _uiState.value.selectedCurrencyTo,
            isLoading = isLoading ?: _uiState.value.isLoading,
            errorMessage = errorMessage ?: _uiState.value.errorMessage,
        )
    }
}
