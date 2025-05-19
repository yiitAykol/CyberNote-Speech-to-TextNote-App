package com.example.cybernote.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // autoGenerate için varsayılan değer 0 olmalı
    val title: String,
    val content: String,
    val date: Date
)