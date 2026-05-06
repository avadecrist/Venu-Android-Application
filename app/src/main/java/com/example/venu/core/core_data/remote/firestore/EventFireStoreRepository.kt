package com.example.venu.core.core_data.remote.firestore

import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.repository.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EventFirestoreRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : EventRepository {
    private val eventsCollection = firestore.collection("events")

    override suspend fun createEvent(event: Event) { //might need to return an event?
        val now = System.currentTimeMillis()

        val dto = event.toFirestoreDto(
            createdAt = now,
            updatedAt = now
        )

        eventsCollection
            .document(event.id)
            .set(dto)
            .await()
    }

     override suspend fun getEventById(id: String): Event? {
        val snapshot = eventsCollection
            .document(id)
            .get()
            .await()

        return snapshot
            .toObject(EventFirestoreDto::class.java)
            ?.toDomain()
    }

    override suspend fun getTrendingEvents(): List<Event> {
        return getAllEvents()
            .sortedByDescending { it.venuRating } // SORT BY VENU RATING NOW
            .take(10)
    }

    override suspend fun getNearbyEvents(): List<Event> {
        return getAllEvents()
    }

    override suspend fun getEventsByCategory(genre: Genre): List<Event> {
        return getAllEvents()
            .filter { it.genre == genre }
    }

    override suspend fun searchEvents(
        query: String,
        categories: Set<Genre>
    ): List<Event> {
        val normalizedQuery = query.trim().lowercase()

        return getAllEvents().filter { event ->
            val matchesQuery =
                normalizedQuery.isBlank() ||
                        event.eventName.lowercase().contains(normalizedQuery) ||
                        event.description.lowercase().contains(normalizedQuery) ||
                        event.locationName.lowercase().contains(normalizedQuery)

            val matchesCategory =
                categories.isEmpty() || event.genre in categories

            matchesQuery && matchesCategory
        }
    }

    private suspend fun getAllEvents(): List<Event> {
        val snapshot = eventsCollection
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(EventFirestoreDto::class.java)?.toDomain()
        }
    }
}