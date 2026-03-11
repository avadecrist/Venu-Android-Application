package com.example.venu.features.explore.model


enum class ExploreGenre(val label: String) {
    Food("Food"),
    Study("Study"),
    Music("Music"),
    Sports("Sports"),
    Museums("Museums"),
    Coffee("Coffee"),
    Nightlife("Nightlife"),
    Outdoors("Outdoors"),

}

data class PlaceUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val distanceKm: Double?,
    val rating: Double,
    val genre: ExploreGenre,
    val isVerified: Boolean,
    val isSaved: Boolean
)