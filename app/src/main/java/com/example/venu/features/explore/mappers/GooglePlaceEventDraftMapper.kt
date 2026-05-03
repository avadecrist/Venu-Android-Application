package com.example.venu.features.explore.mappers

import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.core.core_domain.model.Event
import com.example.venu.features.explore.model.GooglePlaceEventDraft
import java.util.UUID

fun GooglePlaceEventDraft.toUserCreatedEvent(): Event {
    return Event(
        id = "user-${UUID.randomUUID()}",
        name = eventName.trim(),
        subtitle = eventSubtitle.trim(),
        genre = genre,
        locationName = venueName.trim(),
        googlePlaceId = googlePlaceId,
        googlePlaceAddress = googlePlaceAddress?.trim(),
        latitude = latitude,
        longitude = longitude,
        distanceKm = null,
        priceTier = priceTier,
        startTimeLabel = startTimeLabel.trim(),
        imageUrl = imageUrl,
        credibilityScore = 0,
        reviewCount = 0,
        isVerifiedVenue = true,
        averageRating = rating ?: 0.0,
        attendeeCount = 0,
        crowdLevel = CrowdLevel.UNKNOWN
    )
}