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
        hours: String = "User-created event",
        priceTier: PriceTier = PriceTier.FREE,
        googleRating: Double? = null
    ): Event {
        return Event(
            id = UUID.randomUUID().toString(),
            eventName = eventName.ifBlank { placeName },
            description = description.ifBlank { placeAddress ?: placeName },
            genre = genre,
            locationName = placeName,

            googlePlaceId = placeId,
            googlePlaceAddress = placeAddress,
            googleRating = googleRating,

            latitude = latitude,
            longitude = longitude,
            distanceKm = null,

            priceTier = priceTier,
            hours = hours,
            imageUrl = imageUrl,

            venuRating = 0.0,
            reviewCount = 0,
            isVerifiedVenue = true,
            interestLevel = 0
        )
    }
}