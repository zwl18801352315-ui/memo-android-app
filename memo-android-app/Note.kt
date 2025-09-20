package com.example.memoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
