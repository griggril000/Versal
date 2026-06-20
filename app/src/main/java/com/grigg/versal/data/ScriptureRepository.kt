package com.grigg.versal.data

import com.grigg.versal.model.Book
import com.grigg.versal.model.Chapter
import com.grigg.versal.model.Verse
import com.grigg.versal.model.Volume

/**
 * ScriptureRepository provides access to the Standard Works of The Church of Jesus Christ of Latter-day Saints.
 * 
 * Special thanks to Ben Crowder for the open-source scripture datasets (bcbooks/scriptures-json)
 * which provide accurate metadata and text for these volumes.
 */
object ScriptureRepository {
    private fun createBook(id: String, name: String, slug: String, verseCounts: List<Int>): Book {
        return Book(
            id = id,
            name = name,
            slug = slug,
            chapters = verseCounts.mapIndexed { index, count ->
                Chapter(index + 1, (1..count).map { verseNum -> Verse(verseNum) })
            }
        )
    }

    val volumes = listOf(
        Volume(
            id = "ot",
            name = "Old Testament",
            slug = "ot",
            books = listOf(
                createBook("gen", "Genesis", "gen", listOf(31, 25, 24, 26, 32, 22, 24, 22, 29, 32, 32, 20, 18, 24, 21, 16, 27, 33, 38, 18, 34, 24, 20, 67, 34, 35, 46, 22, 35, 43, 55, 32, 20, 31, 29, 43, 36, 30, 23, 23, 57, 38, 34, 34, 28, 34, 31, 22, 33, 26)),
                createBook("ex", "Exodus", "ex", listOf(22, 25, 22, 31, 23, 30, 25, 32, 35, 29, 10, 51, 22, 31, 27, 36, 16, 27, 25, 26, 36, 31, 33, 18, 40, 37, 21, 43, 46, 38, 18, 35, 23, 35, 35, 38, 29, 31, 43, 38)),
                createBook("lev", "Leviticus", "lev", listOf(17, 16, 17, 35, 19, 30, 38, 36, 24, 20, 47, 8, 59, 57, 33, 34, 16, 30, 37, 27, 24, 33, 44, 23, 55, 46, 34)),
                createBook("num", "Numbers", "num", listOf(54, 34, 51, 49, 31, 27, 89, 26, 23, 36, 35, 16, 33, 45, 41, 50, 13, 32, 22, 29, 35, 41, 30, 25, 18, 65, 23, 31, 40, 16, 54, 42, 56, 29, 34, 13)),
                createBook("deut", "Deuteronomy", "deut", listOf(46, 37, 29, 49, 33, 25, 26, 20, 29, 22, 32, 32, 18, 29, 23, 22, 20, 22, 21, 20, 23, 30, 25, 22, 19, 19, 26, 68, 29, 20, 30, 52, 29, 12)),
                createBook("josh", "Joshua", "josh", listOf(30, 26, 17, 24, 15, 27, 26, 35, 27, 43, 23, 24, 33, 15, 63, 10, 18, 28, 51, 9, 45, 34, 16, 33)),
                createBook("judg", "Judges", "judg", listOf(36, 23, 31, 24, 31, 40, 25, 35, 57, 18, 40, 15, 25, 20, 20, 31, 13, 31, 30, 48, 25)),
                createBook("ruth", "Ruth", "ruth", listOf(22, 23, 18, 22)),
                createBook("1-sam", "1 Samuel", "1-sam", listOf(28, 36, 21, 22, 12, 21, 17, 22, 27, 27, 15, 25, 23, 52, 35, 23, 58, 30, 24, 42, 15, 23, 29, 22, 44, 25, 12, 25, 11, 31, 13)),
                createBook("2-sam", "2 Samuel", "2-sam", listOf(27, 32, 39, 12, 25, 23, 29, 18, 13, 19, 27, 31, 39, 33, 37, 23, 29, 33, 43, 26, 22, 51, 39, 25)),
                createBook("1-kgs", "1 Kings", "1-kgs", listOf(53, 46, 28, 34, 18, 38, 51, 66, 28, 29, 43, 33, 34, 31, 34, 34, 24, 46, 21, 43, 29, 53)),
                createBook("2-kgs", "2 Kings", "2-kgs", listOf(18, 25, 27, 44, 27, 33, 20, 29, 37, 36, 21, 21, 25, 25, 38, 20, 41, 37, 37, 21, 26, 20, 37, 20, 30)),
                createBook("1-chr", "1 Chronicles", "1-chr", listOf(54, 55, 24, 43, 26, 81, 40, 40, 44, 14, 47, 40, 14, 17, 29, 43, 27, 17, 19, 8, 30, 19, 32, 31, 31, 32, 34, 21, 30)),
                createBook("2-chr", "2 Chronicles", "2-chr", listOf(17, 18, 17, 22, 14, 42, 22, 18, 31, 19, 23, 16, 22, 15, 19, 14, 19, 34, 11, 37, 20, 12, 21, 27, 28, 23, 9, 27, 36, 27, 21, 33, 25, 33, 27, 23)),
                createBook("ezra", "Ezra", "ezra", listOf(11, 70, 13, 24, 17, 22, 28, 36, 15, 44)),
                createBook("neh", "Nehemiah", "neh", listOf(11, 20, 32, 23, 19, 19, 73, 18, 38, 39, 36, 47, 31)),
                createBook("esth", "Esther", "esth", listOf(22, 23, 15, 17, 14, 14, 10, 17, 32, 3)),
                createBook("job", "Job", "job", listOf(22, 13, 26, 21, 27, 30, 21, 22, 35, 22, 20, 25, 28, 22, 35, 22, 16, 21, 29, 29, 34, 30, 17, 25, 6, 14, 23, 28, 25, 31, 40, 22, 33, 37, 16, 33, 24, 41, 30, 24, 34, 17)),
                createBook("ps", "Psalms", "ps", listOf(6, 12, 8, 8, 12, 10, 17, 9, 20, 18, 7, 8, 6, 7, 5, 11, 15, 50, 14, 9, 13, 31, 6, 10, 22, 12, 14, 9, 11, 12, 24, 11, 22, 22, 28, 12, 40, 22, 13, 17, 13, 11, 5, 26, 17, 11, 9, 14, 20, 23, 19, 9, 6, 7, 23, 13, 11, 11, 17, 12, 8, 12, 11, 10, 13, 20, 7, 35, 36, 5, 24, 20, 28, 23, 10, 12, 20, 72, 13, 19, 16, 8, 18, 12, 13, 17, 7, 18, 52, 17, 16, 15, 5, 23, 11, 13, 12, 9, 9, 5, 8, 28, 22, 35, 45, 48, 43, 13, 31, 7, 10, 10, 9, 8, 18, 19, 2, 29, 176, 7, 8, 9, 4, 8, 5, 6, 5, 6, 8, 8, 3, 18, 3, 3, 21, 26, 9, 8, 24, 13, 10, 7, 12, 15, 21, 10, 20, 14, 9, 6)),
                createBook("prov", "Proverbs", "prov", listOf(33, 22, 35, 27, 23, 35, 27, 36, 18, 32, 31, 28, 25, 35, 33, 33, 28, 24, 29, 30, 31, 29, 35, 34, 28, 28, 27, 28, 27, 33, 31)),
                createBook("eccl", "Ecclesiastes", "eccl", listOf(18, 26, 22, 16, 20, 12, 29, 17, 18, 20, 10, 14)),
                createBook("song", "Song of Solomon", "song", listOf(17, 17, 11, 16, 16, 13, 13, 14)),
                createBook("isa", "Isaiah", "isa", listOf(31, 22, 26, 6, 30, 13, 25, 22, 21, 34, 16, 6, 22, 32, 9, 14, 14, 7, 25, 6, 17, 25, 18, 23, 12, 21, 13, 29, 24, 33, 9, 20, 24, 17, 10, 22, 38, 22, 8, 31, 29, 25, 28, 28, 25, 13, 15, 22, 26, 11, 23, 15, 12, 17, 13, 12, 21, 14, 21, 22, 11, 12, 19, 12, 25, 24)),
                createBook("jer", "Jeremiah", "jer", listOf(19, 37, 25, 31, 31, 30, 34, 22, 26, 25, 23, 17, 27, 22, 21, 21, 27, 23, 15, 18, 14, 30, 40, 10, 38, 24, 22, 17, 32, 24, 40, 44, 26, 22, 19, 32, 21, 28, 18, 16, 18, 22, 13, 30, 5, 28, 7, 47, 39, 46, 64, 34)),
                createBook("lam", "Lamentations", "lam", listOf(22, 22, 66, 22, 22)),
                createBook("ezek", "Ezekiel", "ezek", listOf(28, 10, 27, 17, 17, 14, 27, 18, 11, 22, 25, 28, 23, 23, 8, 63, 24, 32, 14, 49, 32, 31, 49, 27, 17, 21, 36, 26, 21, 26, 18, 32, 33, 31, 15, 38, 28, 23, 29, 49, 26, 20, 27, 31, 25, 24, 23, 35)),
                createBook("dan", "Daniel", "dan", listOf(21, 49, 30, 37, 31, 28, 28, 27, 27, 21, 45, 13)),
                createBook("hos", "Hosea", "hos", listOf(11, 23, 5, 19, 15, 11, 16, 14, 17, 15, 12, 14, 16, 9)),
                createBook("joel", "Joel", "joel", listOf(20, 32, 21)),
                createBook("amos", "Amos", "amos", listOf(15, 16, 15, 13, 27, 14, 17, 14, 15)),
                createBook("obad", "Obadiah", "obad", listOf(21)),
                createBook("jonah", "Jonah", "jonah", listOf(17, 10, 10, 11)),
                createBook("micah", "Micah", "micah", listOf(16, 13, 12, 13, 15, 16, 20)),
                createBook("nahum", "Nahum", "nahum", listOf(15, 13, 19)),
                createBook("hab", "Habakkuk", "hab", listOf(17, 20, 19)),
                createBook("zeph", "Zephaniah", "zeph", listOf(18, 15, 20)),
                createBook("hag", "Haggai", "hag", listOf(15, 23)),
                createBook("zech", "Zechariah", "zech", listOf(21, 13, 10, 14, 11, 15, 14, 23, 17, 12, 17, 14, 9, 21)),
                createBook("mal", "Malachi", "mal", listOf(14, 17, 18, 6))
            )
        ),
        Volume(
            id = "nt",
            name = "New Testament",
            slug = "nt",
            books = listOf(
                createBook("matt", "Matthew", "matt", listOf(25, 23, 17, 25, 48, 34, 29, 34, 38, 42, 30, 50, 58, 36, 39, 28, 27, 35, 30, 34, 46, 46, 39, 51, 46, 75, 66, 20)),
                createBook("mark", "Mark", "mark", listOf(45, 28, 35, 41, 43, 56, 37, 38, 50, 52, 33, 44, 37, 72, 47, 20)),
                createBook("luke", "Luke", "luke", listOf(80, 52, 38, 44, 39, 49, 50, 56, 62, 42, 54, 59, 35, 35, 32, 31, 37, 43, 48, 47, 38, 71, 56, 53)),
                createBook("john", "John", "john", listOf(51, 25, 36, 54, 47, 71, 53, 59, 41, 42, 57, 50, 38, 31, 27, 33, 26, 40, 42, 31, 25)),
                createBook("acts", "Acts", "acts", listOf(26, 47, 26, 37, 42, 15, 60, 40, 43, 48, 30, 25, 52, 28, 41, 40, 34, 28, 41, 38, 40, 30, 35, 27, 27, 32, 44, 31)),
                createBook("rom", "Romans", "rom", listOf(32, 29, 31, 25, 21, 23, 25, 39, 33, 21, 36, 21, 14, 23, 33, 27)),
                createBook("1-cor", "1 Corinthians", "1-cor", listOf(31, 16, 23, 21, 13, 20, 40, 13, 27, 33, 34, 31, 13, 40, 58, 24)),
                createBook("2-cor", "2 Corinthians", "2-cor", listOf(24, 17, 18, 18, 21, 18, 16, 24, 15, 18, 33, 21, 14)),
                createBook("gal", "Galatians", "gal", listOf(24, 21, 29, 31, 26, 18)),
                createBook("eph", "Ephesians", "eph", listOf(23, 22, 21, 32, 33, 24)),
                createBook("phil", "Philippians", "phil", listOf(30, 30, 21, 23)),
                createBook("col", "Colossians", "col", listOf(29, 23, 25, 18)),
                createBook("1-thess", "1 Thessalonians", "1-thess", listOf(10, 20, 13, 18, 28)),
                createBook("2-thess", "2 Thessalonians", "2-thess", listOf(12, 17, 18)),
                createBook("1-tim", "1 Timothy", "1-tim", listOf(20, 15, 16, 16, 25, 21)),
                createBook("2-tim", "2 Timothy", "2-tim", listOf(18, 26, 17, 22)),
                createBook("titus", "Titus", "titus", listOf(16, 15, 15)),
                createBook("philem", "Philemon", "philem", listOf(25)),
                createBook("heb", "Hebrews", "heb", listOf(14, 18, 19, 16, 14, 20, 28, 13, 28, 39, 40, 29, 25)),
                createBook("james", "James", "james", listOf(27, 26, 18, 17, 20)),
                createBook("1-pet", "1 Peter", "1-pet", listOf(25, 25, 22, 19, 14)),
                createBook("2-pet", "2 Peter", "2-pet", listOf(21, 22, 18)),
                createBook("1-jn", "1 John", "1-jn", listOf(10, 29, 24, 21, 21)),
                createBook("2-jn", "2 John", "2-jn", listOf(13)),
                createBook("3-jn", "3 John", "3-jn", listOf(14)),
                createBook("jude", "Jude", "jude", listOf(25)),
                createBook("rev", "Revelation", "rev", listOf(20, 29, 22, 11, 14, 17, 17, 13, 21, 11, 19, 17, 18, 20, 8, 21, 18, 24, 21, 15, 27, 21))
            )
        ),
        Volume(
            id = "bofm",
            name = "Book of Mormon",
            slug = "bofm",
            books = listOf(
                createBook("1-ne", "1 Nephi", "1-ne", listOf(20, 24, 31, 38, 22, 6, 22, 38, 6, 22, 36, 23, 42, 30, 36, 39, 55, 25, 24, 22, 26, 31)),
                createBook("2-ne", "2 Nephi", "2-ne", listOf(32, 30, 25, 35, 34, 18, 11, 25, 54, 25, 8, 22, 26, 6, 30, 13, 25, 22, 21, 34, 16, 6, 22, 32, 30, 33, 35, 32, 14, 18, 21, 9, 15)),
                createBook("jacob", "Jacob", "jacob", listOf(19, 35, 14, 18, 77, 13, 27)),
                createBook("enos", "Enos", "enos", listOf(27)),
                createBook("jarom", "Jarom", "jarom", listOf(15)),
                createBook("omni", "Omni", "omni", listOf(30)),
                createBook("w-of-m", "Words of Mormon", "w-of-m", listOf(18)),
                createBook("mosiah", "Mosiah", "mosiah", listOf(18, 41, 27, 30, 15, 7, 33, 21, 19, 22, 29, 37, 35, 12, 31, 15, 20, 35, 29, 26, 36, 16, 39, 25, 24, 39, 37, 20, 47)),
                createBook("alma", "Alma", "alma", listOf(33, 38, 27, 20, 62, 8, 27, 32, 34, 32, 46, 37, 31, 29, 19, 21, 39, 43, 36, 30, 23, 35, 18, 30, 17, 37, 30, 14, 17, 60, 38, 43, 23, 41, 16, 30, 47, 15, 19, 26, 15, 31, 54, 24, 24, 41, 36, 25, 30, 40, 37, 40, 23, 24, 35, 57, 36, 41, 13, 36, 21, 52, 17)),
                createBook("hel", "Helaman", "hel", listOf(34, 14, 37, 26, 52, 41, 29, 28, 41, 19, 38, 26, 39, 31, 17, 25)),
                createBook("3-ne", "3 Nephi", "3-ne", listOf(30, 19, 26, 33, 26, 30, 26, 25, 22, 19, 41, 48, 34, 27, 24, 20, 25, 39, 36, 46, 29, 17, 14, 18, 6, 21, 33, 40, 9, 2)),
                createBook("4-ne", "4 Nephi", "4-ne", listOf(49)),
                createBook("morm", "Mormon", "morm", listOf(19, 29, 22, 23, 24, 22, 10, 41, 37)),
                createBook("ether", "Ether", "ether", listOf(43, 25, 28, 19, 6, 30, 27, 26, 35, 34, 23, 41, 31, 31, 34)),
                createBook("moro", "Moroni", "moro", listOf(4, 3, 4, 3, 2, 9, 48, 30, 26, 34))
            )
        ),
        Volume(
            id = "dc-testament",
            name = "Doctrine and Covenants",
            slug = "dc-testament",
            books = listOf(
                createBook("dc", "Doctrine and Covenants", "dc", listOf(39, 1, 25, 3, 37, 11, 20, 4, 12, 11, 30, 6, 6, 11, 20, 4, 11, 5, 6, 14, 5, 2, 7, 5, 6, 12, 4, 3, 17, 6, 13, 5, 15, 4, 33, 16, 40, 42, 69, 3, 6, 2, 7, 26, 15, 35, 6, 4, 33, 30, 18, 42, 15, 10, 28, 16, 26, 62, 40, 34, 36, 3, 4, 13, 11, 4, 28, 30, 45, 18, 10, 26, 26, 120, 22, 14, 19, 4, 33, 4, 7, 31, 24, 120, 41, 14, 13, 141, 32, 37, 33, 2, 22, 26, 11, 12, 28, 31, 20, 8, 16, 30, 31, 18, 30, 37, 100, 10, 3, 31, 11, 26, 30, 7, 5, 1, 15, 21, 3, 1, 46, 12, 2, 145, 28, 58, 12, 2, 3, 38, 7, 3, 6, 4, 5, 1, 60)),
                createBook("od", "Official Declarations", "od", listOf(1, 1))
            )
        ),
        Volume(
            id = "pgp",
            name = "Pearl of Great Price",
            slug = "pgp",
            books = listOf(
                createBook("moses", "Moses", "moses", listOf(42, 31, 25, 32, 59, 68, 69, 30)),
                createBook("abr", "Abraham", "abr", listOf(31, 25, 28, 31, 21)),
                createBook("js-m", "Joseph Smith—Matthew", "js-m", listOf(55)),
                createBook("js-h", "Joseph Smith—History", "js-h", listOf(75)),
                createBook("a-of-f", "Articles of Faith", "a-of-f", listOf(13))
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
