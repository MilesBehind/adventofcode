package de.smartsteuer.frank.adventofcode2021.day01

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DepthIncreaseCountKtTest {
  @Test
  internal fun `increase count for empty input is 0`() {
    countIncreases(emptyList()) shouldBe 0
  }

  @Test
  internal fun `increase count for single input is 0`() {
    countIncreases(listOf(1)) shouldBe 0
  }

  @Test
  internal fun `increase count for two inputs is ok`() {
    countIncreases(listOf(1, 2)) shouldBe 1
    countIncreases(listOf(2, 1)) shouldBe 0
  }

  @Test
  internal fun `increase count for three inputs is ok`() {
    countIncreases(listOf(1, 2, 3)) shouldBe 2
    countIncreases(listOf(1, 3, 2)) shouldBe 1
    countIncreases(listOf(2, 1, 3)) shouldBe 1
    countIncreases(listOf(2, 3, 1)) shouldBe 1
    countIncreases(listOf(3, 1, 2)) shouldBe 1
    countIncreases(listOf(3, 2, 1)) shouldBe 0
  }

  @Test
  internal fun `windowed increase count for less than 4 depths is 0`() {
    countIncreasesWindowed(emptyList())     shouldBe 0
    countIncreasesWindowed(listOf(1))       shouldBe 0
    countIncreasesWindowed(listOf(1, 2))    shouldBe 0
    countIncreasesWindowed(listOf(1, 2, 3)) shouldBe 0
  }

  @Test
  internal fun `windowed increase count for 4 depths is ok`() {
    countIncreasesWindowed(listOf(1, 2, 3, 4)) shouldBe 1
    countIncreasesWindowed(listOf(1, 3, 4, 2)) shouldBe 1
    countIncreasesWindowed(listOf(2, 3, 4, 1)) shouldBe 0
  }

  @Test
  internal fun `windowed increase count for 10 depths is 5`() {
    countIncreasesWindowed(listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)) shouldBe 5
  }
}