package de.smartsteuer.frank.adventofcode2025.day10

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.collections.mapIndexedNotNull

class MachinesTest {
  private val input = listOf(
    "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
    "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
    "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}",
  )

  @Test
  fun `part 1 returns expected result`() {
    Machines.part1(input) shouldBe 7
  }

  @Test
  fun `part 2 returns expected result`() {
    Machines.part2(input) shouldBe 33
  }

  @Test
  fun `machines can be parsed`() {
    val machines = input.parseMachines()
    machines shouldHaveSize 3
    machines.first() shouldBe Machine(listOf(false, true, true, false),
                                      listOf(Button(listOf(3   ), 4),
                                            Button(listOf(1, 3), 4),
                                            Button(listOf(2   ), 4),
                                            Button(listOf(2, 3), 4),
                                            Button(listOf(0, 2), 4),
                                            Button(listOf(0, 1), 4)),
                                      listOf(3, 5, 4, 7))
  }

  @Test
  fun `fewest clicks for required joltages are found`() {
    val machines = input.parseMachines()
    machines[0].findFewestButtonPressesForRequiredJoltages()
  }

  @Test
  fun `indicator pattern can be converted to mask`() {
    val booleans = ".###.#".toBooleans()
    booleans shouldBe listOf(false, true, true, true, false, true)
    val indexes = booleans.mapIndexedNotNull { index, flag -> if (flag) index else null}
    indexes shouldBe listOf(1, 2, 3, 5)
    indexes.toMask() shouldBe 0b0101110
  }

  @Test
  fun `button can be applied to state`() {
    val machines = input.parseMachines()
    val buttons = machines.first().buttons
    buttons.map { it.apply(0) } shouldBe listOf(0b1000, 0b1010, 0b0100, 0b1100, 0b0101, 0b0011)
  }

  @Test
  fun `reachable states can be computed`() {
    val joltages         = listOf(3, 5, 0, 0)
    val requiredJoltages = listOf(3, 5, 4, 7)
    val machines = input.parseMachines()
    val buttons = machines.first().buttons
    val newJoltages = buttons.map { button ->
      val newJoltages = joltages.mapIndexed { index, joltage -> joltage + if (index in button.targetIndicators) 1 else 0 }
      if (newJoltages.zip(requiredJoltages).any { (new, target) -> new > target }) null else newJoltages
    }
    newJoltages shouldBe listOf(listOf(3, 5, 0, 1),
                                null,
                                listOf(3, 5, 1, 0),
                                listOf(3, 5, 1, 1),
                                null,
                                null)
  }

  @Test
  fun `part 2 solutions for machines work`() {
    val machines = input.parseMachines()
    val machine1 = machines.first()
    val machine1Buttons = machine1.buttons
    val machine1StateSize = machine1.targetIndicators.size
    val resultState = listOf(0, 1, 1, 1, 3, 3, 3, 4, 5, 5).fold(List(machine1StateSize) { 0 }) { joltages, buttonIndex ->
      machine1Buttons[buttonIndex].apply(joltages)
    }
    resultState shouldBe listOf(3, 5, 4, 7)
  }

  private fun String.toBooleans() =
    trim().map { it == '#' }.toList()
}