package com.grigg.versal.data

import com.grigg.versal.model.Book
import com.grigg.versal.model.Chapter
import com.grigg.versal.model.Verse
import com.grigg.versal.model.Volume

object ScriptureRepository {
    private fun createBook(id: String, name: String, slug: String, chapterCount: Int, verseCount: Int = 30): Book {
        return Book(
            id = id,
            name = name,
            slug = slug,
            chapters = (1..chapterCount).map { chapterNum ->
                Chapter(chapterNum, (1..verseCount).map { verseNum -> Verse(verseNum) })
            }
        )
    }

    val volumes = listOf(
        Volume(
            id = "ot",
            name = "Old Testament",
            slug = "ot",
            books = listOf(
                createBook("gen", "Genesis", "gen", 50),
                createBook("ex", "Exodus", "ex", 40),
                createBook("lev", "Leviticus", "lev", 27),
                createBook("num", "Numbers", "num", 36),
                createBook("deut", "Deuteronomy", "deut", 34),
                createBook("josh", "Joshua", "josh", 24),
                createBook("judg", "Judges", "judg", 21),
                createBook("ruth", "Ruth", "ruth", 4),
                createBook("1-sam", "1 Samuel", "1-sam", 31),
                createBook("2-sam", "2 Samuel", "2-sam", 24),
                createBook("1-kgs", "1 Kings", "1-kgs", 22),
                createBook("2-kgs", "2 Kings", "2-kgs", 25),
                createBook("1-chr", "1 Chronicles", "1-chr", 29),
                createBook("2-chr", "2 Chronicles", "2-chr", 36),
                createBook("ezra", "Ezra", "ezra", 10),
                createBook("neh", "Nehemiah", "neh", 13),
                createBook("esth", "Esther", "esth", 10),
                createBook("job", "Job", "job", 42),
                createBook("ps", "Psalms", "ps", 150),
                createBook("prov", "Proverbs", "prov", 31),
                createBook("eccl", "Ecclesiastes", "eccl", 12),
                createBook("song", "Song of Solomon", "song", 8),
                createBook("isa", "Isaiah", "isa", 66),
                createBook("jer", "Jeremiah", "jer", 52),
                createBook("lam", "Lamentations", "lam", 5),
                createBook("ezek", "Ezekiel", "ezek", 48),
                createBook("dan", "Daniel", "dan", 12),
                createBook("hos", "Hosea", "hos", 14),
                createBook("joel", "Joel", "joel", 3),
                createBook("amos", "Amos", "amos", 9),
                createBook("obad", "Obadiah", "obad", 1),
                createBook("jonah", "Jonah", "jonah", 4),
                createBook("micah", "Micah", "micah", 7),
                createBook("nahum", "Nahum", "nahum", 3),
                createBook("hab", "Habakkuk", "hab", 3),
                createBook("zeph", "Zephaniah", "zeph", 3),
                createBook("hag", "Haggai", "hag", 2),
                createBook("zech", "Zechariah", "zech", 14),
                createBook("mal", "Malachi", "mal", 4)
            )
        ),
        Volume(
            id = "nt",
            name = "New Testament",
            slug = "nt",
            books = listOf(
                createBook("matt", "Matthew", "matt", 28),
                createBook("mark", "Mark", "mark", 16),
                createBook("luke", "Luke", "luke", 24),
                createBook("john", "John", "john", 21),
                createBook("acts", "Acts", "acts", 28),
                createBook("rom", "Romans", "rom", 16),
                createBook("1-cor", "1 Corinthians", "1-cor", 16),
                createBook("2-cor", "2 Corinthians", "2-cor", 13),
                createBook("gal", "Galatians", "gal", 6),
                createBook("eph", "Ephesians", "eph", 6),
                createBook("phil", "Philippians", "phil", 4),
                createBook("col", "Colossians", "col", 4),
                createBook("1-thess", "1 Thessalonians", "1-thess", 5),
                createBook("2-thess", "2 Thessalonians", "2-thess", 3),
                createBook("1-tim", "1 Timothy", "1-tim", 6),
                createBook("2-tim", "2 Timothy", "2-tim", 4),
                createBook("titus", "Titus", "titus", 3),
                createBook("philem", "Philemon", "philem", 1),
                createBook("heb", "Hebrews", "heb", 13),
                createBook("james", "James", "james", 5),
                createBook("1-pet", "1 Peter", "1-pet", 5),
                createBook("2-pet", "2 Peter", "2-pet", 3),
                createBook("1-jn", "1 John", "1-jn", 5),
                createBook("2-jn", "2 John", "2-jn", 1),
                createBook("3-jn", "3 John", "3-jn", 1),
                createBook("jude", "Jude", "jude", 1),
                createBook("rev", "Revelation", "rev", 22)
            )
        ),
        Volume(
            id = "bofm",
            name = "Book of Mormon",
            slug = "bofm",
            books = listOf(
                createBook("1-ne", "1 Nephi", "1-ne", 22),
                createBook("2-ne", "2 Nephi", "2-ne", 33),
                createBook("jacob", "Jacob", "jacob", 7),
                createBook("enos", "Enos", "enos", 1),
                createBook("jarom", "Jarom", "jarom", 1),
                createBook("omni", "Omni", "omni", 1),
                createBook("w-of-m", "Words of Mormon", "w-of-m", 1),
                createBook("mosiah", "Mosiah", "mosiah", 29),
                createBook("alma", "Alma", "alma", 63),
                createBook("hel", "Helaman", "hel", 16),
                createBook("3-ne", "3 Nephi", "3-ne", 30),
                createBook("4-ne", "4 Nephi", "4-ne", 1),
                createBook("morm", "Mormon", "morm", 9),
                createBook("ether", "Ether", "ether", 15),
                createBook("moro", "Moroni", "moro", 10)
            )
        ),
        Volume(
            id = "dc-testament",
            name = "Doctrine and Covenants",
            slug = "dc-testament",
            books = listOf(
                createBook("dc", "Doctrine and Covenants", "dc", 138),
                createBook("od", "Official Declarations", "od", 2)
            )
        ),
        Volume(
            id = "pgp",
            name = "Pearl of Great Price",
            slug = "pgp",
            books = listOf(
                createBook("moses", "Moses", "moses", 8),
                createBook("abr", "Abraham", "abr", 5),
                createBook("js-m", "Joseph Smith—Matthew", "js-m", 1),
                createBook("js-h", "Joseph Smith—History", "js-h", 1),
                createBook("a-of-f", "Articles of Faith", "a-of-f", 1)
            )
        )
    )

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
}
