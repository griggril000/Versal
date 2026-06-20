package com.grigg.versal.model

import kotlinx.serialization.Serializable

@Serializable
data class Volume(
    val id: String,
    val name: String,
    val slug: String,
    val books: List<Book> = emptyList()
)

@Serializable
data class Book(
    val id: String,
    val name: String,
    val slug: String,
    val chapters: List<Chapter> = emptyList()
)

@Serializable
data class Chapter(
    val number: Int,
    val verses: List<Verse> = emptyList()
)

@Serializable
data class Verse(
    val number: Int,
    val text: String = "" // Placeholder for now
)

data class VerseSelection(
    val volumeSlug: String,
    val bookSlug: String,
    val chapterNumber: Int,
    val selectedVerses: Set<Int> = emptySet()
) {
    fun generateLink(lang: String = "eng"): String {
        if (selectedVerses.isEmpty()) {
            return "https://www.churchofjesuschrist.org/study/scriptures/$volumeSlug/$bookSlug/$chapterNumber?lang=$lang"
        }

        val sortedVerses = selectedVerses.sorted()
        val ranges = mutableListOf<String>()
        if (sortedVerses.isNotEmpty()) {
            var start = sortedVerses[0]
            var end = sortedVerses[0]

            for (i in 1 until sortedVerses.size) {
                if (sortedVerses[i] == end + 1) {
                    end = sortedVerses[i]
                } else {
                    ranges.add(if (start == end) "p$start" else "p$start-p$end")
                    start = sortedVerses[i]
                    end = sortedVerses[i]
                }
            }
            ranges.add(if (start == end) "p$start" else "p$start-p$end")
        }

        val idParam = ranges.joinToString(",")
        val firstVerse = sortedVerses.first()
        return "https://www.churchofjesuschrist.org/study/scriptures/$volumeSlug/$bookSlug/$chapterNumber?lang=$lang&id=$idParam#p$firstVerse"
    }
}
