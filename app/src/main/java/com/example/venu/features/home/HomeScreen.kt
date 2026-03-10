package com.example.venu.features.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.venu.features.home.model.HomeAction
import com.example.venu.features.home.model.HomeUiState
import com.example.venu.features.home.viewmodel.HomeViewModel

// ADDED TO USE VIEWMODEL
@Composable
fun HomeRoute(
    viewModel: HomeViewModel = viewModel()
) {
    HomeScreen(
        state = viewModel.uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Venu",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.query,
            onValueChange = { onAction(HomeAction.QueryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search Venues") },
            singleLine = true
        )

        Spacer(Modifier.height(20.dp))

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
            state.featured.forEach { venue ->
                FeaturedCard(
                    title = venue.title,
                    subtitle = venue.subtitle
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Near you",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        // maps fake seed data to each venue card
        state.nearYou.forEach { venue ->
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
                }
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FeaturedCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(120.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun VenueCard(name: String, details: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(text = details, style = MaterialTheme.typography.bodyMedium)
        }
    }
}