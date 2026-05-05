package com.example.venu.features.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.features.lists.model.ListsUiEvent
import com.example.venu.features.lists.model.ListsUiState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.venu.core.core_common.core_ui.theme.VenuTheme
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier

@Composable
fun ListsScreen(
    state: ListsUiState,
    onEvent: (ListsUiEvent) -> Unit
) {
    var showAddListDialog by remember { mutableStateOf(false) }
    var newListName by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Text(
//                text = "Lists",
//                color = MaterialTheme.colorScheme.onBackground,
//                style = MaterialTheme.typography.headlineLarge,
//                modifier = Modifier.padding(16.dp)
//            )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 18.dp)
                .padding(top = 24.dp, bottom = 0.dp)
        ) {
            Text(
                text = "Your Lists",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Save, organize, and revisit your favorite events.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            val selectedIndex =
                state.tabs.indexOfFirst { it == state.selectedTab }
                    .let { if (it >= 0) it else 0 }

            key(state.tabs.map {
                when (it) {
                    is ListType.Custom -> "custom:${it.id}"
                    ListType.WantToGo -> "want"
                    ListType.AlreadyWent -> "went"
                    ListType.ToReview -> "review"
                }
            }) {
                ScrollableTabRow(
                    selectedTabIndex = selectedIndex,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    edgePadding = 16.dp
                ) {
                    state.tabs.forEach { tab ->
                        key(
                            when (tab) {
                                is ListType.Custom -> "custom:${tab.id}"
                                ListType.WantToGo -> "want"
                                ListType.AlreadyWent -> "went"
                                ListType.ToReview -> "review"
                            }
                        ) {
                            Tab(
                                selected = tab == state.selectedTab,
                                onClick = { onEvent(ListsUiEvent.SelectTab(tab)) },
                                selectedContentColor = MaterialTheme.colorScheme.onBackground,
                                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                text = { Text(tabLabel(tab)) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (state.events.isEmpty()) {
                Text(
                    text = emptyMessageFor(state.selectedTab),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    items(state.events, key = { event -> event.id }) { event ->
                        ListEventCard(
                            event = event,
                            selectedTab = state.selectedTab,
                            onEvent = onEvent
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        AddListFab(
            onClick = { showAddListDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 24.dp)
        )
    }
    if (showAddListDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddListDialog = false
                newListName = ""
            },
            title = { Text("Create new list") },
            text = {
                OutlinedTextField(
                    value = newListName,
                    onValueChange = { newListName = it },
                    label = { Text("List name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(ListsUiEvent.CreateCustomList(newListName))
                        showAddListDialog = false
                        newListName = ""
                    },
                    enabled = newListName.isNotBlank()
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddListDialog = false
                        newListName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
private fun ListEventCard(
    event: Event,
    selectedTab: ListType,
    onEvent: (ListsUiEvent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = event.subtitle,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${event.locationName} • ${event.startTimeLabel}",
                style = MaterialTheme.typography.bodySmall
            )

            event.distanceKm?.let { km ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$km km away",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category: ${event.genre} • Price: ${event.priceTier}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            ActionRow(
                event = event,
                selectedTab = selectedTab,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun ActionRow(
    event: Event,
    selectedTab: ListType,
    onEvent: (ListsUiEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        when (selectedTab) {
            ListType.WantToGo -> {
                TextButton(
                    onClick = {
                        onEvent(
                            ListsUiEvent.MoveEvent(
                                eventId = event.id,
                                from = ListType.WantToGo,
                                to = ListType.AlreadyWent
                            )
                        )
                    }
                ) {
                    Text("Mark Went")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = {
                        onEvent(ListsUiEvent.ToggleWantToGo(event.id))
                    }
                ) {
                    Text("Remove")
                }
            }

            ListType.AlreadyWent -> {
                TextButton(
                    onClick = {
                        onEvent(
                            ListsUiEvent.MoveEvent(
                                eventId = event.id,
                                from = ListType.AlreadyWent,
                                to = ListType.ToReview
                            )
                        )
                    }
                ) {
                    Text("To Review")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(
                    onClick = {
                        onEvent(
                            ListsUiEvent.RemoveFromList(
                                tab = ListType.AlreadyWent,
                                eventId = event.id
                            )
                        )
                    }
                ) {
                    Text("Remove")
                }
            }

            ListType.ToReview -> {
                TextButton(
                    onClick = {
                        onEvent(
                            ListsUiEvent.RemoveFromList(
                                tab = ListType.ToReview,
                                eventId = event.id
                            )
                        )
                    }
                ) {
                    Text("Remove")
                }
            }

            is ListType.Custom -> {
                val customListName = selectedTab.name
                TextButton(
                    onClick = {
                        onEvent(
                            ListsUiEvent.RemoveFromList(
                                tab = selectedTab, // this is the Custom list instance
                                eventId = event.id
                            )
                        )
                    }
                ) {
                    Text("Remove $customListName")
                }
            }
        }
    }
}

private fun emptyMessageFor(selectedTab: ListType): String {
    return when (selectedTab) {
        ListType.WantToGo -> "You have no events in Want to Go yet."
        ListType.AlreadyWent -> "You have no events in Already Went yet."
        ListType.ToReview -> "You have no events in To Review yet."
        is ListType.Custom -> "You have no events in this list yet."
    }
}

fun tabLabel(tab: ListType): String {
    return when (tab) {
        ListType.WantToGo -> "Want To Go"
        ListType.AlreadyWent -> "Already Went"
        ListType.ToReview -> "To Review"
        is ListType.Custom -> tab.name
    }
}

@Composable
private fun AddListFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(60.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add list",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun ListsScreenPreview() {
    VenuTheme {
        ListsScreen(
            state = ListsUiState(
                tabs = listOf(
                    ListType.WantToGo,
                    ListType.AlreadyWent,
                    ListType.ToReview,
                    ListType.Custom(id = "custom-1", name = "Date Night")
                ),
                selectedTab = ListType.WantToGo,
                events = listOf(
                    Event(
                        id = "1",
                        name = "Beach Bonfire",
                        subtitle = "Live music and food trucks",
                        genre = Genre.MUSIC,
                        locationName = "Newport Beach",
                        latitude = 33.6189,
                        longitude = -117.9298,
                        distanceKm = 4.2,
                        priceTier = PriceTier.UNDER_20,
                        startTimeLabel = "Tonight • 7:00 PM",
                        imageUrl = null,
                        credibilityScore = 92,
                        reviewCount = 18,
                        isVerifiedVenue = true,
                        averageRating = 4.6
                    ),
                    Event(
                        id = "2",
                        name = "Coffee Pop-Up",
                        subtitle = "Local vendors and outdoor seating",
                        genre = Genre.FOOD,
                        locationName = "Costa Mesa",
                        latitude = 33.6411,
                        longitude = -117.9187,
                        distanceKm = 7.8,
                        priceTier = PriceTier.FREE,
                        startTimeLabel = "Tomorrow • 10:00 AM",
                        imageUrl = null,
                        credibilityScore = 85,
                        reviewCount = 9,
                        isVerifiedVenue = false,
                        averageRating = 4.2
                    )
                )
            ),
            onEvent = {}
        )
    }
}