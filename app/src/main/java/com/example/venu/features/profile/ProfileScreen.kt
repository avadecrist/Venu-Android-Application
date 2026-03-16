package com.example.venu.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.features.profile.model.ProfileUiState

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onSignInClick : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ProfileHeader(
            state = state,
            onSignInClick = onSignInClick
        )

        if (state.isSignedIn) {
            Spacer(modifier = Modifier.height(24.dp))
            StatsRow(state = state)
        }

        Spacer(modifier = Modifier.height(24.dp))

        MenuSection(state = state)
    }
}

@Composable
private fun ProfileHeader(
    state: ProfileUiState,
    onSignInClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape),
            tonalElevation = 2.dp,
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome, ${state.displayName}!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (state.isSignedIn) {
                "Your profile activity and stats"
            } else {
                "Sign in to save events and leave reviews"
            },
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!state.isSignedIn) {
            Button(onClick = onSignInClick) {
                Text("Sign In")
            }
        }
    }
}

@Composable
private fun StatsRow(
    state: ProfileUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(value = state.eventsCount.toString(), label = "Events")
        StatItem(value = state.reviewsCount.toString(), label = "Reviews")
        StatItem(value = state.streakCount.toString(), label = "Streak")
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun MenuSection(
    state: ProfileUiState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        MenuRow(
            leading = "(bookmark)",
            title = "Saved Events",
            trailingText = if (state.isSignedIn) state.eventsCount.toString() else null
        )

        HorizontalDivider()

        MenuRow(
            leading = "(star)",
            title = "My Reviews",
            trailingText = if (state.isSignedIn) state.reviewsCount.toString() else null
        )

        HorizontalDivider()

        MenuRow(
            leading = "(pin)",
            title = "Attended",
            trailingText = if (state.isSignedIn) state.eventsCount.toString() else null
        )

        HorizontalDivider()

        MenuRow(
            leading = "⚙",
            title = "Settings"
        )
    }
}

@Composable
private fun MenuRow(
    leading: String,
    title: String,
    trailingText: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = leading,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        if (trailingText != null) {
            Text(
                text = trailingText,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.size(8.dp))
        }

        Text(
            text = ">",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}