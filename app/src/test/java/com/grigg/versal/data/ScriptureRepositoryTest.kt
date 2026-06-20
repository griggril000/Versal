package com.grigg.versal.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ScriptureRepositoryTest {

    @Test
    fun testVolumesCount() {
        assertEquals(5, ScriptureRepository.volumes.size)
    }

    @Test
    fun testSearchBooks() {
        val results = ScriptureRepository.searchBooks("Nephi")
        assertTrue(results.any { it.second.name == "1 Nephi" })
        assertTrue(results.any { it.second.name == "2 Nephi" })
        assertTrue(results.any { it.second.name == "3 Nephi" })
        assertTrue(results.any { it.second.name == "4 Nephi" })
    }

    @Test
    fun testGetChapter() {
        val chapter = ScriptureRepository.getChapter("bofm", "1-ne", 1)
        assertNotNull(chapter)
        assertEquals(1, chapter?.number)
    }

    @Test
    fun testAllVolumesPresent() {
        val slugs = ScriptureRepository.volumes.map { it.slug }
        assertTrue(slugs.contains("ot"))
        assertTrue(slugs.contains("nt"))
        assertTrue(slugs.contains("bofm"))
        assertTrue(slugs.contains("dc-testament"))
        assertTrue(slugs.contains("pgp"))
    }
}
