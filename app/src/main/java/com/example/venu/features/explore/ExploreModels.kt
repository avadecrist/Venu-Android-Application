package com.example.venu.features.explore

enum class ExploreGenre(val label: String) {
    Coffee("Coffee"),
    Food("Food"),
    Nightlife("Nightlife"),
    Music("Music"),
    Study("Study"),
    Outdoors("Outdoors")
}

data class PlaceUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val distanceKm: Double,
    val rating: Double,
    val genre: ExploreGenre,
    val isVerified: Boolean,
    val isSaved: Boolean
)