package com.example.venu.core.core_common.core_ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.venu.core.core_common.core_ui.theme.VenuTheme
//@Composable
//fun ReviewCard(
//    review: ReviewUi
//) {
//    Surface(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(24.dp),
//        color = Color.White,
//        border = BorderStroke(1.dp, VenuColors.Border)
//    ) {
//        Column(
//            modifier = Modifier.padding(20.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.Top
//            ) {
//                ReviewerAvatar(initial = review.authorInitial)
//
//                Spacer(modifier = Modifier.width(12.dp))
//
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = review.displayName,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        color = VenuColors.TextPrimary
//                    )
//                }
//
//                Text(
//                    text = review.timeAgo,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = VenuColors.TextMuted
//                )
//            }
//
//            Spacer(modifier = Modifier.height(14.dp))
//
//            Text(
//                text = buildStarString(review.rating),
//                style = MaterialTheme.typography.headlineSmall,
//                color = VenuColors.Star
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                text = review.comment,
//                style = MaterialTheme.typography.bodyLarge,
//                color = VenuColors.TextSecondary
//            )
//        }
//    }
//}
@Composable
fun ReviewCard(
    review: ReviewUi
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                ReviewerAvatar(
                    photoUrl = review.photoUrl
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = review.displayName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = review.timeAgo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// tweak this to use photoUrl from Google Auth
@Composable
private fun ReviewerAvatar(
    photoUrl: String?
) {
    AsyncImage(
        model = photoUrl,
        contentDescription = "Reviewer profile photo",
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

private fun buildStarString(rating: Int): String {
    return "★".repeat(rating.coerceIn(0, 5)) + "☆".repeat((5 - rating).coerceIn(0, 5))
}

@Preview(
    name = "Review Card - Light Mode",
    showBackground = true
)
@Composable
private fun ReviewCardLightPreview() {
    VenuTheme(darkTheme = false) {
        ReviewCard(
            review = ReviewUi(
                id = "1",
                displayName = "Dominic Hurtado",
                photoUrl = "https://example.com/profile.jpg",
                rating = 5,
                comment = "Really cool spot. Great energy, good crowd, and definitely somewhere I would come back to.",
                timeAgo = "2h ago"
            )
        )
    }
}

@Preview(
    name = "Review Card - Dark Mode",
    showBackground = true
)
@Composable
private fun ReviewCardDarkPreview() {
    VenuTheme(darkTheme = true) {
        ReviewCard(
            review = ReviewUi(
                id = "1",
                displayName = "Dominic Hurtado",
                photoUrl = "",
                rating = 5,
                comment = "Really cool spot. Great energy, good crowd, and definitely somewhere I would come back to.",
                timeAgo = "2h ago"
            )
        )
    }
}