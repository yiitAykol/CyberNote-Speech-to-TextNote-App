package com.example.cybernote.view
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
import com.example.cybernote.CyberNoteApplication
import com.example.cybernote.data.NoteRepository
import com.example.cybernote.data.Note
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    // Repository'ye erişim
    //private val repository = (application as CyberNoteApplication).noteRepository
    private val repository: NoteRepository by lazy {
        (getApplication<CyberNoteApplication>()).noteRepository
    }
    // Tüm notları dinle (Flow/LiveData)
    val allNotes = repository.allNotes

    // Not ekleme fonksiyonu
    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }
    /*
    // Not silme fonksiyonu (Opsiyonel)
    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }*/
}