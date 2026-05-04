package com.example.venu.core.core_domain.model

import java.util.UUID

object UserCreatedEventFactory {

    fun createEventFromGooglePlace(
        eventName: String,
        description: String,
        genre: Genre,
        placeId: String,
        placeName: String,
        placeAddress: String?,
        latitude: Double,
        longitude: Double,
        imageUrl: String? = null,
        startTimeLabel: String = "User-created event",
        priceTier: PriceTier = PriceTier.FREE
    ): Event {
        return Event(
            id = UUID.randomUUID().toString(),
            name = eventName.ifBlank { placeName },
            subtitle = description.ifBlank { placeAddress ?: placeName },
            genre = genre,

            locationName = placeName,
            latitude = latitude,
            longitude = longitude,
            googlePlaceId = placeId,
            googlePlaceAddress = placeAddress,

            distanceKm = null,
            priceTier = priceTier,
            startTimeLabel = startTimeLabel,
            imageUrl = imageUrl,

            credibilityScore = 50,
            reviewCount = 0,
            isVerifiedVenue = true,
            averageRating = 0.0
        )
    }
}