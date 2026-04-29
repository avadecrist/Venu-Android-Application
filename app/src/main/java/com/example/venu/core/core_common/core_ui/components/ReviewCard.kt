package com.example.venu.core.core_common.core_ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.theme.VenuColors
import com.example.venu.core.core_presentation.ReviewUi

@Composable
fun ReviewCard(
    review: ReviewUi
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                ReviewerAvatar(initial = review.authorInitial)

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = review.authorName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = VenuColors.TextPrimary
                    )
                }

                Text(
                    text = review.timeAgo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VenuColors.TextMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = buildStarString(review.rating),
                style = MaterialTheme.typography.headlineSmall,
                color = VenuColors.Star
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyLarge,
                color = VenuColors.TextSecondary
            )
        }
    }
}

@Composable
private fun ReviewerAvatar(initial: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(VenuColors.AvatarBg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleLarge,
            color = VenuColors.AccentBlue,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun buildStarString(rating: Int): String {
    return "★".repeat(rating.coerceIn(0, 5)) + "☆".repeat((5 - rating).coerceIn(0, 5))
}