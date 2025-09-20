package com.example.memoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "memos")
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val isCompleted: Boolean = false,
    val createdAt: Long = Date().time,
    val updatedAt: Long = Date().time
)
