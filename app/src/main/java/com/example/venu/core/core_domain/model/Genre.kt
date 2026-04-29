package com.example.venu.core.core_domain.model

enum class Genre {
    FOOD,
    STUDY,
    MUSIC,
    SPORTS,
    MUSEUMS,
    COFFEE,
    NIGHTLIFE,
    OUTDOORS
}

val Genre.label: String
    get() = when (this) {
        Genre.FOOD -> "Food"
        Genre.STUDY -> "Study"
        Genre.MUSIC -> "Music"
        Genre.SPORTS -> "Sports"
        Genre.MUSEUMS -> "Museums"
        Genre.COFFEE -> "Coffee"
        Genre.NIGHTLIFE -> "Nightlife"
        Genre.OUTDOORS -> "Outdoors"
    }