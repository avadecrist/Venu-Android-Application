package com.example.venu.core.core_data.remote.firestore

import com.example.venu.core.core_domain.repository.ReviewRepository
import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_domain.model.RatingSummary
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

//class ReviewFireStoreRepository : ReviewRepository {
//    val db = FirebaseFirestore.getInstance()
//    val reviewsCollection = db.collection("reviews")
//
//    fun addReview(
//        review: ReviewDto,
//        onSuccess: () -> Unit,
//        onError: (Exception) -> Unit
//    ) {
//        val docRef = reviewsCollection.document()
//
//        val reviewWithId = review.copy(reviewId = docRef.id)
//
//        docRef.set(reviewWithId)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { onError(it) }
//    }
//
//    fun getReviewsForEvent(
//        eventId: String,
//        onResult: (List<ReviewDto>) -> Unit
//    ) {
//        reviewsCollection
//            .whereEqualTo("eventId", eventId)
//            .get()
//            .addOnSuccessListener { snapshot ->
//                val reviews = snapshot.toObjects(ReviewDto::class.java)
//                onResult(reviews)
//            }
//    }
//}

class ReviewFireStoreRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ReviewRepository {

    private val reviewsCollection = db.collection("reviews")

    override suspend fun getReviewsForEvent(eventId: String): List<Review> {
        val snapshot = reviewsCollection
            .whereEqualTo("eventId", eventId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(ReviewFireStoreDto::class.java)?.toDomain()
        }
    }

    override suspend fun getRatingSummary(eventId: String): RatingSummary {
        val reviews = getReviewsForEvent(eventId)

        if (reviews.isEmpty()) {
            return RatingSummary(
                average = 0.0,
                count = 0
            )
        }

        return RatingSummary(
            average = reviews.map { it.rating }.average(),
            count = reviews.size
        )
    }

    override suspend fun addReview(
        eventId: String,
        rating: Int,
        comment: String
    ) {
        val user = auth.currentUser
            ?: throw IllegalStateException("User must be signed in to add a review")

        val docRef = reviewsCollection.document()

        val reviewFireStoreDto = ReviewFireStoreDto(
            reviewId = docRef.id,
            eventId = eventId,
            googlePlaceId = "", // fill this later if needed
            uid = user.uid,
            displayName = user.displayName ?: "Anonymous",
            photoUrl = user.photoUrl?.toString(),
            rating = rating,
            comment = comment,
            createdAt = System.currentTimeMillis()
        )

        docRef.set(reviewFireStoreDto).await()
    }

    override suspend fun hasUserReviewed(
        eventId: String,
        userId: String
    ): Boolean {
        val snapshot = reviewsCollection
            .whereEqualTo("eventId", eventId)
            .whereEqualTo("uid", userId)
            .limit(1)
            .get()
            .await()

        return !snapshot.isEmpty
    }

    override suspend fun getUserReviewForEvent(eventId: String): Review? {
        val user = auth.currentUser ?: return null

        val snapshot = reviewsCollection
            .whereEqualTo("eventId", eventId)
            .whereEqualTo("uid", user.uid)
            .limit(1)
            .get()
            .await()

        return snapshot.documents.firstOrNull()
            ?.toObject(ReviewFireStoreDto::class.java)
            ?.toDomain()
    }
}