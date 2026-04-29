package com.example.venu.core.core_presentation

import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.features.explore.model.PlaceUi
import com.example.venu.features.home.model.HomeVenueUi

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

fun HomeVenueUi.toEventDetailsUi(): EventDetailsUi {
    return EventDetailsUi(
        id = id,
        name = title,
        subtitle = subtitle,
        locationName = title,
        distanceKm = distanceLabel
            ?.removeSuffix(" km")
            ?.toDoubleOrNull(),
        averageRating = ratingLabel
            ?.removePrefix("★ ")
            ?.toDoubleOrNull(),
        isSaved = isSaved,

        // Temporary defaults until HomeVenueUi has richer data
        genre = Genre.FOOD,
        startTimeLabel = "Today",
        priceText = "$$",
        isVerifiedVenue = false,
        credibilityScore = 85,
        googleRating = null,
        userRating = null,
        reviewCount = 0,
        attendeeCount = 0,
        crowdLevel = CrowdLevel.QUIET,
        reviews = emptyList()
    )
}