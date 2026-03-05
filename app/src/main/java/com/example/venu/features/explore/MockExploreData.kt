package com.example.venu.features.explore

object MockExploreData {
    val places: List<PlaceUi> = listOf(
        PlaceUi("p1", "Cafe Luna", "Cozy • Coffee", 0.6, 4.6, ExploreGenre.Coffee, true, false),
        PlaceUi("p2", "Sushi Miko", "Fresh • Japanese", 1.2, 4.7, ExploreGenre.Food, true, true),
        PlaceUi("p3", "Bar Atlas", "Cocktails • Night", 2.4, 4.4, ExploreGenre.Nightlife, false, false),
        PlaceUi("p4", "Vinyl Room", "Live sets • DJ nights", 1.8, 4.5, ExploreGenre.Music, true, false),
        PlaceUi("p5", "North Library", "Quiet • Study", 0.9, 4.8, ExploreGenre.Study, false, false),
        PlaceUi("p6", "Riverside Trail", "Views • Sunset walk", 3.1, 4.3, ExploreGenre.Outdoors, false, false),
        PlaceUi("p7", "Taco Corner", "Quick • Street food", 1.0, 4.2, ExploreGenre.Food, false, true),
        PlaceUi("p8", "Blue Bottle Corner", "Espresso • Pour-over", 0.4, 4.6, ExploreGenre.Coffee, true, false),
        PlaceUi("p9", "Neon Basement", "EDM • Late night", 2.9, 4.1, ExploreGenre.Nightlife, true, false),
        PlaceUi("p10", "Park Study Pods", "Outdoor tables • Wi-Fi", 1.6, 4.0, ExploreGenre.Study, false, false)
    )
}

