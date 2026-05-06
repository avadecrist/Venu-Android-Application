package com.example.venu.core.core_common.util
import kotlin.math.floor

fun formatDistance(distanceKm: Double): String {
    return if (distanceKm < 1.0) {
        "${(distanceKm * 1000).toInt()} m away"
    } else {
        val truncated = floor(distanceKm * 100) / 100
        "${String.format("%.2f", truncated)} km away"
    }
}

fun formatOneDecimal(value: Double): String {
    return String.format("%.1f", value)
}

fun formatTimeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "${days}d ago"
        hours > 0 -> "${hours}h ago"
        minutes > 0 -> "${minutes}m ago"
        else -> "Just now"
    }
}