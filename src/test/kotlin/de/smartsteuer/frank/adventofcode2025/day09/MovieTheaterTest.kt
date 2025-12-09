package de.smartsteuer.frank.adventofcode2025.day09

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MovieTheaterTest {
  private val input = listOf(
    "7,1",
    "11,1",
    "11,7",
    "9,7",
    "9,5",
    "2,5",
    "2,3",
    "7,3",
  )

  @Test
  fun `part 1 returns expected result`() {
    MovieTheater.part1(input) shouldBe 50
  }

  @Test
  fun `part 2 returns expected result`() {
    MovieTheater.part2(input) shouldBe 24
  }
}