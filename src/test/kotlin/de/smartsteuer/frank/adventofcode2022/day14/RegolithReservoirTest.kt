package de.smartsteuer.frank.adventofcode2022.day14

import de.smartsteuer.frank.adventofcode2022.day14.Day14.part1
import de.smartsteuer.frank.adventofcode2022.day14.Day14.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RegolithReservoirTest {

  private val input = listOf(
    "498,4 -> 498,6 -> 496,6",
    "503,4 -> 502,4 -> 502,9 -> 494,9",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 24
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 93
  }
}