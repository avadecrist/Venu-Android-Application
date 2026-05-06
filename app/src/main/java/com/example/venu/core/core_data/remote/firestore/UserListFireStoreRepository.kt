package com.example.venu.core.core_data.remote.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ListenerRegistration

class UserListFirestoreRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private fun listsCollection(uid: String) =
        firestore
            .collection("users")
            .document(uid)
            .collection("lists")

    suspend fun getLists(uid: String): List<UserListFirestoreDto> {
        val snapshot = listsCollection(uid)
            .get()
            .await()

        return snapshot.toObjects(UserListFirestoreDto::class.java)
    }

    suspend fun getList(
        uid: String,
        listId: String
    ): UserListFirestoreDto? {
        val snapshot = listsCollection(uid)
            .document(listId)
            .get()
            .await()

        return snapshot.toObject(UserListFirestoreDto::class.java)
    }

    suspend fun createCustomList(
        uid: String,
        name: String
    ): String {
        val now = System.currentTimeMillis()
        val docRef = listsCollection(uid).document()

        val list = UserListFirestoreDto(
            id = docRef.id,
            name = name,
            type = "custom",
            ownerId = uid,
            createdAt = now,
            updatedAt = now,
            itemCount = 0
        )

        docRef.set(list).await()

        return docRef.id
    }

    suspend fun updateListName(
        uid: String,
        listId: String,
        name: String
    ) {
        listsCollection(uid)
            .document(listId)
            .update(
                mapOf(
                    "name" to name,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .await()
    }

    suspend fun deleteList(
        uid: String,
        listId: String
    ) {
        // Prevent deleting preset lists
        if (listId in listOf("want_to_go", "visited", "to_review")) {
            throw IllegalArgumentException("Default lists cannot be deleted.")
        }

        listsCollection(uid)
            .document(listId)
            .delete()
            .await()
    }

    suspend fun getAlreadyWentCountForUser(userId: String): Int {
        val snapshot = firestore
            .collection("users")
            .document(userId)
            .collection("lists")
            .document("visited")
            .collection("events")
            .get()
            .await()

        return snapshot.size()
    }

    fun observeAlreadyWentCountForUser(
        userId: String,
        onCountChanged: (Int) -> Unit
    ): ListenerRegistration {
        return firestore
            .collection("users")
            .document(userId)
            .collection("lists")
            .document("visited")
            .collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onCountChanged(0)
                    return@addSnapshotListener
                }

                onCountChanged(snapshot?.size() ?: 0)
            }
    }
}