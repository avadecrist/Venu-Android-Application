package com.example.venu.core.core_domain.model

enum class PriceTier {
    FREE,
    ONE,
    TWO,
    THREE,
    FOUR,
    UNKNOWN
}

val PriceTier.label: String
    get() = when (this) {
        PriceTier.FREE -> "Free"
        PriceTier.ONE -> "$"
        PriceTier.TWO -> "$$"
        PriceTier.THREE -> "$$$"
        PriceTier.FOUR -> "$$$$"
        PriceTier.UNKNOWN -> "Unknown"
    }