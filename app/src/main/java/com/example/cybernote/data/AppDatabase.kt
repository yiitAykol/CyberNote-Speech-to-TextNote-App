package com.example.cybernote.data
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class) // Bu satırı ekleyin
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}