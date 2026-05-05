package com.example.venu.features.home.mappers

import com.example.venu.core.core_domain.model.Event
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
        title = name,
        subtitle = "$locationName • $startTimeLabel",
        locationName = this.locationName,
        latitude = latitude,
        longitude = longitude,
        ratingLabel = if (summary.count > 0) {
            "★ ${summary.average.roundTo1Decimal()}"
        } else {
            null
        },
        distanceLabel = distanceKm?.let {
            "${it.roundTo1Decimal()} km"
        },
        genre = genre,
        isSaved = listsRepo.isInList(ListType.WantToGo, id),
        imageUrl = imageUrl
    )
}

private fun Double.roundTo1Decimal(): Double {
    return round(this * 10) / 10.0
}