package de.smartsteuer.frank.adventofcode2022.day11

import de.smartsteuer.frank.adventofcode2022.day11.Day11.Monkey
import de.smartsteuer.frank.adventofcode2022.day11.Day11.MonkeyRule
import de.smartsteuer.frank.adventofcode2022.day11.Day11.AddOperation
import de.smartsteuer.frank.adventofcode2022.day11.Day11.MultiplyOperation
import de.smartsteuer.frank.adventofcode2022.day11.Day11.SquareOperation
import de.smartsteuer.frank.adventofcode2022.day11.Day11.parseMonkeyRules
import de.smartsteuer.frank.adventofcode2022.day11.Day11.part1
import de.smartsteuer.frank.adventofcode2022.day11.Day11.part2
import de.smartsteuer.frank.adventofcode2022.day11.Day11.replaced
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MonkeyInTheMiddleTest {

  private val input = listOf(
    "Monkey 0:",
    "  Starting items: 79, 98",
    "  Operation: new = old * 19",
    "  Test: divisible by 23",
    "    If true: throw to monkey 2",
    "    If false: throw to monkey 3",
    "",
    "Monkey 1:",
    "  Starting items: 54, 65, 75, 74",
    "  Operation: new = old + 6",
    "  Test: divisible by 19",
    "    If true: throw to monkey 2",
    "    If false: throw to monkey 0",
    "",
    "Monkey 2:",
    "  Starting items: 79, 60, 97",
    "  Operation: new = old * old",
    "  Test: divisible by 13",
    "    If true: throw to monkey 1",
    "    If false: throw to monkey 3",
    "",
    "Monkey 3:",
    "  Starting items: 74",
    "  Operation: new = old + 3",
    "  Test: divisible by 17",
    "    If true: throw to monkey 0",
    "    If false: throw to monkey 1"
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 10_605
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 2_713_310_158
  }

  @Test
  fun `monkey rules can be parsed`() {
    parseMonkeyRules(input) shouldContainExactly listOf(
      Monkey(listOf(79, 98),         MonkeyRule(MultiplyOperation(19), Day11.Test(23, 2, 3))),
      Monkey(listOf(54, 65, 75, 74), MonkeyRule(AddOperation     ( 6), Day11.Test(19, 2, 0))),
      Monkey(listOf(79, 60, 97),     MonkeyRule(SquareOperation,       Day11.Test(13, 1, 3))),
      Monkey(listOf(74),             MonkeyRule(AddOperation     ( 3), Day11.Test(17, 0, 1))),
    )
  }

  @Test
  fun `replaced() returns expected list with replaced element`() {
    listOf(1, 2, 3).replaced(0, 5) shouldContainExactly listOf(5, 2, 3)
    listOf(1, 2, 3).replaced(1, 5) shouldContainExactly listOf(1, 5, 3)
    listOf(1, 2, 3).replaced(2, 5) shouldContainExactly listOf(1, 2, 5)
  }
}
