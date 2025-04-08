package com.dara.swiftfx.data

import com.dara.swiftfx.data.models.ConvertApiResponse
import com.dara.swiftfx.data.models.HistoricalRate
import com.dara.swiftfx.data.network.FixerApi
import javax.inject.Inject

class ConversionRepository @Inject constructor(private val fixerApi: FixerApi) {

    suspend fun getSymbols(): Result<List<String>> {
        return try {
            // Get server response
            val serverResponse = fixerApi.fetchSymbols()
            when {
                !serverResponse.symbols.isNullOrEmpty() -> {
                    val symbols = serverResponse.symbols.keys.toList()
                    Result.success(symbols)
                }
                // Handle FixerApi error response
                serverResponse.error != null -> Result.failure(Exception(serverResponse.error.info))
                else -> Result.failure(Exception())

            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExchangeRate(base: String, symbols: String): Result<ConvertApiResponse> {
        return try {
            // Get server response
            val serverResponse = fixerApi.getExchangeRate(base, symbols)
            when {
                !serverResponse.rates.isNullOrEmpty() -> Result.success(serverResponse)
                serverResponse.error != null -> Result.failure(Exception(serverResponse.error.info))
                else -> Result.failure(Exception())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getExchangeRateHistory(
        date: String,
        base: String,
        symbols: String
    ): Result<HistoricalRate> {
        return try {
            // Get server response
            val serverResponse = fixerApi.getHistory(date, base, symbols)
            when {
                !serverResponse.rates.isNullOrEmpty() -> {
                    // Extract date and rate from response
                    val historicalRate =
                        HistoricalRate(serverResponse.rates.values.first())
                    Result.success(historicalRate)
                }
                serverResponse.error != null -> Result.failure(Exception(serverResponse.error.info))
                else -> Result.failure(Exception())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
