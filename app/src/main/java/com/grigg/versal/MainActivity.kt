package com.grigg.versal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen() {
    val backStack = rememberNavBackStack(Route.Home)
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val windowSize = adaptiveInfo.windowSizeClass
    val isWide = windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val isCompactHeight = !windowSize.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
    
    val posture = adaptiveInfo.windowPosture

    Row(modifier = Modifier.fillMaxSize()) {
        if (isWide) {
            NavigationRail(
                modifier = Modifier
                    .statusBarsPadding()
                    .displayCutoutPadding()
            ) {
                NavigationRailItem(
                    selected = (backStack.lastOrNull() is Route.Home) || (backStack.isNotEmpty() && backStack.last() !is Route.About),
                    onClick = {
                        while (backStack.size > 1) backStack.removeLastOrNull()
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home)) },
                    label = { Text(stringResource(R.string.home)) }
                )
                NavigationRailItem(
                    selected = backStack.lastOrNull() is Route.About,
                    onClick = {
                        if (backStack.lastOrNull() !is Route.About) {
                            // When going to About, we clear the stack to ensure it's a full-screen
                            // destination and doesn't show side-by-side with the home screen.
                            while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                            backStack.add(Route.About)
                        }
                    },
                    icon = { Icon(Icons.Default.Info, contentDescription = stringResource(R.string.about)) },
                    label = { Text(stringResource(R.string.about)) }
                )
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                val bottomPadding = if (posture.isTabletop) 16.dp else 0.dp
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = bottomPadding)
                ) {
                    if (backStack.size > 1 || (backStack.isNotEmpty() && backStack.last() is Route.About)) {
                        Breadcrumbs(
                            backStack = backStack,
                            isCompactHeight = isCompactHeight
                        ) { targetKey ->
                            val indexInStack = backStack.indexOfLast { it == targetKey }
                            if (indexInStack >= 0) {
                                while (backStack.size > indexInStack + 1) {
                                    backStack.removeLastOrNull()
                                }
                            } else {
                                while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                                backStack.add(Route.Home)
                                when (targetKey) {
                                    is Route.Books -> backStack.add(targetKey)
                                    is Route.Chapters -> {
                                        backStack.add(Route.Books(targetKey.volumeId))
                                        backStack.add(targetKey)
                                    }
                                    is Route.Verses -> {
                                        backStack.add(Route.Books(targetKey.volumeId))
                                        backStack.add(Route.Chapters(targetKey.volumeId, targetKey.bookId))
                                        backStack.add(targetKey)
                                    }
                                    is Route.About -> backStack.add(Route.About)
                                    else -> {}
                                }
                            }
                        }
                    }
                    if (!isWide) {
                        NavigationBar {
                            NavigationBarItem(
                                selected = (backStack.lastOrNull() is Route.Home) || (backStack.isNotEmpty() && backStack.last() !is Route.About),
                                onClick = {
                                    while (backStack.size > 1) backStack.removeLastOrNull()
                                },
                                icon = { Icon(Icons.Default.Home, null) },
                                label = { Text(stringResource(R.string.home)) }
                            )
                            NavigationBarItem(
                                selected = backStack.lastOrNull() is Route.About,
                                onClick = {
                                    if (backStack.lastOrNull() !is Route.About) {
                                        while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                                        backStack.add(Route.About)
                                    }
                                },
                                icon = { Icon(Icons.Default.Info, null) },
                                label = { Text(stringResource(R.string.about)) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .consumeWindowInsets(innerPadding)
            ) {
                NavDisplay(
                    backStack = backStack,
                    onBack = { backStack.removeLastOrNull() },
                    entryProvider = entryProvider {
                        entry<Route.Home> {
                            VolumeListScreen(
                                onVolumeClick = { backStack.add(Route.Books(it)) },
                                onBookClick = { vId, bId -> backStack.add(Route.Chapters(vId, bId)) }
                            )
                        }

                        entry<Route.Books> { key ->
                            BookListScreen(
                                volumeId = key.volumeId,
                                onBack = { backStack.removeLastOrNull() },
                                onBookClick = { backStack.add(Route.Chapters(key.volumeId, it)) }
                            )
                        }

                        entry<Route.Chapters> { key ->
                            ChapterListScreen(
                                volumeId = key.volumeId,
                                bookId = key.bookId,
                                onBack = { backStack.removeLastOrNull() },
                                onChapterClick = { backStack.add(Route.Verses(key.volumeId, key.bookId, it)) }
                            )
                        }

                        entry<Route.Verses> { key ->
                            VerseSelectionScreen(
                                volumeId = key.volumeId,
                                bookId = key.bookId,
                                chapterNumber = key.chapterNumber,
                                onBack = { backStack.removeLastOrNull() }
                            )
                        }

                        entry<Route.About> {
                            AboutScreen(onBack = { 
                                while (backStack.isNotEmpty()) backStack.removeLastOrNull()
                                backStack.add(Route.Home)
                            })
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Breadcrumbs(backStack: NavBackStack<NavKey>, isCompactHeight: Boolean, onBreadcrumbClick: (NavKey) -> Unit) {
    val lastKey = backStack.lastOrNull() ?: return

    val breadcrumbItems = when (lastKey) {
        is Route.Home -> listOf(stringResource(R.string.volumes) to Route.Home)
        is Route.About -> listOf(stringResource(R.string.about) to Route.About)
        is Route.Books -> listOf(
            stringResource(R.string.volumes) to Route.Home,
            (ScriptureRepository.getVolume(lastKey.volumeId)?.name ?: stringResource(R.string.books)) to lastKey
        )
        is Route.Chapters -> listOf(
            stringResource(R.string.volumes) to Route.Home,
            (ScriptureRepository.getVolume(lastKey.volumeId)?.name ?: stringResource(R.string.books)) to Route.Books(lastKey.volumeId),
            (ScriptureRepository.getBook(lastKey.volumeId, lastKey.bookId)?.name ?: stringResource(R.string.chapters)) to lastKey
        )
        is Route.Verses -> listOf(
            stringResource(R.string.volumes) to Route.Home,
            (ScriptureRepository.getVolume(lastKey.volumeId)?.name ?: stringResource(R.string.books)) to Route.Books(lastKey.volumeId),
            (ScriptureRepository.getBook(lastKey.volumeId, lastKey.bookId)?.name ?: stringResource(R.string.chapters)) to Route.Chapters(lastKey.volumeId, lastKey.bookId),
            stringResource(R.string.chapter_number_format, lastKey.chapterNumber) to lastKey
        )
        else -> emptyList()
    }

    if (breadcrumbItems.isEmpty()) return

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = if (isCompactHeight) 2.dp else 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            breadcrumbItems.forEachIndexed { index, (label, key) ->
                if (index > 0) {
                    Text(
                        text = ">",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .padding(horizontal = 4.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                val isLast = index == breadcrumbItems.size - 1
                TextButton(
                    onClick = { onBreadcrumbClick(key) },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
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
