package com.example.venu.core.core_data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.venu.core.core_data.local.db.dao.EventDao
import com.example.venu.core.core_data.local.db.entity.EventEntity

/* This class:
 * Declares the Room database
 * Lists the entities
 * Exposes our DAO
 * Creates a singleton DB instance
 */
@Database(
    entities = [EventEntity::class],
    version = 3,
    exportSchema = false
)
abstract class VenuLocalDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: VenuLocalDatabase? = null

        fun getDatabase(context: Context): VenuLocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VenuLocalDatabase::class.java,
                    "venu_local_database"
                )
                    // Development-only shortcut.
                    // This drops/rebuilds the local Room database when the schema changes.
                    // Keep this while we are actively changing EventEntity for real Google Places-backed events.
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}