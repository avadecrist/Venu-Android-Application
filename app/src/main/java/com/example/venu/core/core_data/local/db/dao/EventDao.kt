package com.example.venu.core.core_data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.venu.core.core_data.local.db.entity.EventEntity

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)
    /*  example usage:
     *  eventDao.insertEvents(FakeSeed.events.map { it.toEntity() })
    */

    // Good to have for future use
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    // Useful for debugging
    @Query("SELECT * FROM events")
    suspend fun getAllEvents(): List<EventEntity>

    // 'LIMIT 1' is important because each id is unique
    @Query("SELECT * FROM events WHERE id = :eventId LIMIT 1")
    suspend fun getEventById(eventId: String): EventEntity?

    // For search bar inquiries
    @Query("""
        SELECT * FROM events
        WHERE name LIKE '%' || :query || '%'
           OR subtitle LIKE '%' || :query || '%'
           OR genre LIKE '%' || :query || '%'
           OR locationName LIKE '%' || :query || '%'
    """)
    suspend fun searchEvents(query: String): List<EventEntity>

    // Sort events based on credibilityScore and then reviewCount
    @Query("""
        SELECT * FROM events
        ORDER BY credibilityScore DESC, reviewCount DESC
    """)
    suspend fun getTrendingEvents(): List<EventEntity>

    // Find events based on Genre
    @Query("SELECT * FROM events WHERE genre = :genre")
    suspend fun getEventsByGenre(genre: String): List<EventEntity>

    // Use for first-launch seeding
    @Query("SELECT COUNT(*) FROM events")
    suspend fun getCount(): Int
    /* example usage:
        if (eventDao.getCount() == 0) {
            // seed database
        }
    */

    // Useful for testing seed logic
    @Query("DELETE FROM events")
    suspend fun clearAll()
}