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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.features.profile.model.ProfileUiState

@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onSignInClick: () -> Unit,
    onEditProfileClick: () -> Unit = {},
    onMyReviewsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        ProfileHeader(
            state = state,
            onSignInClick = onSignInClick,
            onEditProfileClick = onEditProfileClick
        )

        if (state.isSignedIn) {
            Spacer(modifier = Modifier.height(24.dp))
            ActivityCard(state = state)
        }

        Spacer(modifier = Modifier.height(24.dp))

        MenuSection(
            state = state,
            onMyReviewsClick = onMyReviewsClick,
            onSettingsClick = onSettingsClick
        )
    }
}

@Composable
private fun ProfileHeader(
    state: ProfileUiState,
    onSignInClick: () -> Unit,
    onEditProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(92.dp)
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
            text = if (state.isSignedIn) state.displayName else "Profile",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (state.isSignedIn) {
                "Your activity and account"
            } else {
                "Sign in to save events and leave reviews"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isSignedIn) {
            OutlinedButton(onClick = onEditProfileClick) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Edit Profile")
            }
        } else {
            Button(onClick = onSignInClick) {
                Text("Sign In")
            }
        }
    }
}

@Composable
private fun ActivityCard(
    state: ProfileUiState
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = "Your Activity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    value = state.eventsCount.toString(),
                    label = "Events",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    value = state.reviewsCount.toString(),
                    label = "Reviews",
                    modifier = Modifier.weight(1f)
                )
                StatItem(
                    value = state.streakCount.toString(),
                    label = "Streak",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun MenuSection(
    state: ProfileUiState,
    onMyReviewsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Account",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 2.dp
        ) {
            Column {
                MenuRow(
                    leading = Icons.Filled.Star,
                    title = "My Reviews",
                    trailingText = if (state.isSignedIn) state.reviewsCount.toString() else null,
                    onClick = onMyReviewsClick
                )

                HorizontalDivider()

                MenuRow(
                    leading = Icons.Filled.Settings,
                    title = "Settings",
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
private fun MenuRow(
    leading: ImageVector,
    title: String,
    trailingText: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leading,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.size(8.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}