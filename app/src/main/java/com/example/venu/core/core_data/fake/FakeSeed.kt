package com.example.venu.core.core_data.fake

import com.example.venu.core.core_domain.model.Category
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.PriceTier
import com.example.venu.core.core_domain.model.Review

object FakeSeed {

    val events: List<Event> = listOf(
        Event(
            id = "e1",
            title = "Sunset Volleyball",
            subtitle = "Student-run pickup games",
            category = Category.SPORTS,
            locationName = "Campus Courts",
            distanceMiles = 0.4,
            priceTier = PriceTier.FREE,
            startTimeLabel = "Today 5 PM",
            credibilityScore = 82,
            reviewCount = 19,
            averageRating = 4.6
        ),
        Event(
            id = "e2",
            title = "Coffee + Study Sprint",
            subtitle = "Quiet tables + lo-fi playlist",
            category = Category.STUDY,
            locationName = "Library 2nd Floor",
            distanceMiles = 0.2,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Tonight 7 PM",
            credibilityScore = 74,
            reviewCount = 8,
            averageRating = 4.2
        ),
        Event(
            id = "e3",
            title = "Open Mic Night",
            subtitle = "Music, comedy, and poetry",
            category = Category.MUSIC,
            locationName = "Student Union Patio",
            distanceMiles = 0.6,
            priceTier = PriceTier.FREE,
            startTimeLabel = "Fri 8 PM",
            credibilityScore = 67,
            reviewCount = 31,
            averageRating = 4.4
        )
    )

    val reviews: List<Review> = listOf(
        Review(
            id = "r1",
            eventId = "e1",
            authorName = "Maya",
            rating = 5,
            comment = "Actually well organized and friendly.",
            createdAtLabel = "2d ago"
        ),
        Review(
            id = "r2",
            eventId = "e2",
            authorName = "Ethan",
            rating = 4,
            comment = "Good vibe. Wish there were more outlets.",
            createdAtLabel = "5d ago"
        ),
        Review(
            id = "r3",
            eventId = "e3",
            authorName = "Jules",
            rating = 5,
            comment = "So fun — legit talent showed up.",
            createdAtLabel = "1w ago"
        )
    )
}