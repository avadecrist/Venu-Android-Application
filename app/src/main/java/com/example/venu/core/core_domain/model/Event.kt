package com.example.venu.core.core_domain.model

data class Event(
    val id: String,

    // User-facing event/place title
    val name: String,

    // Specific event/activity happening at the venue
    val subtitle: String,

    val genre: Genre,

    // Current venue/location name
    val locationName: String,

    // Google Places bridge fields
    val googlePlaceId: String? = null,
    val googlePlaceAddress: String? = null,

    val latitude: Double,
    val longitude: Double,

    val distanceKm: Double?,

    val priceTier: PriceTier,

    val startTimeLabel: String,

    // Future source: Google Places photo URL or cached photo URL
    val imageUrl: String? = null,

    // Venu-specific trust/review fields
    val credibilityScore: Int = 0,
    val reviewCount: Int = 0,
    val isVerifiedVenue: Boolean = false,
    val averageRating: Double = 0.0,

    val attendeeCount: Int = 0,
    val crowdLevel: CrowdLevel = CrowdLevel.UNKNOWN
)

