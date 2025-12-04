package de.smartsteuer.frank.adventofcode2025.day04

import de.smartsteuer.frank.adventofcode2025.day04.Coordinate.Companion.c
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PaperRollsTest {
  private val input = listOf(
    "..@@.@@@@.",
    "@@@.@.@.@@",
    "@@@@@.@.@@",
    "@.@@@@..@.",
    "@@.@@@@.@@",
    ".@@@@@@@.@",
    ".@.@.@.@@@",
    "@.@@@.@@@@",
    ".@@@@@@@@.",
    "@.@.@@@.@.",
  )

  @Test
  fun `part 1 returns expected result`() {
    PrintingDepartment.part1(input) shouldBe 13
  }

  @Test
  fun `part 2 returns expected result`() {
    PrintingDepartment.part2(input) shouldBe 43
  }

  @Test
  fun `paper rolls can be parsed`() {
    input.parsePaperRolls() shouldBe PaperRolls(setOf(
      c(2, 0), c(3, 0), c(5, 0), c(6, 0), c(7, 0), c(8, 0),
      c(0, 1), c(1, 1), c(2, 1), c(4, 1), c(6, 1), c(8, 1), c(9, 1),
      c(0, 2), c(1, 2), c(2, 2), c(3, 2), c(4, 2), c(6, 2), c(8, 2), c(9, 2),
      c(0, 3), c(2, 3), c(3, 3), c(4, 3), c(5, 3), c(8, 3),
      c(0, 4), c(1, 4), c(3, 4), c(4, 4), c(5, 4), c(6, 4), c(8, 4), c(9, 4),
      c(1, 5), c(2, 5), c(3, 5), c(4, 5), c(5, 5), c(6, 5), c(7, 5), c(9, 5),
      c(1, 6), c(3, 6), c(5, 6), c(7, 6), c(8, 6), c(9, 6),
      c(0, 7), c(2, 7), c(3, 7), c(4, 7), c(6, 7), c(7, 7), c(8, 7), c(9, 7),
      c(1, 8), c(2, 8), c(3, 8), c(4, 8), c(5, 8), c(6, 8), c(7, 8), c(8, 8),
      c(0, 9), c(2, 9), c(4, 9), c(5, 9), c(6, 9), c(8, 9),
    ))
  }
}