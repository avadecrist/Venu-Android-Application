package com.example.venu.features.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExploreViewModel : ViewModel() {

    private var allPlaces: List<PlaceUi> = MockExploreData.places

    var uiState by mutableStateOf(
        ExploreUiState(
            places = allPlaces
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

            is ExploreAction.ToggleSaved -> {
                allPlaces = allPlaces.map { p ->
                    if (p.id == action.id) p.copy(isSaved = !p.isSaved) else p
                }
                applyFilters()
            }
        }
    }

    private fun applyFilters() {
        val q = uiState.query.trim().lowercase()
        val g = uiState.selectedGenre

        val filtered = allPlaces.filter { p ->
            val matchesQuery =
                q.isBlank() || p.name.lowercase().contains(q) || p.subtitle.lowercase().contains(q)
            val matchesGenre = (g == null) || (p.genre == g)
            matchesQuery && matchesGenre
        }

        // keep selection if still visible, otherwise clear
        val selectedStillVisible = uiState.selectedPlaceId?.let { id -> filtered.any { it.id == id } } == true

        uiState = uiState.copy(
            places = filtered,
            selectedPlaceId = if (selectedStillVisible) uiState.selectedPlaceId else null
        )
    }
}