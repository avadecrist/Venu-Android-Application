package com.example.venu.features.profile.menu

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.components.ReviewCard
import com.example.venu.core.core_common.core_ui.theme.VenuColors
import com.example.venu.core.core_data.remote.firestore.ReviewFireStoreRepository
import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_presentation.ReviewUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyReviewsScreen(
    onBackClick: () -> Unit
) {
    val reviewRepository = remember {
        ReviewFireStoreRepository()
    }

    var reviews by remember {
        mutableStateOf<List<ReviewUi>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            errorMessage = null

            reviews = reviewRepository
                .getReviewsForCurrentUser()
                .map { review ->
                    review.toReviewUi()
                }
        } catch (error: Exception) {
            errorMessage = error.message ?: "Failed to load reviews."
        } finally {
            isLoading = false
        }
    }

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
                    text = errorMessage ?: "Something went wrong.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            reviews.isEmpty() -> {
                Text(
                    text = "You have not written any reviews yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VenuColors.TextSecondary
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

private fun Review.toReviewUi(): ReviewUi {
    return ReviewUi(
        authorInitial = displayName.firstOrNull()?.uppercase() ?: "?",
        displayName = displayName,
        photoUrl = photoUrl.orEmpty(),
        rating = rating,
        comment = comment,
        timeAgo = createdAt.toDateLabel(),
        id = id
    )
}

private fun Long.toDateLabel(): String {
    return SimpleDateFormat(
        "MMM d, yyyy",
        Locale.getDefault()
    ).format(Date(this))
}