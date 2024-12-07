package de.smartsteuer.frank.adventofcode2024.day07

import de.smartsteuer.frank.adventofcode2024.day07.BridgeRepair.Equation
import de.smartsteuer.frank.adventofcode2024.day07.BridgeRepair.parseEquations
import de.smartsteuer.frank.adventofcode2024.day07.BridgeRepair.part1
import de.smartsteuer.frank.adventofcode2024.day07.BridgeRepair.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BridgeRepairTest {
  private val input = listOf(
    "190: 10 19",
    "3267: 81 40 27",
    "83: 17 5",
    "156: 15 6",
    "7290: 6 8 6 15",
    "161011: 16 10 13",
    "192: 17 8 14",
    "21037: 9 7 18 13",
    "292: 11 6 16 20",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 3749L
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 11387
  }

  @Test
  fun `equations can be parsed`() {
    input.parseEquations() shouldBe listOf(
      Equation(190,    listOf(10, 19)),
      Equation(3267,   listOf(81, 40, 27)),
      Equation(83,     listOf(17, 5)),
      Equation(156,    listOf(15, 6)),
      Equation(7290,   listOf(6, 8, 6, 15)),
      Equation(161011, listOf(16, 10, 13)),
      Equation(192,    listOf(17, 8, 14)),
      Equation(21037,  listOf(9, 7, 18, 13)),
      Equation(292,    listOf(11, 6, 16, 20)),
    )
  }
}