package com.dara.swiftfx.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LatestRatesResponse(
    val disclaimer: String? = null,
    val license: String? = null,
    val timestamp: Long? = null,
    val base: String? = null,
    val rates: Map<String, Float>? = null,
)
