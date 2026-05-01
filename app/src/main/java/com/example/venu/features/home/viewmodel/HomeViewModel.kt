package com.example.venu.features.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.venu.core.core_common.AppGraph
import com.example.venu.features.home.mappers.toHomeVenueUi
import com.example.venu.features.home.model.HomeAction
import com.example.venu.features.home.model.HomeUiState
import com.example.venu.features.home.model.HomeVenueUi
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val eventRepo = AppGraph.eventRepo
    private val reviewRepo = AppGraph.reviewRepo
    private val listsRepo = AppGraph.listsRepo

    private var allFeatured: List<HomeVenueUi> = emptyList()
    private var allNearYou: List<HomeVenueUi> = emptyList()

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadHome()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.QueryChanged -> {
                uiState = uiState.copy(query = action.text)
                applyFilters()
            }

            is HomeAction.SaveClicked -> {
                uiState = uiState.copy(
                    showSaveSheet = true,
                    pendingSaveEventId = action.eventId,
                    availableLists = listsRepo.getAllLists()
                )
            }

            is HomeAction.SaveToList -> {
                listsRepo.addToList(action.listType, action.eventId)

                uiState = uiState.copy(
                    showSaveSheet = false,
                    pendingSaveEventId = null,
                    availableLists = listsRepo.getAllLists()
                )

                loadHome() // or applyFilters()
            }

            is HomeAction.ViewOnMapClicked -> {
                // trigger navigation event (StateFlow / Channel)

            }

            is HomeAction.DismissSaveSheet -> {
                uiState = uiState.copy(
                    showSaveSheet = false,
                    pendingSaveEventId = null
                )
            }
        }
    }

    private fun loadHome() {

        viewModelScope.launch {
            allFeatured = eventRepo
                .getTrendingEvents()
                .map { event ->
                    event.toHomeVenueUi(
                        reviewRepo = reviewRepo,
                        listsRepo = listsRepo
                    )
                }

            allNearYou = eventRepo
                .getNearbyEvents()
                .map { event ->
                    event.toHomeVenueUi(
                        reviewRepo = reviewRepo,
                        listsRepo = listsRepo
                    )
                }

            applyFilters()
        }
    }

    private fun applyFilters() {
        val q = uiState.query.trim().lowercase()

        val filteredFeatured = allFeatured.filter { venue ->
            q.isBlank() ||
                    venue.title.lowercase().contains(q) ||
                    venue.subtitle.lowercase().contains(q)
        }

        val filteredNearYou = allNearYou.filter { venue ->
            q.isBlank() ||
                    venue.title.lowercase().contains(q) ||
                    venue.subtitle.lowercase().contains(q)
        }

        uiState = uiState.copy(
            featured = filteredFeatured,
            nearYou = filteredNearYou
        )
    }
}