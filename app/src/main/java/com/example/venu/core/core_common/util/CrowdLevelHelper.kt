package com.example.venu.core.core_common.util

import com.example.venu.core.core_domain.model.CrowdLevel

fun Int.toCrowdLevel(): CrowdLevel {
    return when {
        this <= 0 -> CrowdLevel.UNKNOWN
        this < 1 -> CrowdLevel.DEAD
        this < 2 -> CrowdLevel.CHILL
        else -> CrowdLevel.PACKED
    }
}