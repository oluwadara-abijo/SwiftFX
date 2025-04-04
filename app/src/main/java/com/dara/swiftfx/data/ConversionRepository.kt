package com.dara.swiftfx.data

import com.dara.swiftfx.data.network.FixerApi
import javax.inject.Inject

class ConversionRepository @Inject constructor(private val fixerApi: FixerApi) {

    suspend fun getSymbols(): Result<List<String>> {
        return try {
            // Get server response
            val serverResponse = fixerApi.fetchSymbols()
            val symbols = serverResponse.symbols.keys.toList()
            Result.success(symbols)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
