package de.smartsteuer.frank.adventofcode2024.day01

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LocationDistancesAlternativeTest {
   private val input = listOf(
     "3   4",
     "4   3",
     "2   5",
     "1   3",
     "3   9",
     "3   3",
   )

  @Test
  fun `transpose transposes list of lists`() {
    emptyList<List<Int>>().transpose().shouldBeEmpty()
    input.parseLocations().transpose() shouldBe listOf(listOf(3, 4, 2, 1, 3, 3), listOf(4, 3, 5, 3, 9, 3))  }

  @Test
  fun `part1 returns expected result`() {
    part1a(input.parseLocations()) shouldBe 11
  }

  @Test
  fun `part2 returns expected result`() {
    part2a(input.parseLocations()) shouldBe 31
  }
}