package com.example.venu.features.explore.model

sealed interface ExploreAction {
    data class QueryChanged(val text: String) : ExploreAction
    data class GenreSelected(val genre: ExploreGenre?) : ExploreAction // null = "All"
    data class PlaceClicked(val id: String) : ExploreAction
    data class ToggleWantToGo(val id: String) : ExploreAction
}