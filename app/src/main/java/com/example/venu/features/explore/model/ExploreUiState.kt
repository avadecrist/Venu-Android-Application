package com.example.venu.features.explore.model

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_presentation.PlaceUi

data class ExploreUiState(
    val query: String = "",
    val selectedGenre: Genre? = null,
    val places: List<PlaceUi> = emptyList(),
    val selectedPlaceId: String? = null,
    val showSaveSheet: Boolean = false,
    val pendingSaveEventId: String? = null,
    val availableLists: List<ListType> = emptyList(),
)