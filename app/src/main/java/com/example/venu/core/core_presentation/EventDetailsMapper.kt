package com.example.venu.core.core_presentation


import android.util.Log
import com.example.venu.features.explore.model.PlaceUi
import com.example.venu.features.home.model.HomeVenueUi

fun PlaceUi.toEventDetailsUi(): EventDetailsUi {
    Log.d(
        "AttendeeDebug",
        "PlaceUi ${name} -> EventDetailsUi attendeeCount=$attendeeCount"
    )
    return EventDetailsUi(
        id = id,
        name = name,
        subtitle = subtitle,
        genre = genre,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        distanceKm = distanceKm,
        priceText = priceText,
        startTimeLabel = hours,
        imageUrl = imageUrl,
        credibilityScore = if (isVerified) 85 else 60,
        reviewCount = 0,
        isVerifiedVenue = isVerified,
        averageRating = rating,
        googleRating = rating,
        userRating = 0.0,
        attendeeCount = attendeeCount,
        crowdLevel = crowdLevel,
        reviews = emptyList(),
        isSaved = isSaved || savedLabel != null
    )
}

fun HomeVenueUi.toEventDetailsUi(): EventDetailsUi {
    return EventDetailsUi(
        id = id,
        name = title,
        subtitle = subtitle,
        genre = genre,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        distanceKm = distanceLabel
            ?.removeSuffix(" km")
            ?.toDoubleOrNull(),
        priceText = "$$",
        startTimeLabel = "Today",
        imageUrl = imageUrl,
        credibilityScore = 85,
        reviewCount = 0,
        isVerifiedVenue = false,
        averageRating = ratingLabel
            ?.removePrefix("★ ")
            ?.toDoubleOrNull(),
        googleRating = null,
        userRating = null,
        attendeeCount = attendeeCount,
        crowdLevel = crowdLevel,
        reviews = emptyList(),
        isSaved = isSaved
    )
}