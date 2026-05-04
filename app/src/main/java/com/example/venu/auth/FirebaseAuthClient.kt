package com.example.venu.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class FirebaseAuthClient(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUserResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = firebaseAuth.signInWithCredential(credential).await()
        val user = authResult.user ?: error("Firebase user was null after sign-in.")

        return FirebaseUserResult(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName,
            photoUrl = user.photoUrl?.toString()
        )
    }

    fun currentUser(): FirebaseUserResult? {
        val user = firebaseAuth.currentUser ?: return null

        return FirebaseUserResult(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName,
            photoUrl = user.photoUrl?.toString()
        )
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}

data class FirebaseUserResult(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?
)