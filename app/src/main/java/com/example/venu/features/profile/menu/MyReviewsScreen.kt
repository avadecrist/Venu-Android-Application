package com.example.venu.features.profile.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.components.ReviewCard
import com.example.venu.core.core_common.core_ui.theme.VenuColors
import com.example.venu.core.core_presentation.ReviewUi

@Composable
fun MyReviewsScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp, vertical = 22.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(26.dp),
                    tint = VenuColors.TextPrimary
                )
            }

            Text(
                text = "My Reviews",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = VenuColors.TextPrimary
            )
        }

        Text(
            text = "Your recent reviews",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = VenuColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(22.dp))

        ReviewCard(
            review = ReviewUi(
                authorInitial = "B",
                authorName = "Blue Bottle Coffee",
                rating = 5,
                comment = "Great coffee and a really nice atmosphere.",
                timeAgo = "Apr 19, 2026",
                id = "a"
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        ReviewCard(
            review = ReviewUi(
                authorInitial = "M",
                authorName = "Madison Square Park",
                rating = 4,
                comment = "Nice place to walk around and relax in the afternoon.",
                timeAgo = "Apr 12, 2026",
                id = "b"
            )
        )
    }
}