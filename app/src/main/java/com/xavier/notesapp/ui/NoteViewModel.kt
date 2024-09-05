package com.xavier.notesapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xavier.notesapp.data.db.Note
import com.xavier.notesapp.data.db.database.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao = NoteDatabase.getDatabase(application).noteDao()
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    fun addNote(note: String) = viewModelScope.launch(Dispatchers.IO) {
        noteDao.insert(Note(content = note))
    }

    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteDao.delete(note)
    }
}