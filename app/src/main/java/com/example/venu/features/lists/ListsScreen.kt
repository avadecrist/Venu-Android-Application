package com.example.venu.features.lists

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.features.lists.model.ListsUiEvent
import com.example.venu.features.lists.model.ListsUiState
import com.example.venu.features.lists.model.defaultListsTabs

@Composable
fun ListsScreen(
    state: ListsUiState,
    onEvent: (ListsUiEvent) -> Unit
) {
    val selectedIndex = defaultListsTabs.indexOfFirst {
        it.type == state.selectedTab
    }.coerceAtLeast(0)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Lists",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        ScrollableTabRow(
            selectedTabIndex = selectedIndex
        ) {
            defaultListsTabs.forEach { tab ->
                Tab(
                    selected = state.selectedTab == tab.type,
                    onClick = { onEvent(ListsUiEvent.SelectTab(tab.type)) },
                    text = { Text(tab.label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (state.events.isEmpty()) {
            Text(
                text = emptyMessageFor(state.selectedTab),
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                text = event.title,
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

            event.distanceMiles?.let { miles ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${miles} mi away",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Category: ${event.category} • Price: ${event.priceTier}",
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
            ListType.WANT_TO_GO -> {
                TextButton(
                    onClick = {
                        onEvent(
                            ListsUiEvent.MoveEvent(
                                eventId = event.id,
                                from = ListType.WANT_TO_GO,
                                to = ListType.ALREADY_WENT
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

            ListType.ALREADY_WENT -> {
                TextButton(
                    onClick = {
                        onEvent(
                            ListsUiEvent.MoveEvent(
                                eventId = event.id,
                                from = ListType.ALREADY_WENT,
                                to = ListType.TO_REVIEW
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
                                tab = ListType.ALREADY_WENT,
                                eventId = event.id
                            )
                        )
                    }
                ) {
                    Text("Remove")
                }
            }

            ListType.TO_REVIEW -> {
                TextButton(
                    onClick = {
                        onEvent(
                            ListsUiEvent.RemoveFromList(
                                tab = ListType.TO_REVIEW,
                                eventId = event.id
                            )
                        )
                    }
                ) {
                    Text("Remove")
                }
            }
        }
    }
}

private fun emptyMessageFor(selectedTab: ListType): String {
    return when (selectedTab) {
        ListType.WANT_TO_GO -> "You have no events in Want to Go yet."
        ListType.ALREADY_WENT -> "You have no events in Already Went yet."
        ListType.TO_REVIEW -> "You have no events in To Review yet."
    }
}