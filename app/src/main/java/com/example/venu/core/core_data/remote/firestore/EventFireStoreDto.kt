package com.example.venu.core.core_data.remote.firestore

data class EventFirestoreDto(
    val id: String = "",
    val eventName: String = "",
    val description: String = "",
    val genre: String = "",
    val locationName: String = "",

    val googlePlaceId: String? = null,
    val googlePlaceAddress: String? = null,
    val googleRating: Double? = null,

    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

    val priceTier: String = "",
    val hours: String = "",
    val imageUrl: String? = null,
    val isVerifiedVenue: Boolean = false,

    val venuRating: Double = 0.0,
    val reviewCount: Int = 0,
    val interestLevel: Int = 0,

    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)