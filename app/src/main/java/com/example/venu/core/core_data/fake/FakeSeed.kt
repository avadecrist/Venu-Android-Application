package com.example.venu.core.core_data.fake

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.PriceTier
import com.example.venu.core.core_domain.model.Review
import com.example.venu.core.core_domain.model.CrowdLevel

// Temporary Database
/* ???
    suspend fun seedIfEmpty() {
        if (eventDao.getCount() == 0) {
            eventDao.insertEvents(FakeSeed.events.map { it.toEntity() })
        }
    }
 */

object FakeSeed {

    val events = listOf(

        Event(
            id = "e1",
            name = "Indie Night at Tupperware",
            subtitle = "Local DJs + indie dance floor",
            genre = Genre.MUSIC,
            locationName = "Tupperware Club",
            latitude = 40.4251,
            longitude = -3.7047,
            distanceKm = 0.2,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Tonight 11 PM",
            credibilityScore = 91,
            reviewCount = 6,
            averageRating = 4.6,
            crowdLevel = CrowdLevel.PACKED,
            attendeeCount = 180
        ),

        Event(
            id = "e2",
            name = "Late Night Study Session",
            subtitle = "Coffee + quiet tables",
            genre = Genre.STUDY,
            locationName = "HanSo Café",
            latitude = 40.4256,
            longitude = -3.7041,
            distanceKm = 0.1,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Tonight 9 PM",
            credibilityScore = 88,
            reviewCount = 5,
            averageRating = 4.5,
            crowdLevel = CrowdLevel.LIGHT,
            attendeeCount = 18
        ),

        Event(
            id = "e3",
            name = "Open Mic Comedy",
            subtitle = "Student comedians + improv",
            genre = Genre.MUSIC,
            locationName = "La Vía Láctea",
            latitude = 40.4257,
            longitude = -3.7056,
            distanceKm = 0.3,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Fri 10 PM",
            credibilityScore = 83,
            reviewCount = 7,
            averageRating = 4.3,
            crowdLevel = CrowdLevel.BUSY,
            attendeeCount = 75
        ),

        Event(
            id = "e4",
            name = "Tapas Crawl",
            subtitle = "Student meet-up hopping tapas bars",
            genre = Genre.FOOD,
            locationName = "Plaza del Dos de Mayo",
            latitude = 40.4265,
            longitude = -3.7040,
            distanceKm = 0.2,
            priceTier = PriceTier.UNDER_20,
            startTimeLabel = "Tonight 8 PM",
            credibilityScore = 87,
            reviewCount = 6,
            averageRating = 4.5,
            crowdLevel = CrowdLevel.BUSY,
            attendeeCount = 95
        ),

        Event(
            id = "e5",
            name = "Pickup Fútbol",
            subtitle = "Casual street football",
            genre = Genre.SPORTS,
            locationName = "Plaza del Dos de Mayo",
            latitude = 40.4266,
            longitude = -3.7038,
            distanceKm = 0.2,
            priceTier = PriceTier.FREE,
            startTimeLabel = "Tomorrow 6 PM",
            credibilityScore = 80,
            reviewCount = 5,
            averageRating = 4.2,
            crowdLevel = CrowdLevel.LIGHT,
            attendeeCount = 22
        ),

        Event(
            id = "e6",
            name = "Coffee & Coding",
            subtitle = "Students building side projects",
            genre = Genre.STUDY,
            locationName = "Ruda Café",
            latitude = 40.4246,
            longitude = -3.7039,
            distanceKm = 0.3,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Sat 10 AM",
            credibilityScore = 89,
            reviewCount = 6,
            averageRating = 4.6,
            crowdLevel = CrowdLevel.LIGHT,
            attendeeCount = 15
        ),

        Event(
            id = "e7",
            name = "Underground DJ Set",
            subtitle = "House + techno night",
            genre = Genre.MUSIC,
            locationName = "Sala Maravillas",
            latitude = 40.4260,
            longitude = -3.7051,
            distanceKm = 0.3,
            priceTier = PriceTier.UNDER_20,
            startTimeLabel = "Sat 11 PM",
            credibilityScore = 84,
            reviewCount = 7,
            averageRating = 4.4,
            crowdLevel = CrowdLevel.PACKED,
            attendeeCount = 210
        ),

        Event(
            id = "e8",
            name = "Taco Corner",
            subtitle = "Quick street tacos",
            genre = Genre.FOOD,
            locationName = "Taco Corner",
            latitude = 40.4194,
            longitude = -3.7032,
            distanceKm = 1.0,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Open now",
            credibilityScore = 83,
            reviewCount = 70,
            averageRating = 4.2,
            crowdLevel = CrowdLevel.BUSY,
            attendeeCount = 65
        ),

        Event(
            id = "e9",
            name = "North Library",
            subtitle = "Quiet study environment",
            genre = Genre.STUDY,
            locationName = "North Library",
            latitude = 40.4240,
            longitude = -3.7021,
            distanceKm = 0.9,
            priceTier = PriceTier.FREE,
            startTimeLabel = "Open until 10 PM",
            credibilityScore = 87,
            reviewCount = 140,
            averageRating = 4.8,
            crowdLevel = CrowdLevel.LIGHT,
            attendeeCount = 40
        ),

        Event(
            id = "e10",
            name = "Vinyl Room",
            subtitle = "Live DJs and music sets",
            genre = Genre.MUSIC,
            locationName = "Vinyl Room",
            latitude = 40.4211,
            longitude = -3.7075,
            distanceKm = 1.8,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Tonight 10 PM",
            credibilityScore = 89,
            reviewCount = 63,
            averageRating = 4.5,
            crowdLevel = CrowdLevel.BUSY,
            attendeeCount = 120
        ),

        Event(
            id = "e11",
            name = "Bar Atlas",
            subtitle = "Cocktails and nightlife",
            genre = Genre.NIGHTLIFE,
            locationName = "Bar Atlas",
            latitude = 40.4202,
            longitude = -3.7061,
            distanceKm = 2.4,
            priceTier = PriceTier.FREE,
            startTimeLabel = "Tonight 9 PM",
            credibilityScore = 80,
            reviewCount = 85,
            averageRating = 4.4,
            crowdLevel = CrowdLevel.BUSY,
            attendeeCount = 140
        ),

        Event(
            id = "e12",
            name = "Sushi Miko",
            subtitle = "Fresh Japanese cuisine",
            genre = Genre.FOOD,
            locationName = "Sushi Miko",
            latitude = 40.4175,
            longitude = -3.7050,
            distanceKm = 1.2,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Dinner hours",
            credibilityScore = 95,
            reviewCount = 210,
            averageRating = 4.7,
            crowdLevel = CrowdLevel.PACKED,
            attendeeCount = 180
        ),

        Event(
            id = "e13",
            name = "Blue Bottle Corner",
            subtitle = "Espresso and pour-over",
            genre = Genre.COFFEE,
            locationName = "Blue Bottle",
            latitude = 40.4162,
            longitude = -3.7044,
            distanceKm = 0.4,
            priceTier = PriceTier.UNDER_10,
            startTimeLabel = "Open now",
            credibilityScore = 90,
            reviewCount = 165,
            averageRating = 4.6,
            crowdLevel = CrowdLevel.BUSY,
            attendeeCount = 85
        ),

        Event(
            id = "e14",
            name = "Neon Basement",
            subtitle = "EDM dance floor",
            genre = Genre.NIGHTLIFE,
            locationName = "Neon Basement",
            latitude = 40.4226,
            longitude = -3.7068,
            distanceKm = 2.9,
            priceTier = PriceTier.UNDER_20,
            startTimeLabel = "Tonight 11 PM",
            credibilityScore = 88,
            reviewCount = 76,
            averageRating = 4.1,
            crowdLevel = CrowdLevel.PACKED,
            attendeeCount = 230
        ),

        Event(
            id = "e15",
            name = "Park Study Pods",
            subtitle = "Outdoor study tables with Wi-Fi",
            genre = Genre.STUDY,
            locationName = "City Park",
            latitude = 40.4235,
            longitude = -3.7010,
            distanceKm = 1.6,
            priceTier = PriceTier.FREE,
            startTimeLabel = "Open now",
            credibilityScore = 82,
            reviewCount = 54,
            averageRating = 4.0,
            crowdLevel = CrowdLevel.LIGHT,
            attendeeCount = 28
        )
    )

    val reviews = listOf(
        Review("r1","e1","user_2","Alex",5,"Great DJ set.","2d ago"),
        Review("r2","e1","user_3","Jordan",4,"Fun crowd.","3d ago"),
        Review("r3","e1","user_4","Maya",5,"One of the best nights out.","4d ago"),
        Review("r4","e1","user_5","Chris",4,"Packed but worth it.","5d ago"),
        Review("r5","e1","user_6","Sam",5,"Love this place.","1w ago"),
        Review("r6","e1","user_7","Taylor",4,"Great indie music.","1w ago")
    )
}