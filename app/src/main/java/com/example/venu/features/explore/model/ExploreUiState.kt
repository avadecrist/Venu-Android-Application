package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_presentation.EventDetailsUi
import com.google.maps.model.DirectionsRoute

data class ExploreUiState(
    val query: String = "",
    val selectedGenre: Genre? = null,
    val places: List<PlaceUi> = emptyList(),

    val showSaveSheet: Boolean = false,
    val pendingSaveEventId: String? = null,
    val availableLists: List<ListType> = emptyList(),

    // For highlighting a selected marker/card
    val selectedPlaceId: String? = null,

    // For receiving event from Home -> Explore
    val shouldStartDirections: Boolean = false,

    // Google maps directions
    val directionsDestination: EventDetailsUi? = null,
    val directionsRoute: DirectionsRoute? = null,
    val isLoadingDirections: Boolean = false,
    val directionsError: String? = null,

    val googlePlaceSuggestions: List<GooglePlaceSuggestionUi> = emptyList(),
    val isSearchingGooglePlaces: Boolean = false,
    val isCreatingGooglePlaceEvent: Boolean = false,
    val googlePlacesError: String? = null,

    // Temporary Google Place preview shown on the map before the user creates an event.
    val selectedGooglePlacePreview: GooglePlaceEventDraft? = null,

    // Editable event draft shown only after the user presses "Create event".
    val pendingGooglePlaceDraft: GooglePlaceEventDraft? = null
)