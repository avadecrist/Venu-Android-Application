package com.example.venu.features.home.model

import com.example.venu.core.core_domain.repository.ListType

data class HomeUiState(
    val query: String = "",
    val featured: List<HomeVenueUi> = emptyList(),
    val nearYou: List<HomeVenueUi> = emptyList(),

    // mimics explore page
    val showSaveSheet: Boolean = false,
    val pendingSaveEventId: String? = null,
    val availableLists: List<ListType> = emptyList()
)