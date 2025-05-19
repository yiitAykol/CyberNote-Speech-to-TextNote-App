package com.example.cybernote.data
import com.example.cybernote.data.NoteDao
import com.example.cybernote.data.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NoteRepository(private val noteDao: NoteDao) {
    // Tüm notları Flow ile dinle
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    // Not ekle
    suspend fun insert(note: Note) = withContext(Dispatchers.IO){
        noteDao.insert(note)
    }

}