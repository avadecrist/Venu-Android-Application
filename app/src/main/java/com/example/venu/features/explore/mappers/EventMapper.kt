package com.example.venu.features.explore.mappers



import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.features.explore.model.PlaceUi
import com.example.venu.core.core_domain.repository.ListType

fun Event.toPlaceUi(listsRepository: ListsRepository): PlaceUi {
    val listsContainingEvent = listsRepository
        .getAllLists()
        .filter { listType -> listsRepository.isInList(listType, id) }

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

    return PlaceUi(
        id = id,
        name = name,
        subtitle = subtitle,
        distanceKm = distanceKm,
        rating = averageRating,
        genre = genre.toExploreGenre(),
        isVerified = isVerifiedVenue,
        isSaved = listsRepository.isSaved(id),
        savedLabel = savedLabel
    )
}