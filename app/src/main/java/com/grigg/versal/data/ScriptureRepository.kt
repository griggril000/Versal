package com.grigg.versal.data

import com.grigg.versal.model.Book
import com.grigg.versal.model.Chapter
import com.grigg.versal.model.Verse
import com.grigg.versal.model.Volume

object ScriptureRepository {
    val volumes = listOf(
        Volume(
            id = "ot",
            name = "Old Testament",
            slug = "ot",
            books = listOf(
                Book("gen", "Genesis", "gen", (1..50).map { Chapter(it, (1..30).map { v -> Verse(v) }) }),
                Book("ex", "Exodus", "ex", (1..40).map { Chapter(it, (1..30).map { v -> Verse(v) }) })
            )
        ),
        Volume(
            id = "nt",
            name = "New Testament",
            slug = "nt",
            books = listOf(
                Book("matt", "Matthew", "matt", (1..28).map { Chapter(it, (1..30).map { v -> Verse(v) }) }),
                Book("mark", "Mark", "mark", (1..16).map { Chapter(it, (1..30).map { v -> Verse(v) }) }),
                Book("luke", "Luke", "luke", (1..24).map { Chapter(it, (1..30).map { v -> Verse(v) }) }),
                Book("john", "John", "john", (1..21).map { Chapter(it, (1..30).map { v -> Verse(v) }) })
            )
        ),
        Volume(
            id = "bofm",
            name = "Book of Mormon",
            slug = "bofm",
            books = listOf(
                Book("1-ne", "1 Nephi", "1-ne", (1..22).map { Chapter(it, (1..30).map { v -> Verse(v) }) }),
                Book("2-ne", "2 Nephi", "2-ne", (1..33).map { Chapter(it, (1..30).map { v -> Verse(v) }) }),
                Book("jacob", "Jacob", "jacob", (1..7).map { Chapter(it, (1..30).map { v -> Verse(v) }) }),
                Book("enos", "Enos", "enos", listOf(Chapter(1, (1..27).map { v -> Verse(v) })))
            )
        ),
        Volume(
            id = "dc-testament",
            name = "Doctrine and Covenants",
            slug = "dc-testament",
            books = listOf(
                Book("dc", "Doctrine and Covenants", "dc", (1..138).map { Chapter(it, (1..20).map { v -> Verse(v) }) })
            )
        ),
        Volume(
            id = "pgp",
            name = "Pearl of Great Price",
            slug = "pgp",
            books = listOf(
                Book("moses", "Moses", "moses", (1..8).map { Chapter(it, (1..30).map { v -> Verse(v) }) }),
                Book("abr", "Abraham", "abr", (1..5).map { Chapter(it, (1..30).map { v -> Verse(v) }) })
            )
        )
    )

    fun getVolume(id: String) = volumes.find { it.id == id }
    fun getBook(volumeId: String, bookId: String) = getVolume(volumeId)?.books?.find { it.id == bookId }
    fun getChapter(volumeId: String, bookId: String, chapterNumber: Int) =
        getBook(volumeId, bookId)?.chapters?.find { it.number == chapterNumber }
}
