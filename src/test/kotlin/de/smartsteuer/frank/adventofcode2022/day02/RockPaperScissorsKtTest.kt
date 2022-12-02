package de.smartsteuer.frank.adventofcode2022.day02

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RockPaperScissorsKtTest {
  @Test
  fun `part 1`() {
    part1(listOf("A Y", "B X", "C Z")) shouldBe 15
  }

  @Test
  fun `part 2`() {
    part2(listOf("A Y", "B X", "C Z")) shouldBe 12
  }
}