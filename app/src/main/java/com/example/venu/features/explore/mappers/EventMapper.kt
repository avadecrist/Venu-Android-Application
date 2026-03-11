package com.example.venu.features.explore.mappers



import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.features.explore.model.PlaceUi

fun Event.toPlaceUi(listsRepository: ListsRepository): PlaceUi {
    return PlaceUi(
        id = id,
        name = name,
        subtitle = subtitle,
        distanceKm = distanceKm,
        rating = averageRating,
        genre = genre.toExploreGenre(),
        isVerified = isVerifiedVenue,
        isSaved = listsRepository.isSaved(id)
    )
}