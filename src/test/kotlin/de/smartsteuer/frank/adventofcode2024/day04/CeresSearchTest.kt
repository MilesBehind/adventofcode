package de.smartsteuer.frank.adventofcode2024.day04

import de.smartsteuer.frank.adventofcode2024.day04.CeresSearch.toWords
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CeresSearchTest {
   private val input = listOf(
     "MMMSXXMASM",
     "MSAMXMSMSA",
     "AMXSXMAAMM",
     "MSAMASMSMX",
     "XMASAMXAMM",
     "XXAMMXXAMA",
     "SMSMSASXSS",
     "SAXAMASAAA",
     "MAMMMXMMMM",
     "MXMXAXMASX",
   )

  @Test
  fun `part 1 returns expected result`() {
    CeresSearch.part1(input.toWords()) shouldBe 18
  }

  @Test
  fun `part 2 returns expected result`() {
    CeresSearch.part2(input.toWords()) shouldBe 9
  }
}