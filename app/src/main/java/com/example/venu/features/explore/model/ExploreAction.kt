package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.repository.ListType

sealed interface ExploreAction {

    data class QueryChanged(val text: String) : ExploreAction

    data class GenreSelected(val genre: Genre?) : ExploreAction

    data class PlaceClicked(val id: String) : ExploreAction

    data class ToggleWantToGo(val id: String) : ExploreAction

    data class SaveClicked(val id: String) : ExploreAction

    data class SaveToList(
        val eventId: String,
        val listType: ListType
    ) : ExploreAction

    data class GooglePlaceSuggestionClicked(
        val placeId: String
    ) : ExploreAction

    data object GooglePlacesErrorDismissed : ExploreAction

    data object PlaceDetailsDismissed : ExploreAction
}