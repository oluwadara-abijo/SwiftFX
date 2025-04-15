package com.dara.swiftfx.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * [CoreNetworkModule] is a Dagger Hilt module responsible for providing core network-related
 * dependencies.
 *
 * This module configures and provides instances of:
 * - [OkHttpClient]: An HTTP client for making network requests, with logging and timeout
 * configurations.
 * - [Json]: A JSON serializer/deserializer for handling JSON data.
 * - [Retrofit]: A type-safe HTTP client for Android and Java, built on top of OkHttp.
 * - [OpenExchangeApi]: The Retrofit service interface for the application's API endpoints.
 *
 * It is installed in the [SingletonComponent], making these dependencies available throughout
 * the application.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object CoreNetworkModule {

    /**
     * Creates [HttpLoggingInterceptor] object for logging purposes
     */
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun providesHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .connectTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            .writeTimeout(NETWORK_TIMEOUT, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun providesRetrofit(
        networkJson: Json,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
    }

    @Provides
    fun providesOpenExchangeApi(retrofit: Retrofit): OpenExchangeApi = retrofit.create()


}

private const val BASE_URL = "https://openexchangerates.org/api/"
private const val NETWORK_TIMEOUT = 30L
