package com.grigg.versal.data

import android.content.Context
import com.grigg.versal.model.Book
import com.grigg.versal.model.Chapter
import com.grigg.versal.model.Reference
import com.grigg.versal.model.Verse
import com.grigg.versal.model.Volume
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * ScriptureRepository provides access to the Standard Works of The Church of Jesus Christ of Latter-day Saints.
 * 
 * Special thanks to Ben Crowder for the open-source scripture datasets (bcbooks/scriptures-json)
 * which provide accurate metadata and text for these volumes.
 */
object ScriptureRepository {
    private var _volumes: List<Volume> = emptyList()
    val volumes: List<Volume> get() = _volumes

    fun init(context: Context) {
        if (_volumes.isNotEmpty()) return
        val jsonString = context.assets.open("data/scriptures.json").bufferedReader().use { it.readText() }
        initWithJson(jsonString)
    }

    fun initWithJson(jsonString: String) {
        val data = Json.decodeFromString<List<VolumeData>>(jsonString)
        _volumes = data.map { volData ->
            Volume(
                id = volData.id,
                name = volData.name,
                slug = volData.slug,
                books = volData.books.map { bookData ->
                    Book(
                        id = bookData.id,
                        name = bookData.name,
                        slug = bookData.slug,
                        chapters = bookData.verseCounts.mapIndexed { index, count ->
                            Chapter(index + 1, (1..count).map { verseNum -> Verse(verseNum) })
                        }
                    )
                }
            )
        }
    }

    fun getVolume(id: String) = volumes.find { it.id == id }
    fun getBook(volumeId: String, bookId: String) = getVolume(volumeId)?.books?.find { it.id == bookId }
    fun getChapter(volumeId: String, bookId: String, chapterNumber: Int) =
        getBook(volumeId, bookId)?.chapters?.find { it.number == chapterNumber }

    fun parseReference(query: String): Reference? {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return null

        // Normalize common abbreviations
        var normalizedQuery = trimmed.lowercase()
            .replace(Regex("^1\\s?ne(phi)?"), "1 Nephi")
            .replace(Regex("^2\\s?ne(phi)?"), "2 Nephi")
            .replace(Regex("^3\\s?ne(phi)?"), "3 Nephi")
            .replace(Regex("^4\\s?ne(phi)?"), "4 Nephi")
            .replace(Regex("^w\\s?o\\s?m"), "Words of Mormon")
            .replace(Regex("^d\\s?&\\s?c"), "Doctrine and Covenants")
            .replace(Regex("^dc"), "Doctrine and Covenants")
            .replace(Regex("^js-m"), "Joseph Smith—Matthew")
            .replace(Regex("^jsm"), "Joseph Smith—Matthew")
            .replace(Regex("^js-h"), "Joseph Smith—History")
            .replace(Regex("^jsh"), "Joseph Smith—History")
            .replace(Regex("^a\\s?of\\s?f"), "Articles of Faith")

        // Regex to match: Book Name Chapter:Verses (e.g., "Alma 5", "Alma 5:12", "Alma 5:12-15")
        // Now handles cases like "dc1:1" where there's no space after book name
        val regex = Regex("""^(.+?)\s*(\d+)(?::([\d\s\-,p]+))?$""", RegexOption.IGNORE_CASE)
        val match = regex.find(normalizedQuery) ?: return null

        val bookNameQuery = match.groupValues[1].trim()
        val chapterNum = match.groupValues[2].toIntOrNull() ?: return null
        val verseQuery = match.groupValues[3].trim()

        // Find the book by name (exact match or starting with)
        val (volume, book) = volumes.flatMap { v -> v.books.map { b -> v to b } }
            .find { it.second.name.equals(bookNameQuery, ignoreCase = true) }
            ?: volumes.flatMap { v -> v.books.map { b -> v to b } }
                .find { it.second.name.startsWith(bookNameQuery, ignoreCase = true) }
            ?: return null

        // Find the chapter
        val chapter = book.chapters.find { it.number == chapterNum } ?: return null

        val selectedVerses = if (verseQuery.isNotEmpty()) {
            parseVerseSelection(verseQuery, chapter.verses.size)
        } else {
            emptySet()
        }

        return Reference(volume, book, chapterNum, selectedVerses)
    }

    private fun parseVerseSelection(query: String, maxVerses: Int): Set<Int> {
        val verses = mutableSetOf<Int>()
        val parts = query.split(',')
        for (part in parts) {
            val trimmedPart = part.trim().lowercase()
            if (trimmedPart.isEmpty()) continue
            
            val rangeParts = trimmedPart.split('-')
            if (rangeParts.size == 1) {
                rangeParts[0].removePrefix("p").trim().toIntOrNull()?.let {
                    if (it in 1..maxVerses) verses.add(it)
                }
            } else if (rangeParts.size == 2) {
                val start = rangeParts[0].removePrefix("p").trim().toIntOrNull()
                val end = rangeParts[1].removePrefix("p").trim().toIntOrNull()
                if (start != null && end != null) {
                    val range = if (start <= end) start..end else end..start
                    for (v in range) {
                        if (v in 1..maxVerses) verses.add(v)
                    }
                }
            }
        }
        return verses
    }

    fun searchBooks(query: String): List<Pair<Volume, Book>> {
        if (query.isBlank()) return emptyList()
        val results = mutableListOf<Pair<Volume, Book>>()
        for (volume in volumes) {
            for (book in volume.books) {
                if (book.name.contains(query, ignoreCase = true)) {
                    results.add(volume to book)
                }
            }
        }
        return results
    }

    @Serializable
    private data class VolumeData(
        val id: String,
        val name: String,
        val slug: String,
        val books: List<BookData>
    )

    @Serializable
    private data class BookData(
        val id: String,
        val name: String,
        val slug: String,
        val verseCounts: List<Int>
    )
}
