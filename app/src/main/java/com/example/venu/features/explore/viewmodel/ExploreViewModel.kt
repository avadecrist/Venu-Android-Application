package com.example.venu.features.explore.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.core.core_common.AppGraph
import com.example.venu.features.explore.mappers.toPlaceUi
import com.example.venu.features.explore.model.ExploreAction
import com.example.venu.features.explore.model.ExploreUiState
import com.example.venu.features.explore.model.PlaceUi
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier
import java.util.UUID
import com.example.venu.features.explore.mappers.toUserCreatedEvent
import com.example.venu.features.explore.model.GooglePlaceEventDraft
import com.example.venu.core.core_data.places.GooglePlacesVenueRepository

class ExploreViewModel(
    private val eventRepository: EventRepository = AppGraph.eventRepo,
    private val listsRepository: ListsRepository = AppGraph.listsRepo,
    private val googlePlacesVenueRepository: GooglePlacesVenueRepository =
        AppGraph.googlePlacesVenueRepository
) : ViewModel() {
//    private val events: List<Event> = eventRepository.getTrendingEvents()
    private var events: List<Event> = emptyList()
    var uiState by mutableStateOf(
        ExploreUiState(
            places = emptyList(),
            availableLists = listsRepository.getAllLists(),
        )
    )
        private set

    init {
        loadInitialEvents()
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


    fun onAction(action: ExploreAction) {
        when (action) {
            is ExploreAction.QueryChanged -> {
                uiState = uiState.copy(query = action.text)
                applyFilters()
            }

            is ExploreAction.GenreSelected -> {
                uiState = uiState.copy(selectedGenre = action.genre)
                applyFilters()
            }

            is ExploreAction.PlaceClicked -> {
                uiState = uiState.copy(selectedPlaceId = action.id)
            }

            is ExploreAction.ToggleWantToGo -> {
                listsRepository.toggleWantToGo(action.id) // adds to 'want' val in InMemoryListsRepository
                applyFilters()
            }

            is ExploreAction.SaveClicked -> {
                uiState = uiState.copy(
                    showSaveSheet = true,
                    pendingSaveEventId = action.id,
                    availableLists = listsRepository.getAllLists()
                )
            }

            is ExploreAction.SaveToList -> {
                listsRepository.addToList(action.listType, action.eventId)

                uiState = uiState.copy(
                    showSaveSheet = false,
                    pendingSaveEventId = null,
                    availableLists = listsRepository.getAllLists()
                )

                applyFilters()
            }

            ExploreAction.PlaceDetailsDismissed -> {
                uiState = uiState.copy(
                    selectedPlaceId = null
                )
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
            val newEvent = draft.toUserCreatedEvent()

            eventRepository.createEvent(newEvent)

            events = eventRepository.getTrendingEvents()

            uiState = uiState.copy(
                places = buildPlaces(),
                availableLists = listsRepository.getAllLists(),
                selectedPlaceId = newEvent.id
            )
        }
    }

    fun createUserEventFromGooglePlaceId(
        placeId: String,
        eventName: String,
        eventSubtitle: String,
        genre: Genre,
        startTimeLabel: String,
        priceTier: PriceTier
    ) {
        viewModelScope.launch {
            try {
                val venue = googlePlacesVenueRepository.getVenueByPlaceId(placeId)

                val draft = GooglePlaceEventDraft(
                    eventName = eventName,
                    eventSubtitle = eventSubtitle,
                    genre = genre,
                    startTimeLabel = startTimeLabel,
                    googlePlaceId = venue.googlePlaceId,
                    venueName = venue.venueName,
                    googlePlaceAddress = venue.venueAddress,
                    latitude = venue.latitude,
                    longitude = venue.longitude,
                    imageUrl = venue.photoUrl,
                    priceTier = priceTier
                )

                val newEvent = draft.toUserCreatedEvent()
                eventRepository.createEvent(newEvent)

                events = eventRepository.getTrendingEvents()

                uiState = uiState.copy(
                    places = buildPlaces(),
                    availableLists = listsRepository.getAllLists(),
                    selectedPlaceId = newEvent.id
                )

                println(
                    "VENU PLACES DEBUG: Created event from Google Place ID=${venue.googlePlaceId}, " +
                            "venue=${venue.venueName}, lat=${venue.latitude}, lng=${venue.longitude}"
                )
            } catch (error: Exception) {
                println("VENU PLACES DEBUG: Failed to create event from placeId=$placeId. ${error.message}")
            }
        }
    }

    fun createDebugUserEvent() {
        viewModelScope.launch {
            val event = Event(
                id = UUID.randomUUID().toString(),
                name = "Debug User Event",
                subtitle = "Created from repository write path",
                genre = Genre.STUDY,
                locationName = "UC3M Leganés",
                latitude = 40.3318,
                longitude = -3.7676,
                distanceKm = null,
                priceTier = PriceTier.FREE,
                startTimeLabel = "Today",
                imageUrl = null,
                credibilityScore = 0,
                reviewCount = 0,
                isVerifiedVenue = false,
                averageRating = 0.0
            )

            eventRepository.createEvent(event)

            events = eventRepository.getTrendingEvents()
            uiState = uiState.copy(
                places = buildPlaces(),
                availableLists = listsRepository.getAllLists()
            )
        }
    }
    private fun buildPlaces(): List<PlaceUi> {
        return events.map { it.toPlaceUi(listsRepository) }
    }

    private fun applyFilters() {
        val q = uiState.query.trim().lowercase()
        val g = uiState.selectedGenre

        val allPlaces = buildPlaces()

        val filtered = allPlaces.filter { place ->
            val matchesQuery =
                q.isBlank() ||
                        place.name.lowercase().contains(q) ||
                        place.subtitle.lowercase().contains(q)

            val matchesGenre = g == null || place.genre == g

            matchesQuery && matchesGenre
        }

        val selectedStillVisible =
            uiState.selectedPlaceId?.let { id -> filtered.any { it.id == id } } == true

        uiState = uiState.copy(
            places = filtered,
            selectedPlaceId = if (selectedStillVisible) uiState.selectedPlaceId else null,
            availableLists = listsRepository.getAllLists()
        )
    }
}