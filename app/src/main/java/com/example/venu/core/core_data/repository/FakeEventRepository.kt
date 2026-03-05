package com.example.venu.core.core_data.repository

import com.example.venu.core.core_data.fake.FakeSeed
import com.example.venu.core.core_domain.model.Category
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.repository.EventRepository

class FakeEventRepository : EventRepository {

    private val events = FakeSeed.events

    override fun getTrendingEvents(): List<Event> {
        // simple: sort by credibility + reviews
        return events.sortedByDescending { (it.credibilityScore * 2) + it.reviewCount }.take(10)
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
    }

    override fun getEventById(id: String): Event? = events.find { it.id == id }
}