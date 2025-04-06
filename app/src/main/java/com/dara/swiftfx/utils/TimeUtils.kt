package com.dara.swiftfx.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatTimestamp(timestampSeconds: Long?): String {
    if (timestampSeconds != null) {
        val date = Date(timestampSeconds * 1000) // Convert to milliseconds
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date) + " UTC"
    } else return ""
}
