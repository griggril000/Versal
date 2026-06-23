package com.grigg.versal.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data class Books(val volumeId: String) : Route

    @Serializable
    data class Chapters(val volumeId: String, val bookId: String) : Route

    @Serializable
    data class Verses(
        val volumeId: String,
        val bookId: String,
        val chapterNumber: Int,
        val initialSelectedVerses: Set<Int> = emptySet()
    ) : Route

    @Serializable
    data object About : Route
}
