package de.smartsteuer.frank.adventofcode2025.day07

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
  fun `splitters2 can be parsed`() {
    input.parseSplitters() shouldBe Splitters(7, listOf(
      setOf(7),
      setOf(6, 8),
      setOf(5, 7, 9),
      setOf(4, 6, 10),
      setOf(3, 5, 9,11),
      setOf(2, 6, 12),
      setOf(1, 3, 5, 7, 9, 13)
    ))
  }
}