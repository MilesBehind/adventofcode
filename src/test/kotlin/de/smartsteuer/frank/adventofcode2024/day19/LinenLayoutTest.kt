package de.smartsteuer.frank.adventofcode2024.day19

import de.smartsteuer.frank.adventofcode2024.day19.LinenLayout.part1
import de.smartsteuer.frank.adventofcode2024.day19.LinenLayout.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LinenLayoutTest {
   private val input = listOf(
     "r, wr, b, g, bwu, rb, gb, br",
     "",
     "brwrr",
     "bggr",
     "gbbr",
     "rrbgbr",
     "ubwu",
     "bwurrg",
     "brgr",
     "bbrgwb",
   )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 6
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 16
  }
}