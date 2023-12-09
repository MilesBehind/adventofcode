package de.smartsteuer.frank.adventofcode2023.day09

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MirageMaintenanceTest {
  private val lines = listOf(
    "0 3 6 9 12 15",
    "1 3 6 10 15 21",
    "10 13 16 21 30 45",
  )

  @Test
  fun `part 1`() {
    part1(parseHistories(lines)) shouldBe 114  // 1647269739
  }

  @Test
  fun `part 2`() {
    part2(parseHistories(lines)) shouldBe 2  // 864
  }

  @Test
  fun `differences can be computed`() {
    listOf(0, 3, 6).computeDifferences() shouldBe listOf(3, 3)
    listOf(1, 3, 6).computeDifferences() shouldBe listOf(2, 3)
  }

  @Test
  fun `histories can be parsed`() {
    parseHistories(lines) shouldBe listOf(
      History(listOf(0, 3, 6, 9, 12, 15)),
      History(listOf(1, 3, 6, 10, 15, 21)),
      History(listOf(10, 13, 16, 21, 30, 45)),
    )
  }
}