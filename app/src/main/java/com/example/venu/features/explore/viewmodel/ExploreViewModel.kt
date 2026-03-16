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

class ExploreViewModel(
    private val eventRepository: EventRepository = AppGraph.eventRepo,
    private val listsRepository: ListsRepository = AppGraph.listsRepo

): ViewModel() {
    private val events: List<Event> = eventRepository.getTrendingEvents()
    var uiState by mutableStateOf(
        ExploreUiState(
            places = buildPlaces(),
            availableLists = listsRepository.getAllLists(),
        )
    )
        private set

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

    /*private fun applyFilters() {
        val q = uiState.query.trim().lowercase()
        val g = uiState.selectedGenre

        val allPlaces: List<PlaceUi> = events.map { it.toPlaceUi(listsRepository) }

        val filtered: List<PlaceUi> = allPlaces.filter { p ->
            val matchesQuery =
                q.isBlank() ||
                        p.name.lowercase().contains(q) ||
                        p.subtitle.lowercase().contains(q)

            val matchesGenre = g == null || p.genre == g

            matchesQuery && matchesGenre
        }

        val selectedStillVisible =
            uiState.selectedPlaceId?.let { id -> filtered.any { it.id == id } } == true

        uiState = uiState.copy(
            places = filtered,
            selectedPlaceId = if (selectedStillVisible) uiState.selectedPlaceId else null
        )

    } */
}