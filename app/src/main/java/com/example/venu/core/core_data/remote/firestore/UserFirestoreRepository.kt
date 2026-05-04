package com.example.venu.core.core_data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserFirestoreRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val usersCollection = firestore.collection("users")

    suspend fun getUser(uid: String): UserFirestoreDto? {
        val snapshot = usersCollection.document(uid).get().await()
        return snapshot.toObject(UserFirestoreDto::class.java)
    }

    suspend fun createUserIfMissing(
        uid: String,
        email: String?,
        displayName: String?,
        photoUrl: String?
    ) {
        val userRef = usersCollection.document(uid)
        val snapshot = userRef.get().await()

        if (!snapshot.exists()) {
            val now = System.currentTimeMillis()

            val user = UserFirestoreDto(
                uid = uid,
                email = email.orEmpty(),
                displayName = displayName,
                photoUrl = photoUrl,
                createdAt = now,
                updatedAt = now
            )

            userRef.set(user).await()
        }
    }

    suspend fun updateDisplayName(
        uid: String,
        displayName: String
    ) {
        usersCollection.document(uid)
            .update(
                mapOf(
                    "displayName" to displayName,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .await()
    }
}