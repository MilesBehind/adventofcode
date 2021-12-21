package de.smartsteuer.frank.adventofcode2021.day08

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class NoteTest {

  private val note = Note.fromString("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")

  @Test
  internal fun `decoding given note returns expected result`() {
    // when: note is decoded
    val decoded: List<DigitAndSegments> = note.decodeNotes().sortedBy { it.digit }
    val decodedAsMap = decoded.associate { it.digit to it.segments.toString() }
    // then: the result should be correct
    decodedAsMap shouldBe mapOf(0 to "abcdeg",
                                1 to "ab",
                                2 to "acdfg",
                                3 to "abcdf",
                                4 to "abef",
                                5 to "bcdef",
                                6 to "bcdefg",
                                7 to "abd",
                                8 to "abcdefg",
                                9 to "abcdef")
  }

  @Test
  internal fun `decoding digits for given note returns expected digits`() {
    // when: note digits are decoded
    val decoded: List<Int> = note.decodeDigits()
    // then: digits should be correct
    decoded shouldContainExactly listOf(5, 3, 5, 3)
  }

  @Test
  internal fun `decoding number should return expected number`() {
    note.decodeNumber() shouldBe 5353
  }
}