package com.example.venu.features.explore.mappers

import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.core.core_domain.model.Event
import com.example.venu.features.explore.model.GooglePlaceEventDraft
import java.util.UUID

fun GooglePlaceEventDraft.toUserCreatedEvent(): Event {
    return Event(
        id = "user-${UUID.randomUUID()}",
        name = location.trim(),
        subtitle = description.trim(),
        genre = category,
        locationName = location.trim(),
        googlePlaceId = googlePlaceId,
        googlePlaceAddress = address.trim().ifBlank { null },
        latitude = latitude,
        longitude = longitude,
        distanceKm = null,
        priceTier = priceTier,
        startTimeLabel = hours?.trim().orEmpty(),
        imageUrl = imageUrl,
        credibilityScore = 0,
        reviewCount = 0,
        isVerifiedVenue = true,
        averageRating = googleRating ?: 0.0,
        attendeeCount = interestLevel,
        crowdLevel = CrowdLevel.UNKNOWN
    )
}