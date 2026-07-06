package com.grigg.versal.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.grigg.versal.R
import com.grigg.versal.data.ScriptureRepository
import com.grigg.versal.model.VerseSelection
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseSelectionScreen(volumeId: String, bookId: String, chapterNumber: Int, onBack: () -> Unit, onHomeClick: () -> Unit) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val chapter = ScriptureRepository.getChapter(volumeId, bookId, chapterNumber)
    val book = ScriptureRepository.getBook(volumeId, bookId)
    val volume = ScriptureRepository.getVolume(volumeId)
    
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val isExpanded = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    
    val baseTitle = stringResource(R.string.book_chapter_format, book?.name ?: "", chapterNumber)
    var selectedVerses by remember { mutableStateOf(setOf<Int>()) }
    var displayedTitle by remember { mutableStateOf(baseTitle) }

    LaunchedEffect(selectedVerses) {
        if (selectedVerses.isEmpty()) {
            displayedTitle = baseTitle
        } else {
            // Use a debounce timer to wait until the user has finished their selection
            // before updating the title, minimizing distraction during active tapping.
            delay(1.seconds)
            val selection = VerseSelection("", "", chapterNumber, selectedVerses)
            val verseString = selection.formatSelectedVerses()
            displayedTitle = "${book?.name ?: ""} $chapterNumber:$verseString"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(displayedTitle) },
                navigationIcon = {
                    if (!isExpanded) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    }
                },
                actions = {
                    if (selectedVerses.isNotEmpty() && volume != null && book != null) {
                        IconButton(onClick = {
                            selectedVerses = emptySet()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear_selection))
                        }
                        IconButton(onClick = {
                            val selection = VerseSelection(
                                volumeSlug = volume.slug,
                                bookSlug = book.slug,
                                chapterNumber = chapterNumber,
                                selectedVerses = selectedVerses
                            )
                            uriHandler.openUri(selection.generateLink())
                        }) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = stringResource(R.string.open_link))
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
                            Icon(Icons.Default.Share, contentDescription = stringResource(R.string.share_link))
                        }
                    } else {
                        IconButton(onClick = onHomeClick) {
                            Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home))
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (chapter != null) {
            val layoutDirection = LocalLayoutDirection.current
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                        start = paddingValues.calculateStartPadding(layoutDirection),
                        end = paddingValues.calculateEndPadding(layoutDirection)
                    ),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = if (isExpanded) 80.dp else 64.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(max = if (isExpanded) 800.dp else Double.POSITIVE_INFINITY.dp)
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
            }
        } else {
            Text(stringResource(R.string.chapter_not_found), modifier = Modifier.padding(paddingValues))
        }
    }
}
