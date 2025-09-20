package com.example.memoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NotesApp(navController: NavController, viewModel: NoteViewModel) {
    // 导航结构
    when (navController.currentDestination?.route) {
        "notes_list" -> NotesListScreen(
            viewModel = viewModel,
            onAddNoteClick = { navController.navigate("add_note") },
            onNoteClick = { noteId -> navController.navigate("edit_note/$noteId") }
        )
        "add_note" -> NoteEditScreen(
            onSaveNote = { title, content ->
                viewModel.insert(Note(title = title, content = content))
                navController.popBackStack()
            },
            onBackClick = { navController.popBackStack() }
        )
        else -> {
            val noteId = navController.currentDestination?.route?.split("/")?.get(1)?.toIntOrNull() ?: -1
            var note by remember { mutableStateOf<Note?>(null) }
            
            LaunchedEffect(noteId) {
                note = viewModel.getNoteById(noteId)
            }
            
            note?.let {
                NoteEditScreen(
                    initialTitle = it.title,
                    initialContent = it.content,
                    isCompleted = it.isCompleted,
                    onSaveNote = { title, content ->
                        viewModel.update(it.copy(title = title, content = content))
                        navController.popBackStack()
                    },
                    onDeleteNote = {
                        viewModel.delete(it)
                        navController.popBackStack()
                    },
                    onBackClick = { navController.popBackStack() },
                    onToggleComplete = {
                        viewModel.update(it.copy(isCompleted = !it.isCompleted))
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    viewModel: NoteViewModel,
    onAddNoteClick: () -> Unit,
    onNoteClick: (Int) -> Unit
) {
    val notes by viewModel.allNotes.observeAsState(emptyList())
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "添加备忘录")
            }
        },
        topBar = {
            TopAppBar(title = { Text("备忘录") })
        }
    ) { innerPadding ->
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("没有备忘录，点击右下角添加")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(notes) { note ->
                    NoteItem(
                        note = note,
                        onClick = { onNoteClick(note.id) },
                        onToggleComplete = {
                            viewModel.update(note.copy(isCompleted = !note.isCompleted))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    onToggleComplete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = note.isCompleted,
                    onCheckedChange = { onToggleComplete() },
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp, bottom = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (note.isCompleted) Color.Gray else Color.Unspecified
                )
            }
            
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = if (note.isCompleted) Color.Gray else Color.Unspecified
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    initialTitle: String = "",
    initialContent: String = "",
    isCompleted: Boolean = false,
    onSaveNote: (String, String) -> Unit,
    onDeleteNote: (() -> Unit)? = null,
    onBackClick: () -> Unit,
    onToggleComplete: (() -> Unit)? = null
) {
    var titleState by remember { mutableStateOf(initialTitle) }
    var contentState by remember { mutableStateOf(initialContent) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (initialTitle.isEmpty()) "添加备忘录" else "编辑备忘录") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { onSaveNote(titleState, contentState) }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "保存")
                    }
                    
                    if (onDeleteNote != null) {
                        IconButton(onClick = onDeleteNote) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "删除")
                        }
                    }
                    
                    if (onToggleComplete != null) {
                        IconButton(onClick = onToggleComplete) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Circle,
                                contentDescription = if (isCompleted) "标记为未完成" else "标记为完成"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TextField(
                value = titleState,
                onValueChange = { titleState = it },
                label = { Text("标题") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            
            TextField(
                value = contentState,
                onValueChange = { contentState = it },
                label = { Text("内容") },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                maxLines = 5,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
        }
    }
}
