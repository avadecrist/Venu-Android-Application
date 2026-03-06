package com.example.venu.features.lists.model

import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.ListType
/* what the 'Lists' screen observes from its ViewModel */
data class ListsUiState(
    val selectedTab: ListType = ListType.WANT_TO_GO, // which tab is active
    val events: List<Event> = emptyList(), // events for that tab
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)