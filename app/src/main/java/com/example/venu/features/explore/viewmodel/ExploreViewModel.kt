package com.example.venu.features.explore.viewmodel

import android.util.Log
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
import com.example.venu.core.core_data.location.DirectionsService
import com.example.venu.core.core_presentation.EventDetailsUi
import kotlinx.coroutines.launch

class ExploreViewModel(
    private val eventRepository: EventRepository = AppGraph.eventRepo,
    private val listsRepository: ListsRepository = AppGraph.listsRepo

): ViewModel() {
    private var events: List<Event> = emptyList()

    private val directionsService = DirectionsService()
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

            is ExploreAction.GetDirectionsClicked -> {
                Log.d("DirectionsDebug", "GetDirectionsClicked received")
                getDirections(
                    event = action.event,
                    userLat = action.userLat,
                    userLng = action.userLng
                )
            }

            ExploreAction.ClearDirections -> {
                uiState = uiState.copy(
                    directionsDestination = null,
                    directionsRoute = null,
                    directionsError = null
                )
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

    private fun getDirections(
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
}