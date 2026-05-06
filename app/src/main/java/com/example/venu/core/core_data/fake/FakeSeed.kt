package com.example.venu.core.core_data.fake

import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.PriceTier
import com.example.venu.core.core_domain.model.Review

object FakeSeed {

    val events = listOf(

        Event(
            id = "e1",
            eventName = "Indie Night at Tupperware",
            description = "Local DJs + indie dance floor",
            genre = Genre.MUSIC,
            locationName = "Tupperware Club",
            googlePlaceId = "TEMP_PLACE_ID_FOR_NOW",
            googlePlaceAddress = "??",
            googleRating = null,
            latitude = 40.4251,
            longitude = -3.7047,
            distanceKm = 0.2,
            priceTier = PriceTier.ONE,
            hours = "Tonight 11 PM",
            imageUrl = null,
            reviewCount = 6,
            venuRating = 4.6,
            isVerifiedVenue = true,
            interestLevel = 180
        ),

        Event(
            id = "e2",
            eventName = "Late Night Study Session",
            description = "Coffee + quiet tables",
            genre = Genre.STUDY,
            locationName = "HanSo Café",
            latitude = 40.4256,
            longitude = -3.7041,
            distanceKm = 0.1,
            priceTier = PriceTier.ONE,
            hours = "Tonight 9 PM",
            reviewCount = 5,
            venuRating = 4.5,
            interestLevel = 18
        ),

        Event(
            id = "e3",
            eventName = "Open Mic Comedy",
            description = "Student comedians + improv",
            genre = Genre.MUSIC,
            locationName = "La Vía Láctea",
            latitude = 40.4257,
            longitude = -3.7056,
            distanceKm = 0.3,
            priceTier = PriceTier.ONE,
            hours = "Fri 10 PM",
            reviewCount = 7,
            venuRating = 4.3,
            interestLevel = 75
        ),

        Event(
            id = "e4",
            eventName = "Tapas Crawl",
            description = "Student meet-up hopping tapas bars",
            genre = Genre.FOOD,
            locationName = "Plaza del Dos de Mayo",
            latitude = 40.4265,
            longitude = -3.7040,
            distanceKm = 0.2,
            priceTier = PriceTier.TWO,
            hours = "Tonight 8 PM",
            reviewCount = 6,
            venuRating = 4.5,
            interestLevel = 95
        ),

        Event(
            id = "e5",
            eventName = "Pickup Fútbol",
            description = "Casual street football",
            genre = Genre.SPORTS,
            locationName = "Plaza del Dos de Mayo",
            latitude = 40.4266,
            longitude = -3.7038,
            distanceKm = 0.2,
            priceTier = PriceTier.FREE,
            hours = "Tomorrow 6 PM",
            reviewCount = 5,
            venuRating = 4.2,
            interestLevel = 22
        ),

        Event(
            id = "e6",
            eventName = "Coffee & Coding",
            description = "Students building side projects",
            genre = Genre.STUDY,
            locationName = "Ruda Café",
            latitude = 40.4246,
            longitude = -3.7039,
            distanceKm = 0.3,
            priceTier = PriceTier.ONE,
            hours = "Sat 10 AM",
            reviewCount = 6,
            venuRating = 4.6,
            interestLevel = 15
        ),

        Event(
            id = "e7",
            eventName = "Underground DJ Set",
            description = "House + techno night",
            genre = Genre.MUSIC,
            locationName = "Sala Maravillas",
            latitude = 40.4260,
            longitude = -3.7051,
            distanceKm = 0.3,
            priceTier = PriceTier.TWO,
            hours = "Sat 11 PM",
            reviewCount = 7,
            venuRating = 4.4,
            interestLevel = 210
        ),

        Event(
            id = "e8",
            eventName = "Taco Corner",
            description = "Quick street tacos",
            genre = Genre.FOOD,
            locationName = "Taco Corner",
            latitude = 40.4194,
            longitude = -3.7032,
            distanceKm = 1.0,
            priceTier = PriceTier.ONE,
            hours = "Open now",
            reviewCount = 70,
            venuRating = 4.2,
            interestLevel = 65
        ),

        Event(
            id = "e9",
            eventName = "North Library",
            description = "Quiet study environment",
            genre = Genre.STUDY,
            locationName = "North Library",
            latitude = 40.4240,
            longitude = -3.7021,
            distanceKm = 0.9,
            priceTier = PriceTier.FREE,
            hours = "Open until 10 PM",
            reviewCount = 140,
            venuRating = 4.8,
            interestLevel = 40
        ),

        Event(
            id = "e10",
            eventName = "Vinyl Room",
            description = "Live DJs and music sets",
            genre = Genre.MUSIC,
            locationName = "Vinyl Room",
            latitude = 40.4211,
            longitude = -3.7075,
            distanceKm = 1.8,
            priceTier = PriceTier.ONE,
            hours = "Tonight 10 PM",
            reviewCount = 63,
            venuRating = 4.5,
            interestLevel = 120
        ),

        Event(
            id = "e11",
            eventName = "Bar Atlas",
            description = "Cocktails and nightlife",
            genre = Genre.NIGHTLIFE,
            locationName = "Bar Atlas",
            latitude = 40.4202,
            longitude = -3.7061,
            distanceKm = 2.4,
            priceTier = PriceTier.FREE,
            hours = "Tonight 9 PM",
            reviewCount = 85,
            venuRating = 4.4,
            interestLevel = 140
        ),

        Event(
            id = "e12",
            eventName = "Sushi Miko",
            description = "Fresh Japanese cuisine",
            genre = Genre.FOOD,
            locationName = "Sushi Miko",
            latitude = 40.4175,
            longitude = -3.7050,
            distanceKm = 1.2,
            priceTier = PriceTier.ONE,
            hours = "Dinner hours",
            reviewCount = 210,
            venuRating = 4.7,
            interestLevel = 180
        ),

        Event(
            id = "e13",
            eventName = "Blue Bottle Corner",
            description = "Espresso and pour-over",
            genre = Genre.COFFEE,
            locationName = "Blue Bottle",
            latitude = 40.4162,
            longitude = -3.7044,
            distanceKm = 0.4,
            priceTier = PriceTier.ONE,
            hours = "Open now",
            reviewCount = 165,
            venuRating = 4.6,
            interestLevel = 85
        ),

        Event(
            id = "e14",
            eventName = "Neon Basement",
            description = "EDM dance floor",
            genre = Genre.NIGHTLIFE,
            locationName = "Neon Basement",
            latitude = 40.4226,
            longitude = -3.7068,
            distanceKm = 2.9,
            priceTier = PriceTier.TWO,
            hours = "Tonight 11 PM",
            reviewCount = 76,
            venuRating = 4.1,
            interestLevel = 230
        ),

        Event(
            id = "e15",
            eventName = "Park Study Pods",
            description = "Outdoor study tables with Wi-Fi",
            genre = Genre.STUDY,
            locationName = "City Park",
            latitude = 40.4235,
            longitude = -3.7010,
            distanceKm = 1.6,
            priceTier = PriceTier.FREE,
            hours = "Open now",
            reviewCount = 54,
            venuRating = 4.0,
            interestLevel = 28
        )
    )

    val reviews = listOf(
        Review("r1", "e1", null, "user_2", "Alex", "", 5, "Great DJ set.", 9, "2d ago"),
        Review("r2", "e1", null, "user_3", "Jordan", "", 4, "Fun crowd.", 9, "3d ago"),
        Review("r3", "e1", null, "user_4", "Maya", "", 5, "One of the best nights out.", 9, "4d ago"),
        Review("r4", "e1", null, "user_5", "Chris", "", 4, "Packed but worth it.", 9, "5d ago"),
        Review("r5", "e1", null, "user_6", "Sam", "", 5, "Love this place.", 9, "1w ago"),
        Review("r6", "e1", null, "user_7", "Taylor", "", 4, "Great indie music.", 9, "1w ago")
    )
}