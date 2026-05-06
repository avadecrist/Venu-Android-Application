package com.example.venu.features.explore.mappers

import android.util.Log
import com.example.venu.core.core_common.util.toCrowdLevel
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.label
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.features.explore.model.PlaceUi

suspend fun Event.toPlaceUi(listsRepository: ListsRepository): PlaceUi {
    val allLists = listsRepository.getAllLists()

    val listsContainingEvent = allLists.filter { listType ->
        listsRepository.isInList(listType, id)
    }

    val savedLabel = when (listsContainingEvent.size) {
        0 -> null

        1 -> when (val list = listsContainingEvent.first()) {
            ListType.WantToGo -> "Want to Go"
            ListType.AlreadyWent -> "Already Went"
            ListType.ToReview -> "To Review"
            is ListType.Custom -> list.name
        }

        else -> "${listsContainingEvent.size} lists"
    }
    Log.d(
        "AttendeeDebug",
        "Event ${eventName} mapped with interestLevel=$interestLevel"
    )

    return PlaceUi(
        id = id,
        name = eventName,
        subtitle = description,
        locationName = locationName,
        latitude = latitude,
        longitude = longitude,
        distanceKm = distanceKm,
        rating = venuRating,
        genre = genre,
        isVerified = isVerifiedVenue,
        isSaved = listsContainingEvent.isNotEmpty(),
        savedLabel = savedLabel,
        imageUrl = imageUrl,
        priceText = priceTier.label,
        hours = hours,
        attendeeCount = interestLevel,
        crowdLevel = interestLevel.toCrowdLevel()
    )
}