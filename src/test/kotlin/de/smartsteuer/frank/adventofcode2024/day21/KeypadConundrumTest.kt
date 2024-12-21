package de.smartsteuer.frank.adventofcode2024.day21

import de.smartsteuer.frank.adventofcode2024.day21.KeypadConundrum.parseCodes
import de.smartsteuer.frank.adventofcode2024.day21.KeypadConundrum.part1
import de.smartsteuer.frank.adventofcode2024.day21.KeypadConundrum.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class KeypadConundrumTest {
  private val input = listOf(
    "029A",
    "980A",
    "179A",
    "456A",
    "379A",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 126384
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 42
  }

  @Test
  fun `movements are computed correctly`() {
    input.parseCodes().computeMovements() shouldBe mapOf("029A" to 68L,
                                                         "980A" to 60L,
                                                         "179A" to 68L,
                                                         "456A" to 64L,
                                                         "379A" to 64L)
  }
}