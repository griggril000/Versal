package com.grigg.versal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.grigg.versal.data.ScriptureRepository
import com.grigg.versal.ui.theme.VersalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolumeListScreen(onVolumeClick: (String) -> Unit, onBookClick: (String, String) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = ScriptureRepository.searchBooks(searchQuery)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Scriptures") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search books...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                if (searchQuery.isNotEmpty()) {
                    items(searchResults) { (volume, book) ->
                        ListItem(
                            headlineContent = { Text(book.name) },
                            supportingContent = { Text(volume.name) },
                            modifier = Modifier.clickable { onBookClick(volume.id, book.id) }
                        )
                        HorizontalDivider()
                    }
                } else {
                    items(ScriptureRepository.volumes) { volume ->
                        ListItem(
                            headlineContent = { Text(volume.name) },
                            supportingContent = { Text("${volume.books.size} books") },
                            modifier = Modifier.clickable { onVolumeClick(volume.id) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VolumeListScreenPreview() {
    VersalTheme {
        VolumeListScreen(onVolumeClick = {}, onBookClick = { _, _ -> })
    }
}
