package com.dara.swiftfx.data.network

import com.dara.swiftfx.data.models.SymbolsApiResponse
import retrofit2.http.GET

interface FixerApi {

    @GET("symbols")
    suspend fun fetchSymbols(): SymbolsApiResponse

}
