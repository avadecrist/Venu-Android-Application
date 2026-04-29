package com.example.venu.core.core_common.eventdetails

import com.example.venu.core.core_domain.model.Genre

fun genreLabel(genre: Genre): String {
    return when (genre) {
        Genre.FOOD -> "Food"
        Genre.STUDY -> "Study"
        Genre.MUSIC -> "Music"
        Genre.SPORTS -> "Sports"
        Genre.MUSEUMS -> "Museums"
        Genre.COFFEE -> "Coffee"
        Genre.NIGHTLIFE -> "Nightlife"
        Genre.OUTDOORS -> "Outdoors"
    }
}

fun genreChipText(genre: Genre): String {
    return when (genre) {
        Genre.FOOD -> "🍔 Food"
        Genre.STUDY -> "📚 Study"
        Genre.MUSIC -> "🎵 Music"
        Genre.SPORTS -> "🏀 Sports"
        Genre.MUSEUMS -> "🖼️ Museums"
        Genre.COFFEE -> "☕ Coffee"
        Genre.NIGHTLIFE -> "🌙 Nightlife"
        Genre.OUTDOORS -> "🌿 Outdoors"
    }
}