package com.nikeboss.messenger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): List<Message>

    @Insert
    fun insertMessage(message: Message)
}
