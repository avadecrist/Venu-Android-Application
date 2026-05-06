package com.example.venu.features.reviews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.venu.core.core_data.remote.firestore.ReviewFireStoreRepository
import com.example.venu.core.core_presentation.ReviewUi
import com.example.venu.features.profile.menu.MyReviewsScreen
import com.example.venu.features.reviews.mappers.toReviewUi

@Composable
fun MyReviewsRoute(
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

    MyReviewsScreen(
        reviews = reviews,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onBackClick = onBackClick
    )
}