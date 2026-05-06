package com.example.venu.core.core_data.remote.firestore

import com.example.venu.core.core_domain.model.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventFirestoreRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val eventsCollection = firestore.collection("events")

    suspend fun createEvent(event: Event): Event {
        val now = System.currentTimeMillis()

        val dto = event.toFirestoreDto(
            createdAt = now,
            updatedAt = now
        )

        eventsCollection
            .document(event.id)
            .set(dto)
            .await()

        return event
    }

    suspend fun getEvent(eventId: String): Event? {
        val snapshot = eventsCollection
            .document(eventId)
            .get()
            .await()

        return snapshot
            .toObject(EventFirestoreDto::class.java)
            ?.toDomain()
    }

    suspend fun getAllEvents(): List<Event> {
        val snapshot = eventsCollection
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(EventFirestoreDto::class.java)?.toDomain()
        }
    }
}