package com.example.venu.features.explore

sealed interface ExploreAction {
    data class QueryChanged(val text: String) : ExploreAction
    data class GenreSelected(val genre: ExploreGenre?) : ExploreAction // null = "All"
    data class PlaceClicked(val id: String) : ExploreAction
    data class ToggleSaved(val id: String) : ExploreAction
}
