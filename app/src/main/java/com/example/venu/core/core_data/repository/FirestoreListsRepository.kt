package com.example.venu.core.core_data.repository

import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.EventRepository
import com.example.venu.core.core_domain.repository.ListType
import com.example.venu.core.core_domain.repository.ListsRepository
import com.example.venu.core.core_data.remote.firestore.UserListFirestoreDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreListsRepository(
    private val eventRepo: EventRepository,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ListsRepository {

    private fun currentUid(): String {
        return firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in to use lists.")
    }

    private fun listsCollection() =
        firestore
            .collection("users")
            .document(currentUid())
            .collection("lists")

    private fun listDocumentId(type: ListType): String {
        return when (type) {
            ListType.WantToGo -> "want_to_go"
            ListType.AlreadyWent -> "visited"
            ListType.ToReview -> "to_review"
            is ListType.Custom -> type.id
        }
    }

    private fun eventsCollection(type: ListType) =
        listsCollection()
            .document(listDocumentId(type))
            .collection("events")

    override suspend fun getList(type: ListType): List<Event> {
        val snapshot = eventsCollection(type)
            .get()
            .await()

        val eventIds = snapshot.documents.map { document ->
            document.id
        }

        return eventIds.mapNotNull { eventId ->
            eventRepo.getEventById(eventId)
        }
    }

    override suspend fun addToList(type: ListType, eventId: String) {
        val now = System.currentTimeMillis()

        eventsCollection(type)
            .document(eventId)
            .set(
                mapOf(
                    "eventId" to eventId,
                    "addedAt" to now
                )
            )
            .await()

        listsCollection()
            .document(listDocumentId(type))
            .update(
                mapOf(
                    "updatedAt" to now
                )
            )
            .await()
    }

    override suspend fun removeFromList(type: ListType, eventId: String) {
        eventsCollection(type)
            .document(eventId)
            .delete()
            .await()

        listsCollection()
            .document(listDocumentId(type))
            .update(
                mapOf(
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .await()
    }

    override suspend fun moveEvent(eventId: String, from: ListType, to: ListType) {
        removeFromList(from, eventId)
        addToList(to, eventId)
    }

    override suspend fun isInList(type: ListType, eventId: String): Boolean {
        val snapshot = eventsCollection(type)
            .document(eventId)
            .get()
            .await()

        return snapshot.exists()
    }

    override suspend fun isSaved(eventId: String): Boolean {
        val lists = getAllLists()

        return lists.any { list ->
            isInList(list, eventId)
        }
    }

    override suspend fun toggleWantToGo(eventId: String) {
        if (isInList(ListType.WantToGo, eventId)) {
            removeFromList(ListType.WantToGo, eventId)
        } else {
            addToList(ListType.WantToGo, eventId)
        }
    }

    override suspend fun createCustomList(name: String): ListType.Custom {
        val now = System.currentTimeMillis()
        val docRef = listsCollection().document()

        val customList = UserListFirestoreDto(
            id = docRef.id,
            name = name,
            type = "custom",
            ownerId = currentUid(),
            createdAt = now,
            updatedAt = now,
            itemCount = 0
        )

        docRef.set(customList).await()

        return ListType.Custom(
            id = docRef.id,
            name = name
        )
    }

    override suspend fun deleteCustomList(listId: String) {
        listsCollection()
            .document(listId)
            .delete()
            .await()
    }

    override suspend fun getAllLists(): List<ListType> {
        val snapshot = listsCollection()
            .get()
            .await()

        val lists = snapshot.toObjects(UserListFirestoreDto::class.java)

        val builtIns = listOf(
            ListType.WantToGo,
            ListType.AlreadyWent,
            ListType.ToReview
        )

        val customLists = lists
            .filter { it.type == "custom" }
            .map { list ->
                ListType.Custom(
                    id = list.id,
                    name = list.name
                )
            }

        return builtIns + customLists
    }
}