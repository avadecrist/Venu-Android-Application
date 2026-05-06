package com.example.venu.features.profile.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.components.ReviewCard
import com.example.venu.core.core_common.core_ui.theme.VenuTheme
import com.example.venu.core.core_data.fake.FakeSeed
import com.example.venu.core.core_presentation.ReviewUi
import com.example.venu.features.reviews.mappers.toReviewUi

@Composable
fun MyReviewsScreen(
    reviews: List<ReviewUi>,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 22.dp)
            .padding(top = 24.dp, bottom = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
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
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "My Reviews",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Text(
            text = "Your recent reviews",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(22.dp))

        when {
            isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            reviews.isEmpty() -> {
                Text(
                    text = "You have not written any reviews yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(
                        items = reviews,
                        key = { review -> review.id }
                    ) { review ->
                        ReviewCard(review = review)
                    }
                }
            }
        }
    }
}


@Preview(
    name = "My Reviews - Light Mode",
    showBackground = true
)
@Composable
private fun MyReviewsScreenLightPreview() {
    VenuTheme(darkTheme = false) {
        MyReviewsScreen(
            reviews = FakeSeed.reviews.map { it.toReviewUi() },
            isLoading = false,
            errorMessage = null,
            onBackClick = {}
        )
    }
}

@Preview(
    name = "My Reviews - Dark Mode",
    showBackground = true
)
@Composable
private fun MyReviewsScreenDarkPreview() {
    VenuTheme(darkTheme = true) {
        MyReviewsScreen(
            reviews = FakeSeed.reviews.map { it.toReviewUi() },
            isLoading = false,
            errorMessage = null,
            onBackClick = {}
        )
    }
}