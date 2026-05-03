package com.example.venu.core.core_data.local.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "list_event_cross_refs",
    primaryKeys = ["listId", "eventId"],
    indices = [
        Index(value = ["eventId"])
    ]
)
data class ListEventCrossRef(
    val listId: String,
    val eventId: String,
    val savedAt: Long
)