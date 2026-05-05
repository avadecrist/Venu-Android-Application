package com.example.venu.core.core_data.mapper

import com.example.venu.core.core_data.local.db.entity.EventEntity
import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier

fun Event.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        name = name,
        subtitle = subtitle,
        genre = genre.name,
        locationName = locationName,
        googlePlaceId = googlePlaceId,
        googlePlaceAddress = googlePlaceAddress,
        latitude = latitude,
        longitude = longitude,
        distanceKm = distanceKm,
        priceTier = priceTier.name,
        startTimeLabel = startTimeLabel,
        imageUrl = imageUrl,
        credibilityScore = credibilityScore,
        reviewCount = reviewCount,
        isVerifiedVenue = isVerifiedVenue,
        averageRating = averageRating,
    )
}

fun EventEntity.toDomain(): Event {
    return Event(
        id = id,
        name = name,
        subtitle = subtitle,
        genre = Genre.valueOf(genre),
        locationName = locationName,
        googlePlaceId = googlePlaceId,
        googlePlaceAddress = googlePlaceAddress,
        latitude = latitude,
        longitude = longitude,
        distanceKm = distanceKm,
        priceTier = priceTier.toPriceTier(),
        startTimeLabel = startTimeLabel,
        imageUrl = imageUrl,
        credibilityScore = credibilityScore,
        reviewCount = reviewCount,
        isVerifiedVenue = isVerifiedVenue,
        averageRating = averageRating,

        // Still temporary until attendee/crowd data is added to Room
        attendeeCount = 0,
        crowdLevel = CrowdLevel.UNKNOWN
    )
}

private fun String.toPriceTier(): PriceTier {
    return when (this) {
        // Current values
        "FREE" -> PriceTier.FREE
        "ONE" -> PriceTier.ONE
        "TWO" -> PriceTier.TWO
        "THREE" -> PriceTier.THREE
        "FOUR" -> PriceTier.FOUR
        "UNKNOWN" -> PriceTier.UNKNOWN

        // Legacy Room values from old enum
        "UNDER_10" -> PriceTier.ONE
        "UNDER_20" -> PriceTier.TWO

        else -> PriceTier.UNKNOWN
    }
}