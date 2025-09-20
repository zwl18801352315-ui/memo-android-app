package com.example.memoapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>
    
    init {
        val noteDao = AppDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes.asLiveData()
    }
    
    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }
    
    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }
    
    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
    
    suspend fun getNoteById(id: Int): Note? {
        return repository.getNoteById(id)
    }
}

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes = noteDao.getAllNotes()
    
    suspend fun insert(note: Note) {
        noteDao.insertNote(note)
    }
    
    suspend fun update(note: Note) {
        noteDao.updateNote(note)
    }
    
    suspend fun delete(note: Note) {
        noteDao.deleteNote(note)
    }
    
    suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }
}
