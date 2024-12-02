package de.smartsteuer.frank.adventofcode2024.day01

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LocationDistancesTest {
   private val input = listOf(
     "3   4",
     "4   3",
     "2   5",
     "1   3",
     "3   9",
     "3   3",
   )

  @Test
  fun `locations can be parsed`() {
    input.parseLocations() shouldBe listOf(
      listOf(3, 4),
      listOf(4, 3),
      listOf(2, 5),
      listOf(1, 3),
      listOf(3, 9),
      listOf(3, 3),
    )
  }

  @Test
  fun `part1 returns expected result`() {
    part1a(input.parseLocations()) shouldBe 11
  }

  @Test
  fun `part2 returns expected result`() {
    part2a(input.parseLocations()) shouldBe 31
  }
}