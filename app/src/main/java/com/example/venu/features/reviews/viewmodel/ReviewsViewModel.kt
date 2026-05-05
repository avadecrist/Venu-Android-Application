package com.example.venu.features.reviews.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.venu.core.core_domain.repository.ReviewRepository
import com.example.venu.features.reviews.model.ReviewDraft
import com.example.venu.features.reviews.model.ReviewsAction
import com.example.venu.features.reviews.model.ReviewsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewsViewModel(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewsUiState())
    val state: StateFlow<ReviewsUiState> = _state.asStateFlow()

    fun onAction(event: ReviewsAction) {
        when (event) {
            is ReviewsAction.LoadReviews -> loadReviews(event.eventId)
            is ReviewsAction.UpdateRating -> updateRating(event.rating)
            is ReviewsAction.UpdateComment -> updateComment(event.comment)
            ReviewsAction.Submit -> submitReview()
            ReviewsAction.Cancel -> cancelReview()
        }
    }

    private fun loadReviews(eventId: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    eventId = eventId,
                    draft = ReviewDraft(eventId = eventId),
                    isLoading = true,
                    errorMessage = null
                )

                val reviews = reviewRepository.getReviewsForEvent(eventId)
                val summary = reviewRepository.getRatingSummary(eventId)
                val myReview = reviewRepository.getUserReviewForEvent(eventId)

                _state.value = _state.value.copy(
                    reviews = reviews,
                    summary = summary,
                    myReview = myReview,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Could not load reviews."
                )
            }
        }
    }

    private fun updateRating(rating: Int) {
        val draft = _state.value.draft ?: return

        _state.value = _state.value.copy(
            draft = draft.copy(rating = rating),
            errorMessage = null
        )
    }

    private fun updateComment(comment: String) {
        val draft = _state.value.draft ?: return

        _state.value = _state.value.copy(
            draft = draft.copy(comment = comment),
            errorMessage = null
        )
    }

    private fun submitReview() {
        Log.d("ReviewDebug", "submitReview() called")

        val draft = _state.value.draft ?: return
        Log.d("ReviewDebug", "Current draft = $draft")

        if (draft.rating !in 1..5) {
            _state.value = _state.value.copy(
                errorMessage = "Please select a rating."
            )
            return
        }

        viewModelScope.launch {
            try {
                Log.d("ReviewDebug", "Coroutine started")
                _state.value = _state.value.copy(
                    isSubmitting = true,
                    errorMessage = null
                )

                Log.d("ReviewDebug", "Calling repository.addReview")
                reviewRepository.addReview(
                    eventId = draft.eventId,
                    rating = draft.rating,
                    comment = draft.comment
                )
                Log.d("ReviewDebug", "Firestore addReview completed")

                val reviews = reviewRepository.getReviewsForEvent(draft.eventId)
                val summary = reviewRepository.getRatingSummary(draft.eventId)
                val myReview = reviewRepository.getUserReviewForEvent(draft.eventId)

                _state.value = _state.value.copy(
                    reviews = reviews,
                    summary = summary,
                    myReview = myReview,
                    draft = ReviewDraft(eventId = draft.eventId),
                    isSubmitting = false,
                    errorMessage = null
                )
            } catch (e: Exception) {
                Log.e("ReviewDebug", "Submit failed", e)
                _state.value = _state.value.copy(
                    isSubmitting = false,
                    errorMessage = e.message ?: "Could not submit review."
                )
            }
        }
    }

    private fun cancelReview() {
        val eventId = _state.value.eventId

        _state.value = _state.value.copy(
            draft = ReviewDraft(eventId = eventId),
            errorMessage = null
        )
    }
}