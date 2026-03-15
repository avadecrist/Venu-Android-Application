package com.example.venu.features.lists.model

import com.example.venu.core.core_domain.repository.ListType

sealed interface ListsUiEvent {
    data class SelectTab(val tab: ListType) : ListsUiEvent
    data class RemoveFromList(val tab: ListType, val eventId: String) : ListsUiEvent
    data class MoveEvent(val eventId: String, val from: ListType, val to: ListType) : ListsUiEvent
    data class ToggleWantToGo(val eventId: String) : ListsUiEvent
    data class CreateCustomList(val name: String) : ListsUiEvent
}