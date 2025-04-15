package com.dara.swiftfx.data

import com.dara.swiftfx.data.network.OpenExchangeApi
import javax.inject.Inject

class ConversionRepository @Inject constructor(
    private val api: OpenExchangeApi,
) {

    suspend fun getCurrencies(): Result<List<String>> {
        return try {
            // Get server response
            val serverResponse = api.fetchCurrencies()
            // Get currency symbols from map key
            val currencies = serverResponse.keys.toList()
            Result.success(currencies)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
