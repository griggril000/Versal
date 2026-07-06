package com.grigg.versal.model

import org.junit.Assert.assertEquals
import org.junit.Test

class VerseSelectionTest {

    @Test
    fun generateLink_noVerses_returnsChapterLink() {
        val selection = VerseSelection("bofm", "1-ne", 3)
        val expected = "https://www.churchofjesuschrist.org/study/scriptures/bofm/1-ne/3?lang=eng"
        assertEquals(expected, selection.generateLink())
    }

    @Test
    fun generateLink_singleVerse_returnsLinkWithId() {
        val selection = VerseSelection("bofm", "1-ne", 3, setOf(7))
        val expected = "https://www.churchofjesuschrist.org/study/scriptures/bofm/1-ne/3?lang=eng&id=p7#p7"
        assertEquals(expected, selection.generateLink())
    }

    @Test
    fun generateLink_verseRange_returnsLinkWithRangeId() {
        val selection = VerseSelection("bofm", "1-ne", 3, setOf(7, 8, 9))
        val expected = "https://www.churchofjesuschrist.org/study/scriptures/bofm/1-ne/3?lang=eng&id=p7-p9#p7"
        assertEquals(expected, selection.generateLink())
    }

    @Test
    fun generateLink_multipleRanges_returnsLinkWithCommaSeparatedIds() {
        val selection = VerseSelection("bofm", "1-ne", 3, setOf(1, 2, 7, 9, 10))
        val expected = "https://www.churchofjesuschrist.org/study/scriptures/bofm/1-ne/3?lang=eng&id=p1-p2,p7,p9-p10#p1"
        assertEquals(expected, selection.generateLink())
    }

    @Test
    fun generateLink_differentLanguage_returnsLinkWithLangParam() {
        val selection = VerseSelection("bofm", "1-ne", 3, setOf(7))
        val expected = "https://www.churchofjesuschrist.org/study/scriptures/bofm/1-ne/3?lang=spa&id=p7#p7"
        assertEquals(expected, selection.generateLink(lang = "spa"))
    }
}
