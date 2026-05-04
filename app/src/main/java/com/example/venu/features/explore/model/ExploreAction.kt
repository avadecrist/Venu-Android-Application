package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_presentation.EventDetailsUi

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

    data class GooglePlaceCreateClicked(
        val draft: GooglePlaceEventDraft
    ) : ExploreAction

    data class GooglePlaceDraftChanged(
        val draft: GooglePlaceEventDraft
    ) : ExploreAction

    data class GooglePlaceCreateConfirmed(
        val draft: GooglePlaceEventDraft
    ) : ExploreAction

    data object GooglePlacePreviewDismissed : ExploreAction

    data object GooglePlaceDraftDismissed : ExploreAction

    data object GooglePlacesErrorDismissed : ExploreAction

    data object PlaceDetailsDismissed : ExploreAction

    // Google maps directions
    data class GetDirectionsClicked(
        val event: EventDetailsUi,
        val userLat: Double,
        val userLng: Double
    ) : ExploreAction

    data object ClearDirections : ExploreAction
}