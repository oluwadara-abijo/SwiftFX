package com.dara.swiftfx.data.network

import retrofit2.http.GET

interface OpenExchangeApi {

    @GET("currencies.json")
    suspend fun fetchCurrencies(): Map<String, String>

}
