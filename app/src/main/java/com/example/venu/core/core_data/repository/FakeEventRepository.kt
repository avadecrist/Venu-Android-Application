package com.example.venu.core.core_data.repository

import com.example.venu.core.core_data.fake.FakeSeed
import com.example.venu.core.core_domain.model.Category
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.EventRepository

class FakeEventRepository : EventRepository {

    private val events = FakeSeed.events

    override fun getTrendingEvents(): List<Event> {
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
    override fun getNearbyEvents(): List<Event> {
        // Explore's default list
        val hasAnyDistance = events.any { it.distanceMiles != null } // true if distanceMiles has a value

        return if (hasAnyDistance) {
            events
                .sortedWith(
                compareBy<Event> { it.distanceMiles ?: Double.MAX_VALUE } // sort by distance
                    .thenByDescending { it.credibilityScore } // if same distance, then sort by credibility
                )
                .take(20) // displays first 20 events
        } else {
            // fallback if distances aren’t seeded yet
            getTrendingEvents()
        }
    }

    override fun getEventsByCategory(category: Category): List<Event> {
        return events.filter { it.category == category }
    }

    override fun searchEvents(query: String, categories: Set<Category>): List<Event> {
        val q = query.trim().lowercase()

        return events.filter { e ->
            val matchesQuery = q.isBlank() ||
                    e.title.lowercase().contains(q) ||
                    e.subtitle.lowercase().contains(q) ||
                    e.locationName.lowercase().contains(q)

            val matchesCategory = categories.isEmpty() || categories.contains(e.category)

            matchesQuery && matchesCategory
        }
            // SORT results so "best" shows first
            .sortedWith(
                compareByDescending<Event> { it.credibilityScore }
                    .thenByDescending { it.reviewCount }
            )
    }

    override fun getEventById(id: String): Event? {
        return events.find { it.id == id }
    }
}