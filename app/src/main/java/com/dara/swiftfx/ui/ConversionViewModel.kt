package com.dara.swiftfx.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dara.swiftfx.data.ConversionRepository
import com.dara.swiftfx.data.network.utils.ErrorHandler
import com.dara.swiftfx.utils.formatDateToDayMonth
import com.dara.swiftfx.utils.getWeeklyDates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
                    getHistoricalExchangeRates(base, symbols)
                },
                onFailure = { exception ->
                    updateState(
                        isLoading = false,
                        errorMessage = errorHandler.getErrorMessage(exception)
                    )
                })
        }
    }

    suspend fun getHistoricalExchangeRates(base: String, symbols: String) {
        // Get the dates for which to fetch historical data
        val historicalDatesForServer = getWeeklyDates()

        val historicalDatesForDisplay = mutableListOf<String>()
        val historicalRatePairs = mutableListOf<Pair<String, Double>>()

        coroutineScope {
            val deferredResults = historicalDatesForServer.map { date ->
                async {
                    val formattedDate = formatDateToDayMonth(date)
                    historicalDatesForDisplay.add(formattedDate)
                    repository.getExchangeRateHistory(date, base, symbols).map { response ->
                        Pair(formattedDate, response.rate.toDouble())
                    }.onFailure { exception ->
                        updateState(
                            isLoading = false,
                            errorMessage = errorHandler.getErrorMessage(exception)
                        )
                        return@async null
                    }.getOrNull()
                }
            }
            val results = deferredResults.awaitAll().filterNotNull()
            results.forEach { historicalRatePairs.add(it) }
        }

        // Process the results after all data has been fetched.
        if (historicalRatePairs.isNotEmpty()) {
            // Extract dates and rates from the pairs.
            val historicalDates = historicalRatePairs.map { it.first }
            val historicalRates = historicalRatePairs.map { it.second }
            updateState(
                historyDates = historicalDates,
                historyRates = historicalRates,
                isLoading = false
            )
        } else {
            updateState(
                isLoading = false,
                errorMessage = "Failed to fetch historical data"
            )
        }

        // Get historical rates for each date
//        for (date in historicalDatesForServer) {
//            historicalDatesForDisplay.add(formatDateToDayMonth(date))
//            viewModelScope.launch {
//                val result = repository.getExchangeRateHistory(date, base, symbols)
//                result.fold(
//                    onSuccess = { response ->
//                        historicalRates.add(response.rate.toDouble())
//                    },
//                    onFailure = { exception ->
//                        updateState(
//                            isLoading = false,
//                            errorMessage = errorHandler.getErrorMessage(exception)
//                        )
//                    })
//            }
//        }
//        updateState(
//            historyDates = historicalDatesForDisplay,
//            historyRates = historicalRates,
//            isLoading = false
//        )
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
        historyDates: List<String>? = null,
        historyRates: List<Double>? = null,
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
            historyDates = historyDates ?: _uiState.value.historyDates,
            historyRates = historyRates ?: _uiState.value.historyRates,

            )
    }
}
