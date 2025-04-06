package com.dara.swiftfx.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ConvertApiResponse(
    val success: Boolean,
    val timestamp: Long,
    val base: String,
    val date: String,
    val rates: Map<String, Float>
)
