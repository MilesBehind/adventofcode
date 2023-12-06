package de.smartsteuer.frank.adventofcode2023.day06

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class WaitForItTest {
  private val input = listOf(
    "Time:      7  15   30",
    "Distance:  9  40  200"
  )

  @Test
  fun `part 1`() {
    part1(parseTimes(input)) shouldBe 288
  }

  @Test
  fun `part 2`() {
    part2(parseTimes(input)) shouldBe 71503
  }

  @Test
  fun `times can be parsed`() {
    parseTimes(input) shouldBe Times(
      listOf(7, 15,  30),
      listOf(9, 40, 200)
    )
  }
}