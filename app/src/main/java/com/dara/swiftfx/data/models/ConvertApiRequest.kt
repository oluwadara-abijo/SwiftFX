package com.dara.swiftfx.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ConvertApiRequest(
    val from: String,
    val to: String,
    val amount : Int
)
