package com.example.venu.features.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.venu.core.core_common.AppGraph
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.features.home.model.HomeAction
import com.example.venu.features.home.model.HomeUiState
import com.example.venu.features.home.model.HomeVenueUi
import kotlin.math.round

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

            is HomeAction.ToggleSaved -> {
                listsRepo.toggleWantToGo(action.eventId)
                loadHome()
            }
        }
    }

    private fun loadHome() {
        allFeatured = eventRepo
            .getTrendingEvents()
            .map { it.toHomeVenueUi() }

        allNearYou = eventRepo
            .getNearbyEvents()
            .map { it.toHomeVenueUi() }

        applyFilters()
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

    private fun Event.toHomeVenueUi(): HomeVenueUi {
        val summary = reviewRepo.getRatingSummary(id)

        return HomeVenueUi(
            id = id,
            title = name,
            subtitle = "$locationName • $startTimeLabel",
            ratingLabel = if (summary.count > 0) {
                "★ ${summary.average.roundTo1Decimal()}"
            } else {
                null
            },
            distanceLabel = distanceKm?.let {
                "${it.roundTo1Decimal()} mi"
            },
            isSaved = listsRepo.isInList(ListType.WantToGo, id)
        )
    }

    private fun Double.roundTo1Decimal(): Double {
        return round(this * 10) / 10.0
    }
}