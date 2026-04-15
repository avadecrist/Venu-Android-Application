package com.example.venu.features.lists.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.venu.core.core_common.AppGraph
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.features.lists.model.ListsUiEvent
import com.example.venu.features.lists.model.ListsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListsViewModel(): ViewModel() {
    private val listsRepo = AppGraph.listsRepo
    private val _state = MutableStateFlow(ListsUiState())
    val state = _state.asStateFlow()

    init {
        refresh(ListType.WantToGo)
    }


    fun onEvent(event: ListsUiEvent) {
        when (event) {

            is ListsUiEvent.SelectTab -> {
                if (event.tab != _state.value.selectedTab) {
                    refresh(event.tab) // only reloads if tab changed
                }
            }

            is ListsUiEvent.ToggleWantToGo -> {
                listsRepo.toggleWantToGo(event.eventId)
                refresh()
            }

            is ListsUiEvent.RemoveFromList -> {
                listsRepo.removeFromList(event.tab, event.eventId)
                refresh(event.tab)
            }

            is ListsUiEvent.MoveEvent -> {
                listsRepo.moveEvent(event.eventId, event.from, event.to)
                refresh(event.to)
            }

            is ListsUiEvent.CreateCustomList -> {
                if (event.name.isNotBlank()) {
                    val newList = listsRepo.createCustomList(event.name.trim())

                    refresh(newList)
                }
                return
            }
        }
    }

    private fun refresh(selected: ListType = _state.value.selectedTab) {

        viewModelScope.launch {
            val tabs = listsRepo.getAllLists()

            val safeTab = tabs.find { tab ->
                when {
                    tab is ListType.Custom && selected is ListType.Custom ->
                        tab.id == selected.id

                    else -> tab == selected
                }
            } ?: tabs.first()

            _state.value = _state.value.copy(
                tabs = tabs,
                selectedTab = safeTab,
                events = listsRepo.getList(safeTab)
            )

        }
    }
}