package com.nikeboss.messenger.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val sender: String,
    val timestamp: Long = System.currentTimeMillis()
)
