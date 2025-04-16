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
import java.text.DecimalFormat
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
        updateState(isLoadingConversion = true)

        viewModelScope.launch {
            val result = repository.getSymbols()
            result.fold(
                onSuccess = { symbolsList ->
                    updateState(symbols = symbolsList, isLoadingConversion = false)
                },
                onFailure = { exception ->
                    updateState(
                        isLoadingConversion = false,
                        errorMessage = errorHandler.getErrorMessage(exception)
                    )
                })
        }
    }

    fun getExchangeRate(base: String, symbols: String) {
        updateState(isLoadingConversion = true)

        viewModelScope.launch {
            val result = repository.getExchangeRate(base, symbols)
            result.fold(
                onSuccess = { response ->
                    updateState(
                        exchangeRate = response.rates!!.values.first(),
                        timestamp = response.timestamp,
                        isLoadingConversion = false
                    )
                    val result = convertCurrency(
                        uiState.value.amountFrom.toDouble(),
                        uiState.value.selectedCurrencyFrom,
                        uiState.value.selectedCurrencyTo,
                        response.rates
                    )
                    updateAmountTo(amount = result)
                },
                onFailure = { exception ->
                    updateState(
                        isLoadingConversion = false,
                        errorMessage = errorHandler.getErrorMessage(exception)
                    )
                })
        }
    }

    private fun convertCurrency(
        amount: Double,
        from: String,
        to: String,
        rates: Map<String, Float>
    ): String {
        val fromRate = rates[from] ?: error("Rate for $from not found")
        val toRate = rates[to] ?: error("Rate for $to not found")

        // Convert to EUR, then to target
        val amountInEUR = amount / fromRate
        val result = amountInEUR * toRate

        // Format with commas and 2 decimal places
        val formatter = DecimalFormat("#,##0.00")
        return formatter.format(result)
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
                            isLoadingConversion = false,
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
                isLoadingConversion = false
            )
        } else {
            updateState(
                isLoadingConversion = false,
                errorMessage = "Failed to fetch historical data"
            )
        }
    }

    fun updateCurrencyTo(currency: String) {
        updateState(selectedCurrencyTo = currency, isLoadingGraph = true)
        viewModelScope.launch {
            // Get historical rates to update graph data
            getHistoricalExchangeRates(uiState.value.selectedCurrencyFrom, currency)
            updateState(isLoadingGraph = false)
        }
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
        isLoadingConversion: Boolean? = null,
        isLoadingGraph: Boolean? = null,
        errorMessage: String? = null,
        historyDates: List<String>? = null,
        historyRates: List<Double>? = null,
        hasChangedCurrency: Boolean? = null,
    ) {
        _uiState.value = _uiState.value.copy(
            currencies = symbols ?: _uiState.value.currencies,
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
