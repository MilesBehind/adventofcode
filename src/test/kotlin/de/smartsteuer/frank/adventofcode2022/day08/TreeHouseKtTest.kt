package de.smartsteuer.frank.adventofcode2022.day08

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TreeHouseKtTest {
  private val input = listOf(
    "30373",
    "25512",
    "65332",
    "33549",
    "35390",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 21
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 8
  }
}