package com.example.venu.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.venu.core.core_common.core_ui.theme.VenuTheme
import com.example.venu.core.core_common.eventdetails.SaveToListSheet
import com.example.venu.core.core_common.eventdetails.genreEmoji
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_presentation.genreColor
import com.example.venu.features.home.model.HomeVenueUi
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
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        // Section 1: Hero + Featured with gradient
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.50f),
                            0.20f to MaterialTheme.colorScheme.primary.copy(alpha = 0.70f),
                            0.70f to MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            1.0f to MaterialTheme.colorScheme.background
                        )
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Welcome, Explorer",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Find something good near you today",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Featured",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                state.featured.forEach { venue ->
                    FeaturedCard(
                        title = venue.title,
                        subtitle = venue.subtitle,
                        genre = venue.genre,
                        onClick = { selectedEvent = venue.toEventDetailsUi() }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        // Section 2: normal background
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Near You",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            state.nearYou.forEach { venue ->
                VenueCard(
                    name = venue.title,
                    details = buildString {
                        append(venue.subtitle)
                        venue.distanceLabel?.let { append(" • $it") }
                        venue.ratingLabel?.let { append(" • $it") }
                    },
                    genre = venue.genre,
                    onClick = { selectedEvent = venue.toEventDetailsUi() }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
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
    genre: Genre,
    onClick: () -> Unit
) {
    val baseColor = genreColor(genre)
    val glowColor = baseColor.copy(alpha = 0.35f)
    Box(
        modifier = Modifier
            .width(220.dp)
            .height(150.dp)
            .background(
                color = baseColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(2.dp)
    ) {
        BaseEventCard(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick,
            contentPadding = 18.dp,
            borderColor = baseColor.copy(alpha = 0.35f),
            contentColor = baseColor.copy(alpha = 0.15f),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                Text(
                    text = "Learn More",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
private fun VenueCard(
    name: String,
    details: String,
    genre: Genre,
    onClick: () -> Unit
) {
    val baseColor = genreColor(genre)
    val bgColor = baseColor.copy(alpha = 0.12f)
    BaseEventCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        onClick = onClick,
        contentPadding = 18.dp,
        contentColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = genreEmoji(genre),
                    style = MaterialTheme.typography.titleMedium,
//                    color = contentColor
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = details,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }
        }
    }
}

@Preview(
    name = "Home Screen Preview",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun HomeScreenPreview() {
    VenuTheme(
        dynamicColor = false
    ) {
        HomeScreen(
            state = HomeUiState(
                featured = listOf(
                    HomeVenueUi(
                        id = "1",
                        title = "Sushi Miko",
                        subtitle = "Sushi Miko • Dinner hours",
                        distanceLabel = "0.4 km",
                        ratingLabel = "★ 4.7",
                        genre = Genre.FOOD
                    ),
                    HomeVenueUi(
                        id = "2",
                        title = "Indie Night at Tupperware",
                        subtitle = "Tupperware Club • Tonight 11 PM",
                        distanceLabel = "0.2 km",
                        ratingLabel = "★ 4.5",
                        genre = Genre.MUSIC
                    )
                ),
                nearYou = listOf(
                    HomeVenueUi(
                        id = "2",
                        title = "Indie Night at Tupperware",
                        subtitle = "Tupperware Club • Tonight 11 PM",
                        distanceLabel = "0.2 km",
                        ratingLabel = "★ 4.5",
                        genre = Genre.NIGHTLIFE
                    ),
                    HomeVenueUi(
                        id = "3",
                        title = "Late Night Study Session",
                        subtitle = "HanSo Café • Tonight 9 PM",
                        distanceLabel = "0.1 km",
                        ratingLabel = null,
                        genre = Genre.STUDY
                    ),
                    HomeVenueUi(
                        id = "4",
                        title = "Open Mic Comedy",
                        subtitle = "La Vía Láctea • Fri 10 PM",
                        distanceLabel = "0.3 km",
                        ratingLabel = "★ 4.8",
                        genre = Genre.COFFEE
                    )
                )
            ),
            onAction = {}
        )
    }
}