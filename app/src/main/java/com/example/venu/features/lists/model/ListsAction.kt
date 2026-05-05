package com.example.venu.features.lists.model

import com.example.venu.core.core_domain.repository.ListType

sealed interface ListsAction {
    data class SelectTab(val tab: ListType) : ListsAction
    data class RemoveFromList(val tab: ListType, val eventId: String) : ListsAction
    data class MoveEvent(val eventId: String, val from: ListType, val to: ListType) : ListsAction
    data class ToggleWantToGo(val eventId: String) : ListsAction
    data class CreateCustomList(val name: String) : ListsAction

    data object Refresh : ListsAction
}