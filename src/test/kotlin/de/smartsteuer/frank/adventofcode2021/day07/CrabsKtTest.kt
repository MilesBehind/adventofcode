package de.smartsteuer.frank.adventofcode2021.day07

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CrabsKtTest {
  @Test
  internal fun `position with minimum fuel consumption should be 2`() {
    val (pos, fuel) = computePositionWithMinimumFuelConsumption(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14))
    pos  shouldBe  2
    fuel shouldBe 37
  }

  @Test
  internal fun `position with minimum fuel consumption for alternative fuel function should be 5`() {
    val (pos, fuel) = computePositionWithMinimumFuelConsumption(listOf(16, 1, 2, 0, 4, 2, 7, 1, 2, 14)) { it * (it + 1) / 2 }
    pos  shouldBe   5
    fuel shouldBe 168
  }
}