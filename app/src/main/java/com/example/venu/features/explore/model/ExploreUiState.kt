package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.repository.ListType

data class ExploreUiState(
    val query: String = "",
    val selectedGenre: ExploreGenre? = null,
    val places: List<PlaceUi> = emptyList(),
    val selectedPlaceId: String? = null,
    val showSaveSheet: Boolean = false,
    val pendingSaveEventId: String? = null,
    val availableLists: List<ListType> = emptyList(),
)