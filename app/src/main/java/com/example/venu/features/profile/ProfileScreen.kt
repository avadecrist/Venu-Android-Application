package com.example.venu.features.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.theme.VenuColors
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
            .padding(horizontal = 22.dp, vertical = 22.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        ProfileHeader(
            state = state,
            onSignInClick = onSignInClick,
            onEditProfileClick = onEditProfileClick
        )

        if (state.isSignedIn) {
            Spacer(modifier = Modifier.height(30.dp))
            ActivityCard(state = state)
        }

        Spacer(modifier = Modifier.height(30.dp))

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
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(VenuColors.AvatarBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = VenuColors.AccentBlue
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = if (state.isSignedIn) state.displayName else "Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = VenuColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (state.isSignedIn) {
                "Your activity and account"
            } else {
                "Sign in to save events and leave reviews"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = VenuColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (state.isSignedIn) {
            OutlinedButton(onClick = onEditProfileClick) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        } else {
            Button(onClick = onSignInClick) {
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ActivityCard(
    state: ProfileUiState
) {
    SettingsLikeCard {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Your Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = VenuColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(18.dp))

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
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = VenuColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = VenuColors.TextSecondary
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
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = VenuColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsLikeCard {
            Column {
                MenuRow(
                    leading = Icons.Filled.Star,
                    title = "My Reviews",
                    trailingText = if (state.isSignedIn) state.reviewsCount.toString() else null,
                    onClick = onMyReviewsClick
                )

                HorizontalDivider(color = VenuColors.Border)

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
private fun SettingsLikeCard(
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        content()
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
            .padding(horizontal = 20.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = leading,
            contentDescription = title,
            modifier = Modifier.size(22.dp),
            tint = VenuColors.TextSecondary
        )

        Spacer(modifier = Modifier.size(18.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = VenuColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )

        if (trailingText != null) {
            Text(
                text = trailingText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = VenuColors.TextSecondary
            )

            Spacer(modifier = Modifier.size(10.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = VenuColors.TextSecondary
        )
    }
}