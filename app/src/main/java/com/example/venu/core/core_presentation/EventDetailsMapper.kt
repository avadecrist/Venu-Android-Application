package com.example.venu.core.core_presentation


import android.util.Log
import com.example.venu.features.explore.model.PlaceUi
import com.example.venu.features.home.model.HomeVenueUi

fun PlaceUi.toEventDetailsUi(): EventDetailsUi {
    return EventDetailsUi(
        id = id,
        name = name,
        subtitle = subtitle,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        distanceKm = distanceKm,

        genre = genre,
        priceText = priceText,
        startTimeLabel = hours,
        imageUrl = imageUrl,

        credibilityScore = if (isVerified) 85 else 60,
        /* credibilityScore = calculateCredibilityScore( // make calculateCredibilityScore helper func
            googleRating = googleRating,
            reviewCount = reviewCount,
            interestLevel = interestLevel
        ),*/

        reviewCount = reviewCount,
        isVerifiedVenue = isVerified,

        averageRating = rating,
        googleRating = googleRating,
        userRating = 0.0,

        attendeeCount = attendeeCount, //refactor to interestLevel
        crowdLevel = crowdLevel, // default Unknown value
        reviews = emptyList(),

        isSaved = isSaved || savedLabel != null
    )
}

fun HomeVenueUi.toEventDetailsUi(): EventDetailsUi {
    return EventDetailsUi(
        id = id,
        name = title,
        subtitle = subtitle,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        distanceKm = distanceLabel
            ?.removeSuffix(" km")
            ?.toDoubleOrNull(),

        genre = genre,
        priceText = priceText,
        startTimeLabel = startTimeLabel,
        imageUrl = imageUrl,

        credibilityScore = 85,
        /* credibilityScore = calculateCredibilityScore( // make calculateCredibilityScore helper func
            googleRating = googleRating,
            reviewCount = reviewCount,
            interestLevel = interestLevel
        ),*/

        reviewCount = reviewCount,
        isVerifiedVenue = false,
        averageRating = ratingLabel
            ?.removePrefix("★ ")
            ?.toDoubleOrNull(),
        googleRating = googleRating,
        userRating = 0.0,
        attendeeCount = attendeeCount, // change to interestLevel
        crowdLevel = crowdLevel,
        reviews = emptyList(),
        isSaved = isSaved
    )
}