package com.example.venu.core.core_data.mapper

import com.example.venu.core.core_common.EventDetailsUi
import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.features.explore.model.PlaceUi

fun PlaceUi.toEventDetailsUi(): EventDetailsUi {
    return EventDetailsUi(
        id = id,
        name = name,
        subtitle = subtitle,
        genre = genre,
        locationName = name,
        distanceKm = distanceKm,
        priceText = "$",
        startTimeLabel = "Open now",
        imageUrl = null,
        credibilityScore = 85,
        reviewCount = 0,
        isVerifiedVenue = isVerified,
        averageRating = rating,
        googleRating = rating,
        userRating = rating,
        attendeeCount = 0,
        crowdLevel = CrowdLevel.UNKNOWN, //temporary or default value
        reviews = emptyList(),
        isSaved = isSaved || savedLabel != null
    )
}