package com.dara.swiftfx.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ConvertApiResponse(
    val success: Boolean,
    val timestamp: Long? = null,
    val historical: Boolean? = null,
    val base: String? = null,
    val date: String? = null,
    val rates: Map<String, Float>? = null,
    val error: FixerError? = null
)
