package com.example.venu.core.core_domain.repository

// INTERFACE FOR METHODS
import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_domain.model.RatingSummary
interface ReviewRepository {
    fun getReviewsForEvent(eventId: String): List<Review>
    fun getRatingSummary(eventId: String): RatingSummary
        // in Event Details/Rating page:
          // val summary = reviewRepo.getRatingSummary(eventId)

    fun addReview(eventId: String, rating: Int, comment: String)
    // after addReview() you fetch summary again (to make the UI update)

    fun hasUserReviewed(eventId: String, userId: String): Boolean // prevents duplicate review UI

    // helper function
    fun getUserReviewForEvent(eventId: String): Review? // will show user their existing review
}