package de.smartsteuer.frank.adventofcode2022.day05

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CrateStacksTest {
  private val input = listOf(
    "    [D]",
    "[N] [C]",
    "[Z] [M] [P]",
    " 1   2   3",
    "",
    "move 1 from 2 to 1",
    "move 3 from 1 to 3",
    "move 2 from 2 to 1",
    "move 1 from 1 to 2",
  )

  @Test
  fun `part1 is correct`() {
    part1(input) shouldBe "CMZ"
  }

  @Test
  fun `part2 is correct`() {
    part2(input) shouldBe "MCD"
  }

  @Test
  fun `stacks can be parsed`() {
    parseStacks(input) shouldBe listOf( "ZN".toList(),
                                       "MCD".toList(),
                                         "P".toList())
  }

  @Test
  fun `movement can be parsed`() {
    parseMovements(input).toList() shouldBe listOf(Movement(1, 2, 1),
                                                   Movement(3, 1, 3),
                                                   Movement(2, 2, 1),
                                                   Movement(1, 1, 2))
  }
}