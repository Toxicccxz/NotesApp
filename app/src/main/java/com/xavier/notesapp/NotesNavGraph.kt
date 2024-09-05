package com.xavier.notesapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xavier.notesapp.ui.AddNoteScreen
import com.xavier.notesapp.ui.NoteListScreen
import com.xavier.notesapp.ui.NoteViewModel

@Composable
fun NotesNavGraph(navController: NavHostController, noteViewModel: NoteViewModel) {
    NavHost(navController = navController, startDestination = "notesList") {
        // notesList
        composable("notesList") {
            NoteListScreen(navController = navController, noteViewModel = noteViewModel)
        }
        // addNotes
        composable("addNote") {
            AddNoteScreen(navController = navController, noteViewModel = noteViewModel)
        }
    }
}