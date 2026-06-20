package com.grigg.versal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
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
                MainScreen()
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

