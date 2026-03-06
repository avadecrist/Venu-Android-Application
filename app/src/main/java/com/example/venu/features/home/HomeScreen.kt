package com.example.venu.features.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
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
            value = "",
            onValueChange = { /* fake for now */ },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search venues") },
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
            FeaturedCard(title = "Cafe Luna", subtitle = "Cozy • Coffee")
            FeaturedCard(title = "Sushi Miko", subtitle = "Fresh • Japanese")
            FeaturedCard(title = "Bar Atlas", subtitle = "Cocktails • Night")
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Near you",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        VenueCard(name = "Pasta House", details = "Italian • 12 min")
        VenueCard(name = "Honest Greens", details = "Healthy • 8 min")
        VenueCard(name = "Burger Bar", details = "Burgers • 15 min")
        VenueCard(name = "Taco Time", details = "Mexican • 10 min")

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