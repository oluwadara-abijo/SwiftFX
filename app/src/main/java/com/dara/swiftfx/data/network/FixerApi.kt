package com.dara.swiftfx.data.network

import com.dara.swiftfx.data.models.ConvertApiResponse
import com.dara.swiftfx.data.models.SymbolsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApi {

    @GET("symbols")
    suspend fun fetchSymbols(): SymbolsApiResponse

    @GET("latest")
    suspend fun getExchangeRate(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): ConvertApiResponse

}
