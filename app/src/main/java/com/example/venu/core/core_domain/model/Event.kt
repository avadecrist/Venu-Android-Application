package com.example.venu.core.core_domain.model

data class Event(
    val id: String,

    // User-created event title shown on cards/details
    val eventName: String,

    // User-created event description
    val description: String,

    val genre: Genre,

    // Venue/place name
    val locationName: String,

    // Google Places bridge fields
    val googlePlaceId: String? = null,
    val googlePlaceAddress: String? = null,
    val googleRating: Double? = null,

    val latitude: Double,
    val longitude: Double,

    // Do not store this in Firestore. It is calculated client-side from user location.
    val distanceKm: Double? = null,

    val priceTier: PriceTier,

    // Opening hours / event hours
    val hours: String,

    val imageUrl: String? = null,

    // Venu review aggregate fields
    val venuRating: Double = 0.0,
    val reviewCount: Int = 0,

    val isVerifiedVenue: Boolean = false,

    // Calculated from users saving this event to Want to Go
    val interestLevel: Int = 0
)