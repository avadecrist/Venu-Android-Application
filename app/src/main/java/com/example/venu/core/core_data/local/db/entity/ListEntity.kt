package com.example.venu.core.core_data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lists",
    indices = [
        Index(value = ["uid"])
    ]
)
data class ListEntity(
    @PrimaryKey val listId: String,
    val uid: String,
    val title: String,
    val type: String, // "default" or "custom"
    val createdAt: Long,
    val updatedAt: Long
)