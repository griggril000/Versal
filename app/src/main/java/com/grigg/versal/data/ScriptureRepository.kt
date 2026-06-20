package com.grigg.versal.data

import android.content.Context
import com.grigg.versal.model.Book
import com.grigg.versal.model.Chapter
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
