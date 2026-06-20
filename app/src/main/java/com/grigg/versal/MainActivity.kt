package com.grigg.versal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.grigg.versal.data.ScriptureRepository
import com.grigg.versal.navigation.Route
import com.grigg.versal.ui.screens.AboutScreen
import com.grigg.versal.ui.screens.BookListScreen
import com.grigg.versal.ui.screens.ChapterListScreen
import com.grigg.versal.ui.screens.VerseSelectionScreen
import com.grigg.versal.ui.screens.VolumeListScreen
import com.grigg.versal.ui.theme.VersalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScriptureRepository.init(this)
        enableEdgeToEdge()
        setContent {
            VersalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen() {
    val backStack = rememberNavBackStack(Route.Home)
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val paneExpansionState = rememberPaneExpansionState(
        anchors = listOf(
            PaneExpansionAnchor.Proportion(0.5f),
        ),
        initialAnchoredIndex = 0
    )
    val adaptiveStrategy = rememberListDetailSceneStrategy<NavKey>(
        directive = calculatePaneScaffoldDirective(adaptiveInfo),
        paneExpansionState = paneExpansionState
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            Breadcrumbs(
                backStack = backStack,
                onBreadcrumbClick = { index ->
                    while (backStack.size > index + 1) {
                        backStack.removeLastOrNull()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavDisplay(
                backStack = backStack,
                onBack = {
                    backStack.removeLastOrNull()
                },
                sceneStrategy = adaptiveStrategy,
                entryProvider = entryProvider {
                    entry<Route.Home>(
                        metadata = ListDetailSceneStrategy.listPane()
                    ) {
                        VolumeListScreen(
                            onVolumeClick = { volumeId ->
                                backStack.add(Route.Books(volumeId))
                            },
                            onBookClick = { volumeId, bookId ->
                                backStack.add(Route.Chapters(volumeId, bookId))
                            },
                            onAboutClick = {
                                backStack.add(Route.About)
                            }
                        )
                    }
                    entry<Route.About>(
                        metadata = ListDetailSceneStrategy.detailPane()
                    ) {
                        AboutScreen(onBack = { backStack.removeAt(backStack.size - 1) })
                    }
                    entry<Route.Books>(
                        metadata = ListDetailSceneStrategy.listPane()
                    ) { key ->
                        BookListScreen(
                            volumeId = key.volumeId,
                            onBack = { backStack.removeAt(backStack.size - 1) },
                            onBookClick = { bookId ->
                                backStack.add(Route.Chapters(key.volumeId, bookId))
                            }
                        )
                    }
                    entry<Route.Chapters>(
                        metadata = ListDetailSceneStrategy.listPane()
                    ) { key ->
                        ChapterListScreen(
                            volumeId = key.volumeId,
                            bookId = key.bookId,
                            onBack = { backStack.removeAt(backStack.size - 1) },
                            onChapterClick = { chapterNum ->
                                backStack.add(Route.Verses(key.volumeId, key.bookId, chapterNum))
                            }
                        )
                    }
                    entry<Route.Verses>(
                        metadata = ListDetailSceneStrategy.detailPane()
                    ) { key ->
                        VerseSelectionScreen(
                            volumeId = key.volumeId,
                            bookId = key.bookId,
                            chapterNumber = key.chapterNumber,
                            onBack = { backStack.removeAt(backStack.size - 1) }
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Breadcrumbs(backStack: NavBackStack<NavKey>, onBreadcrumbClick: (Int) -> Unit) {
    if (backStack.isEmpty()) return

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            backStack.forEachIndexed { index, key ->
                val label = when (key) {
                    is Route.Home -> "Volumes"
                    is Route.About -> "About"
                    is Route.Books -> ScriptureRepository.getVolume(key.volumeId)?.name ?: "Books"
                    is Route.Chapters -> ScriptureRepository.getBook(key.volumeId, key.bookId)?.name ?: "Chapters"
                    is Route.Verses -> "Chapter ${key.chapterNumber}"
                    else -> ""
                }
                if (label.isNotEmpty()) {
                    if (index > 0) {
                        Text(
                            text = ">",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .align(Alignment.CenterVertically),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    val isLast = index == backStack.size - 1
                    TextButton(
                        onClick = { onBreadcrumbClick(index) },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.heightIn(min = 32.dp),
                        enabled = !isLast
                    ) {
                        Text(
                            text = label,
                            style = if (isLast) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if (isLast) 
                                MaterialTheme.colorScheme.onSurface 
                            else 
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

