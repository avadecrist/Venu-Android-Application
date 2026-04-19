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
//        attendeeCount = attendeeCount,
//        crowdLevel = crowdLevel.name,
    )
}

fun EventEntity.toDomain(): Event {
    return Event(
        id = id,
        name = name,
        subtitle = subtitle,
        genre = Genre.valueOf(genre),
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        distanceKm = distanceKm,
        priceTier = PriceTier.valueOf(priceTier),
        startTimeLabel = startTimeLabel,
        imageUrl = imageUrl,
        credibilityScore = credibilityScore,
        reviewCount = reviewCount,
        isVerifiedVenue = isVerifiedVenue,
        averageRating = averageRating,
        // temp fixed values
        attendeeCount = 0,
        crowdLevel = CrowdLevel.UNKNOWN // CrowdLevel.valueOf(crowdLevel),
    )
}