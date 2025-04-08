package com.dara.swiftfx.data.models

import kotlinx.serialization.Serializable

@Serializable
data class FixerError(
    val code: Int,
    val type: String,
    val info: String
) {

}
