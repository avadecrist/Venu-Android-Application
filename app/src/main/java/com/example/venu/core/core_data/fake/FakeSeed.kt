package com.example.venu.core.core_data.fake

import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.Event
import com.example.venu.core.core_domain.model.PriceTier
import com.example.venu.core.core_domain.model.Review

// Temporary Database
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
            averageRating = 4.6
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
            averageRating = 4.5
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
            averageRating = 4.3
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
            averageRating = 4.5
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
            averageRating = 4.2
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
            averageRating = 4.6
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
            averageRating = 4.4
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
            averageRating = 4.2
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
            averageRating = 4.8
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
            averageRating = 4.5
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
            averageRating = 4.4
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
            averageRating = 4.7
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
            averageRating = 4.6
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
            averageRating = 4.1
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
            averageRating = 4.0
        )
    )

    val reviews = listOf(

        Review("r1","e1","user_2","Alex",5,"Great DJ set.","2d ago"),
        Review("r2","e1","user_3","Jordan",4,"Fun crowd.","3d ago"),
        Review("r3","e1","user_4","Maya",5,"One of the best nights out.","4d ago"),
        Review("r4","e1","user_5","Chris",4,"Packed but worth it.","5d ago"),
        Review("r5","e1","user_6","Sam",5,"Love this place.","1w ago"),
        Review("r6","e1","user_7","Taylor",4,"Great indie music.","1w ago"),

        Review("r7","e2","user_2","Alex",5,"Perfect study spot.","2d ago"),
        Review("r8","e2","user_3","Jordan",4,"Good coffee.","3d ago"),
        Review("r9","e2","user_4","Maya",5,"Very productive night.","5d ago"),
        Review("r10","e2","user_5","Chris",4,"Good vibe.","6d ago"),
        Review("r11","e2","user_6","Sam",5,"Quiet enough to focus.","1w ago"),

        Review("r12","e3","user_3","Jordan",4,"Comedy was hilarious.","2d ago"),
        Review("r13","e3","user_5","Chris",5,"Amazing crowd.","3d ago"),
        Review("r14","e3","user_6","Sam",4,"Good performers.","4d ago"),
        Review("r15","e3","user_7","Taylor",4,"Fun event.","5d ago"),
        Review("r16","e3","user_8","Riley",5,"Loved it.","6d ago"),
        Review("r17","e3","user_9","Drew",4,"Would go again.","1w ago"),

        Review("r18","e4","user_2","Alex",5,"Best tapas night.","2d ago"),
        Review("r19","e4","user_3","Jordan",4,"Great social event.","3d ago"),
        Review("r20","e4","user_4","Maya",5,"Met lots of people.","5d ago"),
        Review("r21","e4","user_5","Chris",4,"Fun bar hopping.","6d ago"),
        Review("r22","e4","user_6","Sam",5,"Amazing vibe.","1w ago"),

        Review("r23","e5","user_3","Jordan",4,"Great game.","2d ago"),
        Review("r24","e5","user_4","Maya",5,"Super fun.","3d ago"),
        Review("r25","e5","user_6","Sam",4,"Good players.","5d ago"),
        Review("r26","e5","user_7","Taylor",4,"Would play again.","6d ago"),
        Review("r27","e5","user_8","Riley",4,"Nice atmosphere.","1w ago"),

        Review("r28","e6","user_4","Maya",5,"Great place to code.","2d ago"),
        Review("r29","e6","user_5","Chris",4,"Nice dev crowd.","3d ago"),
        Review("r30","e6","user_6","Sam",5,"Very productive.","5d ago"),
        Review("r31","e6","user_7","Taylor",4,"Good wifi.","6d ago"),
        Review("r32","e6","user_8","Riley",5,"Great coffee too.","1w ago"),

        Review("r33","e7","user_2","Alex",5,"Amazing DJ.","2d ago"),
        Review("r34","e7","user_3","Jordan",4,"Loved the music.","3d ago"),
        Review("r35","e7","user_4","Maya",5,"Great crowd.","4d ago"),
        Review("r36","e7","user_6","Sam",4,"Fun night.","5d ago"),
        Review("r37","e7","user_8","Riley",4,"Would go again.","6d ago"),
        Review("r38","e7","user_9","Drew",5,"Best techno night.","1w ago")
    )
}