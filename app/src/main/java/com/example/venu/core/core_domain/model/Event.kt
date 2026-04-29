package com.example.venu.core.core_domain.model

data class Event(
    val id: String,
// not visible in Event Details UI
// purely for internal use (navigation, DB, caching, etc.)

    val name: String,
// PRIMARY title of the place or venue (the "anchor")
// ex: "Dubliners", "Santiago Bernabéu", "Blue Bottle Coffee"
// Think: what users recognize first when scanning

    val subtitle: String,
// SPECIFIC experience happening at that place
// ex: "Barca v Madrid Futbol Match", "Study Jam", "Live DJ Night"
// Think: what’s happening RIGHT NOW or what makes this listing interesting
// UI hierarchy: name (big) → subtitle (smaller, descriptive)

    val genre: Genre,
// include in Event Details card UI
// use as a visual (i.e FOOD 🍔, STUDY 📚, NIGHTLIFE 🌙)
// use for quick scanning + filtering

    val locationName: String,
// Location/Host Name — KEEP this as the venue name (same as "name" OR supporting detail)
// BUT: you should ALSO have an address field in the future
// For now:
// - show this as secondary location context OR replace with address later
// Ideal future setup:
// val venueName + val addressLine
// UI: "Dubliners • 0.8 km away"

    val latitude: Double,
    val longitude: Double,
// not visible directly but used for:
// - map view
// - calculating distanceKm
// - sorting "near you"

    val distanceKm: Double?,
// make it friendly (round to 1 decimal)

    val priceTier: PriceTier,
// show in Event Details UI
// keep it simple and visual (ex: $, $$, $$$)
// helps users instantly filter affordability

    val startTimeLabel: String,
// WHERE TO GET THIS:
// 1. Hardcoded in fake seed data (for now)
// 2. Later from APIs (Google Places, Eventbrite, Meetup, etc.)

    val imageUrl: String? = null,
// Event image (Google Places API later)
// show as large hero image at top of EventDetails UI
// fallback: placeholder if null

    val credibilityScore: Int = 0,
// GREAT feature — this is your differentiation
// show as circular badge (0–100)
// color-coded:
// 80–100 → green (very trustworthy)
// 60–79 → yellow
// <60 → red
// calculated PURELY from your app’s reviews (NOT Google)

    val reviewCount: Int = 0,
// User reviews, not from google
// ex: "24 reviews"
// gives weight to credibilityScore + averageRating

    val isVerifiedVenue: Boolean = false,
// uses:
// - show a "Verified" badge ✅
// - prevent fake listings
// - build trust (like Airbnb/Instagram)
// DO NOT remove — this is a scaling feature

    val averageRating: Double = 0.0,
// displayed as stars (ex: ⭐ 4.3)
// Calculation:
// Option A (better UX clarity):
// - show BOTH:
//   "4.5 ⭐ (Google)"
//   "4.2 ⭐ (VENU)"
// Option B (simpler):
// - weighted average (70% Google, 30% VENU early on)
// because early-stage = low user reviews
    val attendeeCount: Int = 0,
    val crowdLevel: CrowdLevel = CrowdLevel.UNKNOWN,
//    val metrics: EventMetrics = EventMetrics()
    // will use this later to establish crowd level
)

data class EventMetrics(
    val goingCount: Int? = null,
    val saveCount: Int? = null,
    val viewCount: Int? = null,
    val checkInCount: Int? = null,
    val estimatedCapacity: Int? = null
)
