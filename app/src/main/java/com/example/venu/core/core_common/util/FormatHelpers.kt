package com.example.venu.core.core_common.util

fun formatDistance(distanceKm: Double): String {
    return if (distanceKm < 1.0) {
        "${(distanceKm * 1000).toInt()} m away"
    } else {
        "${String.format("%.1f", distanceKm)} km away"
    }
}

fun formatOneDecimal(value: Double): String {
    return String.format("%.1f", value)
}