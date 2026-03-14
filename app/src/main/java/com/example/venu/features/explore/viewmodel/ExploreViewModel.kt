package com.example.venu.features.explore.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.features.explore.mappers.toPlaceUi
import com.example.venu.features.explore.model.ExploreAction
import com.example.venu.features.explore.model.ExploreUiState
import com.example.venu.features.explore.model.PlaceUi

class ExploreViewModel(
    private val eventRepository: EventRepository,
    private val listsRepository: ListsRepository
): ViewModel() {
    private val events: List<Event> = eventRepository.getTrendingEvents()
    var uiState by mutableStateOf(
        ExploreUiState(
            places = events.map { it.toPlaceUi(listsRepository) }
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
                listsRepository.toggleWantToGo(action.id)
                applyFilters()
            }
        }
    }

    private fun applyFilters() {
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

    }
}