package com.xavier.notesapp.ui

import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.xavier.notesapp.data.db.Note
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteListScreen(navController: NavController, noteViewModel: NoteViewModel) {
    val notes = noteViewModel.allNotes.collectAsState(initial = emptyList()).value
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addNote") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        topBar = {
            TopAppBar(title = { Text("Notes") })
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(notes) { note ->
                Card(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // body of notes and timestamp
                        Column(modifier = Modifier.weight(1f)) {
                            // notes
                            //compose ui
                            Text(
                                text = note.content,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            //android textview for marquee
//                            AndroidView(
//                                modifier = Modifier.fillMaxWidth(),
//                                factory = { context ->
//                                    TextView(context).apply {
//                                        text = note.content
//                                        isSingleLine = true
//                                        ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
//                                        marqueeRepeatLimit = -1
//                                        isSelected = true
//                                        textSize = 16f
//                                        setTextColor(android.graphics.Color.BLACK)
//                                    }
//                                }
//                            )

                            //Timestamp
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                            val date = Date(note.timestamp)
                            Text(
                                text = "Created on: ${dateFormat.format(date)}",
                                style = MaterialTheme.typography.caption.copy(
                                    color = Color.Gray,
                                    fontStyle = FontStyle.Italic
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        // Delete btn
                        TextButton(onClick = {
                            noteToDelete = note
                            showDialog = true
                        }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }

        if (showDialog && noteToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = "Confirm Delete")
                },
                text = {
                    Text("Are you sure you want to delete this note?")
                },
                confirmButton = {
                    Button(onClick = {
                        coroutineScope.launch {
                            noteToDelete?.let { noteViewModel.deleteNote(it) }
                            showDialog = false
                            noteToDelete = null
                        }
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        noteToDelete = null
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}