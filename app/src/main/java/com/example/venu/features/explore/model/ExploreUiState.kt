package com.example.venu.features.explore.model

data class ExploreUiState(
    val query: String = "",
    val selectedGenre: ExploreGenre? = null,
    val places: List<PlaceUi> = emptyList(),
    val selectedPlaceId: String? = null
)