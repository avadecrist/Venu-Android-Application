package com.example.venu.core.core_data.repository

import com.example.venu.core.core_data.fake.FakeSeed
import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_domain.model.RatingSummary
import com.example.venu.core.core_domain.repository.ReviewRepository
import java.util.UUID

class FakeReviewRepository : ReviewRepository {

    private val reviews = FakeSeed.reviews.toMutableList()
    private val currentUserId = "user_1" // fake logged-in user

    override fun getReviewsForEvent(eventId: String): List<Review> {
        return reviews.filter { it.eventId == eventId }
    }

    override fun getRatingSummary(eventId: String): RatingSummary {
        val eventReviews = reviews.filter { it.eventId == eventId }

        val count = eventReviews.size

        val average = if (count == 0) {
            0.0
        } else {
            eventReviews.map { it.rating }.average()
        }

        return RatingSummary(
            average = average,
            count = count
        )
    }

    override fun addReview(eventId: String, rating: Int, comment: String) {

        // prevent duplicate review
        if (hasUserReviewed(eventId, currentUserId)) return

        reviews.add(
            Review(
                id = UUID.randomUUID().toString(),
                eventId = eventId,
                userId = currentUserId,
                authorName = "You",
                rating = rating,
                comment = comment,
                createdAtLabel = "just now"
            )
        )
    }

    override fun hasUserReviewed(eventId: String, userId: String): Boolean {
        return reviews.any {
            it.eventId == eventId && it.userId == currentUserId
        }
    }

    override fun getUserReviewForEvent(eventId: String): Review? {
        return reviews.find { review ->
            review.eventId == eventId && review.userId == currentUserId
        }
    }
}