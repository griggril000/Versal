package com.grigg.versal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grigg.versal.data.ScriptureRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(volumeId: String, onBack: () -> Unit, onBookClick: (String) -> Unit) {
    val volume = ScriptureRepository.getVolume(volumeId)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(volume?.name ?: "Books") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (volume != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(volume.books) { book ->
                    ListItem(
                        headlineContent = { Text(book.name) },
                        supportingContent = { Text("${book.chapters.size} chapters") },
                        modifier = Modifier.clickable { onBookClick(book.id) }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            Text("Volume not found", modifier = Modifier.padding(paddingValues))
        }
    }
}
