package com.example.venu.features.home.mappers

import com.example.venu.core.core_common.util.formatDistance
import com.example.venu.core.core_common.util.toCrowdLevel
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.label
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.core.core_domain.repository.ReviewRepository
import com.example.venu.features.home.model.HomeVenueUi
import kotlin.math.round

suspend fun Event.toHomeVenueUi(
    reviewRepo: ReviewRepository,
    listsRepo: ListsRepository
): HomeVenueUi {
    val summary = reviewRepo.getRatingSummary(id)

    return HomeVenueUi(
        id = id,
        title = eventName,
        subtitle = "$locationName • $hours",
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        ratingLabel = if (summary.count > 0) {
            "★ ${summary.average.roundTo1Decimal()}"
        } else {
            null
        },
        googleRating = googleRating,
        distanceLabel = distanceKm?.let { distance ->
            formatDistance(distance)
        },
        startTimeLabel = hours,
        genre = genre,
        priceText = priceTier.label,
        isSaved = listsRepo.isInList(ListType.WantToGo, id),
        imageUrl = imageUrl,
        reviewCount = reviewCount,
        attendeeCount = interestLevel,
        crowdLevel = interestLevel.toCrowdLevel()
    )
}

private fun Double.roundTo1Decimal(): Double {
    return round(this * 10) / 10.0
}