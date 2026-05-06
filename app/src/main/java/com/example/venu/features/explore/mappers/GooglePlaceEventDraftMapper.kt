package com.example.venu.features.explore.mappers

import com.example.venu.core.core_domain.model.Event
import com.example.venu.features.explore.model.GooglePlaceEventDraft
import java.util.UUID

fun GooglePlaceEventDraft.toUserCreatedEvent(): Event {
    return Event(
        id = "user-${UUID.randomUUID()}",
        eventName = name.trim(),
        description = description.trim(),
        genre = category,
        locationName = location.trim(),
        googlePlaceId = googlePlaceId,
        googlePlaceAddress = address.trim().ifBlank { null },
        googleRating = googleRating,
        latitude = latitude,
        longitude = longitude,
        distanceKm = null,
        priceTier = priceTier,
        hours = hours?.trim().orEmpty(),
        imageUrl = imageUrl,
        venuRating = 0.0,
        reviewCount = 0,
        isVerifiedVenue = true,
        interestLevel = 0
    )
}