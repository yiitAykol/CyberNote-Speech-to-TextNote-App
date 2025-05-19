package com.example.cybernote

import android.app.Application
import androidx.room.Room
import com.example.cybernote.data.AppDatabase
import com.example.cybernote.data.NoteRepository

class CyberNoteApplication : Application() {

    // Database ve Repository'yi lazy init ile oluştur
    val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "notes-db" // Veritabanı adı
        ).build()
    }

    val noteRepository by lazy {
        NoteRepository(database.noteDao())
    }
}