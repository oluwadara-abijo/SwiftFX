package com.dara.swiftfx.data.network

import com.dara.swiftfx.data.models.LatestRatesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenExchangeApi {

    @GET("currencies.json")
    suspend fun fetchCurrencies(): Map<String, String>

    @GET("latest.json")
    suspend fun fetchLatestRates(@Query("app_id") appId: String): LatestRatesResponse

}
