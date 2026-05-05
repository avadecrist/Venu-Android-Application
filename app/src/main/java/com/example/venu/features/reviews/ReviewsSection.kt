package com.example.venu.features.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.components.LeaveReviewCard
import com.example.venu.core.core_common.core_ui.components.ReviewCard
import com.example.venu.core.core_common.core_ui.components.ReviewsCountHeader
import com.example.venu.features.reviews.mappers.toUi
import com.example.venu.features.reviews.model.ReviewsAction
import com.example.venu.features.reviews.viewmodel.ReviewsViewModel

@Composable
fun ReviewsSection(
    eventId: String,
    viewModel: ReviewsViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.onAction(ReviewsAction.LoadReviews(eventId))
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReviewsCountHeader(reviewCount = state.summary.count)

        if (state.myReview == null) {
            LeaveReviewCard(
                draft = state.draft,
                isSubmitting = state.isSubmitting,
                onRatingChange = {
                    viewModel.onAction(
                        ReviewsAction.UpdateRating(it)
                    )
                },
                onCommentChange = {
                    viewModel.onAction(
                        ReviewsAction.UpdateComment(it)
                    )
                },
                onSubmit = {
                    viewModel.onAction(
                        ReviewsAction.Submit
                    )
                }
            )
        } else {
            Text("You already reviewed this event.")
        }

        state.reviews.forEach { review ->
            ReviewCard(review = review.toUi())
        }
    }
}