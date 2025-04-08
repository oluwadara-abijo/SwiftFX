package com.dara.swiftfx.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SymbolsApiResponse(
    val success: Boolean,
    val symbols: Map<String, String>? = null,
    val error: FixerError? = null
)
