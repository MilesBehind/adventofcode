package de.smartsteuer.frank.adventofcode2025.day07

import de.smartsteuer.frank.adventofcode2025.day07.Coordinate.Companion.c
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LaboratoriesTest {
  private val input = listOf(
    ".......S.......",
    "...............",
    ".......^.......",
    "...............",
    "......^.^......",
    "...............",
    ".....^.^.^.....",
    "...............",
    "....^.^...^....",
    "...............",
    "...^.^...^.^...",
    "...............",
    "..^...^.....^..",
    "...............",
    ".^.^.^.^.^...^.",
    "...............",
  )

  @Test
  fun `part 1 returns expected result`() {
    Laboratories.part1(input) shouldBe 21
  }

  @Test
  fun `part 2 returns expected result`() {
    Laboratories.part2(input) shouldBe 40
  }

  @Test
  fun `splitters can be parsed`() {
    input.parseSplitters() shouldBe Splitters(c(7, 0), setOf(
      c(7, 2),
      c(6, 4), c(8, 4),
      c(5, 6), c(7, 6), c(9, 6),
      c(4, 8), c(6, 8), c(10, 8),
      c(3, 10), c(5, 10), c(9, 10), c(11, 10),
      c(2, 12), c(6, 12), c(12, 12),
      c(1, 14), c(3, 14), c(5, 14), c(7, 14), c(9, 14), c(13, 14),
    ), 15, 16)
  }
}