package com.example.venu.features.lists.viewmodel

import androidx.lifecycle.ViewModel
import com.example.venu.core.core_common.AppGraph
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.features.lists.model.ListsUiEvent
import com.example.venu.features.lists.model.ListsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// will include: fun onEvent(event: ListsUiEvent) { ... }
class ListsViewModel : ViewModel() {

    private val listsRepo = AppGraph.listsRepo

    private val _state = MutableStateFlow(ListsUiState())
    val state = _state.asStateFlow()

    init {
        loadList(ListType.WANT_TO_GO)
    }

    fun onEvent(event: ListsUiEvent) {
        when (event) {

            is ListsUiEvent.SelectTab -> {
                if (event.tab != _state.value.selectedTab) {
                    loadList(event.tab) // only reloads if tab changed
                }
            }

            is ListsUiEvent.ToggleWantToGo -> {
                listsRepo.toggleWantToGo(event.eventId)
                if (_state.value.selectedTab == ListType.WANT_TO_GO) { // only reload if currently in Want to Go tab
                    loadList(ListType.WANT_TO_GO)
                }
            }

            is ListsUiEvent.RemoveFromList -> {
                listsRepo.removeFromList(
                    _state.value.selectedTab,
                    event.eventId
                )
                loadList(_state.value.selectedTab)
            }

            is ListsUiEvent.MoveEvent -> {
                listsRepo.moveEvent(event.eventId, event.from, event.to)
                loadList(_state.value.selectedTab)
            }
        }
    }

    private fun loadList(type: ListType) {
        val events = listsRepo.getList(type)

        _state.value = _state.value.copy(
            selectedTab = type,
            events = events
        )
    }
}

/* How UI would call this:

1)
    val viewModel: ListsViewModel = viewModel()
    val state by viewModel.state.collectAsState()

2)
    Button(
        onClick = {
            viewModel.onEvent(
                ListsUiEvent.ToggleWantToGo(event.id)
            )
        }
    )

 */