package com.example.venu.features.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.components.BaseEventCard
import com.example.venu.core.core_common.eventdetails.EventDetailsSheet
import com.example.venu.core.core_presentation.EventDetailsUi
import com.example.venu.core.core_presentation.toEventDetailsUi
import com.example.venu.features.home.model.HomeAction
import com.example.venu.features.home.model.HomeUiState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.venu.core.core_common.eventdetails.SaveToListSheet
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    var selectedEvent by remember { mutableStateOf<EventDetailsUi?>(null) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome, Explorer",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Find something good near you today",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Featured",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // maps fake seed data to each feature card
//            state.featured.forEach { venue ->
//                FeaturedCard(
//                    title = venue.title,
//                    subtitle = venue.subtitle,
//                    onClick = { selectedEvent = venue.toEventDetailsUi() }
//                )
//            }
            state.featured.forEach { venue ->
                FeaturedCard(
                    title = venue.title,
                    subtitle = venue.subtitle,
                    onClick = { selectedEvent = venue.toEventDetailsUi() }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Near You",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        // maps fake seed data to each venue card
        state.nearYou.forEach { venue ->
//            VenueCard(
//                name = venue.title,
//                details = buildString {
//                    append(venue.subtitle)
//                    venue.distanceLabel?.let {
//                        append(" • ")
//                        append(it)
//                    }
//                    venue.ratingLabel?.let {
//                        append(" • ")
//                        append(it)
//                    }
//                }
//            )
            VenueCard(
                name = venue.title,
                details = buildString {
                    append(venue.subtitle)

                    venue.distanceLabel?.let {
                        append(" • ")
                        append(it)
                    }

                    venue.ratingLabel?.let {
                        append(" • ")
                        append(it)
                    }
                },
                onClick = { selectedEvent = venue.toEventDetailsUi() }
            )
        }

        Spacer(Modifier.height(24.dp))
    }

    if (state.showSaveSheet && state.pendingSaveEventId != null) {
        SaveToListSheet(
            pendingSaveEventId = state.pendingSaveEventId,
            availableLists = state.availableLists,
            onSaveToList = { eventId, listType ->
                onAction(
                    HomeAction.SaveToList(
                        eventId = eventId,
                        listType = listType
                    )
                )
            },
            onDismiss = {
                onAction(HomeAction.DismissSaveSheet)
            }
        )
    }

    selectedEvent?.let { event ->
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                selectedEvent = null
            }
        ) {
            EventDetailsSheet(
                event = event,
                showDirectionsButton = false,
                onBack = {
                    scope.launch {
                        sheetState.hide()
                        selectedEvent = null
                    }
                },
                onSaveClick = { onAction(HomeAction.SaveClicked(event.id)) },
                onViewOnMapClick = { /* TODO */ },
                onGetDirectionsClick = { /* TODO */ },
                onSubmitReview = { _, _ -> }
            )
        }
    }
}

@Composable
fun FeaturedCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    BaseEventCard(
        width = 220.dp,
        height = 140.dp,
        onClick = onClick
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun VenueCard(
    name: String,
    details: String,
    onClick: () -> Unit) {
    BaseEventCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(text = details, style = MaterialTheme.typography.bodyMedium)
        }
    }
}