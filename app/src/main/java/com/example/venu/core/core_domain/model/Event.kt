package com.example.venu.core.core_domain.model

data class Event(
    val id: String,
    val name: String,
    val subtitle: String,          // Specific Event -- i.e. "Barca v Madrid Futbol Match" or "Study Jam"
    val genre: Genre,
    val locationName: String,      // Location/Host Name -- i.e. "Dubliners" or "Bernabéu Stadium"
    val latitude: Double,
    val longitude: Double,
    val distanceKm: Double?,    // null if unknown for now
    val priceTier: PriceTier,
    val startTimeLabel: String,    // simple for now ("Tonight 8 PM")
    val imageUrl: String? = null,
    val credibilityScore: Int = 0, // placeholder 0-100
    val reviewCount: Int = 0,
    val isVerifiedVenue: Boolean = false,
    val averageRating: Double = 0.0
)

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

enum class PriceTier {
    FREE,
    UNDER_10,
    UNDER_20
}