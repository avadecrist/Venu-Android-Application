package com.example.venu.features.explore.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.venu.core.core_common.AppGraph
import com.example.venu.core.core_data.location.DirectionsService
import com.example.venu.core.core_data.places.GooglePlacesRepository
import com.example.venu.core.core_data.remote.firestore.EventFirestoreRepository
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.core.core_presentation.EventDetailsUi
import com.example.venu.features.explore.mappers.toEventDraft
import com.example.venu.features.explore.mappers.toPlaceUi
import com.example.venu.features.explore.mappers.toUi
import com.example.venu.features.explore.mappers.toUserCreatedEvent
import com.example.venu.features.explore.model.ExploreAction
import com.example.venu.features.explore.model.ExploreUiState
import com.example.venu.features.explore.model.GooglePlaceEventDraft
import com.example.venu.features.explore.model.PlaceUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val eventRepository: EventRepository = AppGraph.eventRepo,
    private val listsRepository: ListsRepository = AppGraph.listsRepo,
    private val googlePlacesRepository: GooglePlacesRepository = AppGraph.googlePlacesRepo,
    private val eventFirestoreRepository: EventFirestoreRepository = AppGraph.eventFirestoreRepo
) : ViewModel() {

    private var events: List<Event> = emptyList()

    private val directionsService = DirectionsService()

    private var googlePlacesSearchJob: Job? = null

    var uiState by mutableStateOf(
        ExploreUiState(
            places = emptyList(),
            availableLists = emptyList()
        )
    )
        private set

    init {
        loadInitialEvents()
    }

    fun onAction(action: ExploreAction) {
        when (action) {
            is ExploreAction.QueryChanged -> {
                uiState = uiState.copy(query = action.text)
                applyFilters()
                searchGooglePlaces(action.text)
            }

            is ExploreAction.GenreSelected -> {
                uiState = uiState.copy(selectedGenre = action.genre)
                applyFilters()
            }

            is ExploreAction.PlaceClicked -> {
                uiState = uiState.copy(selectedPlaceId = action.id)
            }

            is ExploreAction.ToggleWantToGo -> {
                viewModelScope.launch {
                    listsRepository.toggleWantToGo(action.id)
                    applyFilters()
                }
            }

            is ExploreAction.SaveClicked -> {
                viewModelScope.launch {
                    uiState = uiState.copy(
                        showSaveSheet = true,
                        pendingSaveEventId = action.id,
                        availableLists = listsRepository.getAllLists()
                    )
                }
            }

            is ExploreAction.SaveToList -> {
                saveToList(
                    eventId = action.eventId,
                    listType = action.listType
                )
            }

            is ExploreAction.GooglePlaceSuggestionClicked -> {
                previewGooglePlace(action.placeId)
            }

            is ExploreAction.GooglePlaceCreateClicked -> {
                uiState = uiState.copy(
                    selectedGooglePlacePreview = null,
                    pendingGooglePlaceDraft = action.draft
                )
            }

            is ExploreAction.GooglePlaceDraftChanged -> {
                uiState = uiState.copy(
                    pendingGooglePlaceDraft = action.draft
                )
            }

            is ExploreAction.GooglePlaceCreateConfirmed -> {
                createUserEventFromGooglePlace(action.draft)
            }

            ExploreAction.GooglePlacePreviewDismissed -> {
                uiState = uiState.copy(
                    selectedGooglePlacePreview = null
                )
            }

            ExploreAction.GooglePlaceDraftDismissed -> {
                uiState = uiState.copy(
                    pendingGooglePlaceDraft = null,
                    isCreatingGooglePlaceEvent = false
                )
            }

            is ExploreAction.GooglePlacesErrorDismissed -> {
                uiState = uiState.copy(googlePlacesError = null)
            }

            is ExploreAction.GetDirectionsClicked -> {
                Log.d("DirectionsDebug", "GetDirectionsClicked received")

                getDirections(
                    event = action.event,
                    userLat = action.userLat,
                    userLng = action.userLng
                )

                uiState = uiState.copy(
                    shouldStartDirections = false
                )
            }

            is ExploreAction.ClearDirections -> {
                uiState = uiState.copy(
                    directionsDestination = null,
                    directionsRoute = null,
                    directionsError = null
                )
            }

            ExploreAction.PlaceDetailsDismissed -> {
                uiState = uiState.copy(selectedPlaceId = null)
            }
        }
    }

    fun dismissSaveSheet() {
        uiState = uiState.copy(
            showSaveSheet = false,
            pendingSaveEventId = null
        )
    }

    fun createUserEventFromGooglePlace(draft: GooglePlaceEventDraft) {
        viewModelScope.launch {
            uiState = uiState.copy(
                isCreatingGooglePlaceEvent = true,
                googlePlacesError = null
            )

            runCatching {
                val newEvent = draft.toUserCreatedEvent()

                // Keep local Room working for current screens.
                eventRepository.createEvent(newEvent)

                // Persist the standardized event globally in Firestore.
                eventFirestoreRepository.createEvent(newEvent)

                newEvent
            }.onSuccess { newEvent ->
                events = eventRepository.getTrendingEvents()

                uiState = uiState.copy(
                    query = "",
                    places = buildPlaces(),
                    availableLists = listsRepository.getAllLists(),
                    selectedPlaceId = newEvent.id,
                    googlePlaceSuggestions = emptyList(),
                    selectedGooglePlacePreview = null,
                    pendingGooglePlaceDraft = null,
                    isCreatingGooglePlaceEvent = false
                )
            }.onFailure { error ->
                uiState = uiState.copy(
                    isCreatingGooglePlaceEvent = false,
                    googlePlacesError = error.message ?: "Unable to create event from Google Place."
                )
            }
        }
    }

    private fun loadInitialEvents() {
        viewModelScope.launch {
            events = eventRepository.getTrendingEvents()

            uiState = uiState.copy(
                places = buildPlaces(),
                availableLists = listsRepository.getAllLists()
            )
        }
    }

    private fun searchGooglePlaces(query: String) {
        googlePlacesSearchJob?.cancel()

        val trimmedQuery = query.trim()

        if (trimmedQuery.length < MIN_GOOGLE_PLACES_QUERY_LENGTH) {
            uiState = uiState.copy(
                googlePlaceSuggestions = emptyList(),
                isSearchingGooglePlaces = false,
                googlePlacesError = null
            )
            return
        }

        googlePlacesSearchJob = viewModelScope.launch {
            delay(GOOGLE_PLACES_SEARCH_DEBOUNCE_MS)

            uiState = uiState.copy(
                isSearchingGooglePlaces = true,
                googlePlacesError = null
            )

            runCatching {
                googlePlacesRepository.searchPlaces(trimmedQuery).map { suggestion ->
                    suggestion.toUi()
                }
            }.onSuccess { suggestions ->
                uiState = uiState.copy(
                    googlePlaceSuggestions = suggestions,
                    isSearchingGooglePlaces = false
                )
            }.onFailure { error ->
                uiState = uiState.copy(
                    googlePlaceSuggestions = emptyList(),
                    isSearchingGooglePlaces = false,
                    googlePlacesError = error.message ?: "Unable to search Google Places."
                )
            }
        }
    }

    private fun previewGooglePlace(placeId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(
                selectedPlaceId = null,
                selectedGooglePlacePreview = null,
                pendingGooglePlaceDraft = null,
                isCreatingGooglePlaceEvent = true,
                googlePlacesError = null
            )

            runCatching {
                googlePlacesRepository.getPlaceDetails(placeId)
            }.onSuccess { place ->
                if (place == null) {
                    uiState = uiState.copy(
                        isCreatingGooglePlaceEvent = false,
                        googlePlacesError = "Unable to load place details."
                    )
                    return@onSuccess
                }

                val draft = place.toEventDraft()

                uiState = uiState.copy(
                    selectedPlaceId = null,
                    selectedGooglePlacePreview = draft,
                    pendingGooglePlaceDraft = null,
                    googlePlaceSuggestions = emptyList(),
                    isCreatingGooglePlaceEvent = false
                )
            }.onFailure { error ->
                uiState = uiState.copy(
                    selectedGooglePlacePreview = null,
                    isCreatingGooglePlaceEvent = false,
                    googlePlacesError = error.message ?: "Unable to load place details."
                )
            }
        }
    }

    private fun saveToList(
        eventId: String,
        listType: ListType
    ) {
        viewModelScope.launch {
            listsRepository.addToList(
                type = listType,
                eventId = eventId
            )

            uiState = uiState.copy(
                showSaveSheet = false,
                pendingSaveEventId = null,
                availableLists = listsRepository.getAllLists()
            )

            applyFilters()
        }
    }

    private suspend fun buildPlaces(): List<PlaceUi> {
        return events.map { event ->
            event.toPlaceUi(listsRepository)
        }
    }

    private fun applyFilters() {
        viewModelScope.launch {
            val q = uiState.query.trim().lowercase()
            val g = uiState.selectedGenre
            val allPlaces = buildPlaces()

            val filtered = allPlaces.filter { place ->
                val matchesQuery = q.isBlank() ||
                        place.name.lowercase().contains(q) ||
                        place.subtitle.lowercase().contains(q)

                val matchesGenre = g == null || place.genre == g

                matchesQuery && matchesGenre
            }

            val selectedStillVisible = uiState.selectedPlaceId?.let { id ->
                filtered.any { place -> place.id == id }
            } == true

            uiState = uiState.copy(
                places = filtered,
                selectedPlaceId = if (selectedStillVisible) {
                    uiState.selectedPlaceId
                } else {
                    null
                },
                availableLists = listsRepository.getAllLists()
            )
        }
    }

    fun getDirections(
        event: EventDetailsUi,
        userLat: Double,
        userLng: Double
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(
                directionsDestination = event,
                isLoadingDirections = true,
                directionsError = null
            )

            try {
                Log.d("DirectionsDebug", "Fetching route...")

                val route = directionsService.getDrivingRoute(
                    originLat = userLat,
                    originLng = userLng,
                    destinationLat = event.latitude,
                    destinationLng = event.longitude
                )

                Log.d(
                    "DirectionsDebug",
                    "Route received. Polyline null? ${route.overviewPolyline == null}"
                )

                uiState = uiState.copy(
                    directionsRoute = route,
                    isLoadingDirections = false
                )
            } catch (e: Exception) {
                Log.e("DirectionsDebug", "Directions failed", e)

                uiState = uiState.copy(
                    directionsRoute = null,
                    isLoadingDirections = false,
                    directionsError = e.message ?: "Could not load directions"
                )
            }
        }
    }

    fun openFromHomeForDirections(eventId: String) {
        uiState = uiState.copy(
            selectedPlaceId = eventId,
            shouldStartDirections = true
        )
    }

    companion object {
        private const val MIN_GOOGLE_PLACES_QUERY_LENGTH = 3
        private const val GOOGLE_PLACES_SEARCH_DEBOUNCE_MS = 350L
    }
}