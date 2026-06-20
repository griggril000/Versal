package com.grigg.versal.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.grigg.versal.data.ScriptureRepository
import com.grigg.versal.model.VerseSelection
import android.content.Intent
import android.content.ClipData
import android.widget.Toast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseSelectionScreen(volumeId: String, bookId: String, chapterNumber: Int, onBack: () -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val chapter = ScriptureRepository.getChapter(volumeId, bookId, chapterNumber)
    val book = ScriptureRepository.getBook(volumeId, bookId)
    val volume = ScriptureRepository.getVolume(volumeId)
    
    var selectedVerses by remember { mutableStateOf(setOf<Int>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${book?.name} $chapterNumber") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (selectedVerses.isNotEmpty() && volume != null && book != null) {
                        IconButton(onClick = {
                            selectedVerses = emptySet()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear Selection")
                        }
                        IconButton(onClick = {
                            val selection = VerseSelection(
                                volumeSlug = volume.slug,
                                bookSlug = book.slug,
                                chapterNumber = chapterNumber,
                                selectedVerses = selectedVerses
                            )
                            val link = selection.generateLink()
                            val clipData = ClipData.newPlainText("Scripture Link", link)
                            scope.launch {
                                clipboard.setClipEntry(ClipEntry(clipData))
                                Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Link")
                        }
                        IconButton(onClick = {
                            val selection = VerseSelection(
                                volumeSlug = volume.slug,
                                bookSlug = book.slug,
                                chapterNumber = chapterNumber,
                                selectedVerses = selectedVerses
                            )
                            val link = selection.generateLink()
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, link)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Share Link")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (chapter != null) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 64.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(chapter.verses) { verse ->
                    val isSelected = selectedVerses.contains(verse.number)
                    Card(
                        colors = if (isSelected) {
                            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        } else {
                            CardDefaults.cardColors()
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .clickable {
                                selectedVerses = if (isSelected) {
                                    selectedVerses - verse.number
                                } else {
                                    selectedVerses + verse.number
                                }
                            }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = verse.number.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        } else {
            Text("Chapter not found", modifier = Modifier.padding(paddingValues))
        }
    }
}
