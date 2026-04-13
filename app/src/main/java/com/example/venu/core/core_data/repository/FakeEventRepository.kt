package com.example.venu.core.core_data.repository

import com.example.venu.core.core_data.fake.FakeSeed
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.EventRepository

class FakeEventRepository : EventRepository {

     private val events = FakeSeed.events

    override suspend fun getTrendingEvents(): List<Event> {
        return events
            .sortedWith(
                compareByDescending<Event> { it.credibilityScore } // sort based on credibilityScore
                    .thenByDescending { it.reviewCount } // if tied, then sort by number of reviews
            )
            .take(10) // displays first 10
    }

    /* NOTE: when we use user's GPS location later, this method will be tweaked:
    *   1. We’ll get the user’s lat/lng (FusedLocationProviderClient)
    *   2. Change the interface to accept location
    *   ex. declaration: fun getNearbyEvents(userLat: Double, userLng: Double): List<Event>
    *   3. Compute distance(userLocation, eventLocation) with Location.distanceBetween()
    */
    override suspend fun getNearbyEvents(): List<Event> {
        // Explore's default list
        val hasAnyDistance = events.any { it.distanceKm != null } // true if distanceMiles has a value

        return if (hasAnyDistance) {
            events
                .sortedWith(
                compareBy<Event> { it.distanceKm ?: Double.MAX_VALUE } // sort by distance
                    .thenByDescending { it.credibilityScore } // if same distance, then sort by credibility
                )
                .take(20) // displays first 20 events
        } else {
            // fallback if distances aren’t seeded yet
            getTrendingEvents()
        }
    }

    override suspend fun getEventsByCategory(genre: Genre): List<Event> {
        return events.filter { it.genre == genre }
    }

    override suspend fun searchEvents(query: String, categories: Set<Genre>): List<Event> {
        val q = query.trim().lowercase()

        return events.filter { e ->
            val matchesQuery = q.isBlank() ||
                    e.name.lowercase().contains(q) ||
                    e.subtitle.lowercase().contains(q) ||
                    e.locationName.lowercase().contains(q)

            val matchesCategory = categories.isEmpty() || categories.contains(e.genre)

            matchesQuery && matchesCategory
        }
            // SORT results so "best" shows first
            .sortedWith(
                compareByDescending<Event> { it.credibilityScore }
                    .thenByDescending { it.reviewCount }
            )
    }

    override suspend fun getEventById(id: String): Event? {
        return events.find { it.id == id }
    }
}