package de.smartsteuer.frank.adventofcode2024.day05

import de.smartsteuer.frank.adventofcode2024.day05.PrintQueue.Rule
import de.smartsteuer.frank.adventofcode2024.day05.PrintQueue.parsePageUpdates
import de.smartsteuer.frank.adventofcode2024.day05.PrintQueue.part1
import de.smartsteuer.frank.adventofcode2024.day05.PrintQueue.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PrintQueueTest {
   private val input = listOf(
     "47|53",
     "97|13",
     "97|61",
     "97|47",
     "75|29",
     "61|13",
     "75|53",
     "29|13",
     "97|29",
     "53|29",
     "61|53",
     "97|53",
     "61|29",
     "47|13",
     "75|47",
     "97|75",
     "47|61",
     "75|61",
     "47|29",
     "75|13",
     "53|13",
     "",
     "75,47,61,53,29",
     "97,61,53,29,13",
     "75,29,13",
     "75,97,47,61,53",
     "61,13,29",
     "97,13,75,29,47",
   )

  @Test
  fun `part 1 returns expected result`() {
    part1(input.parsePageUpdates()) shouldBe 143
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input.parsePageUpdates()) shouldBe 123
  }

  @Test
  fun `input can be parsed`() {
    input.parsePageUpdates() shouldBe PrintQueue.PageUpdates(
      listOf(
        Rule(47, 53), Rule(97, 13), Rule(97, 61), Rule(97, 47), Rule(75, 29), Rule(61, 13), Rule(75, 53),
        Rule(29, 13), Rule(97, 29), Rule(53, 29), Rule(61, 53), Rule(97, 53), Rule(61, 29), Rule(47, 13),
        Rule(75, 47), Rule(97, 75), Rule(47, 61), Rule(75, 61), Rule(47, 29), Rule(75, 13), Rule(53, 13)
      ),
      listOf(
        listOf(75,47,61,53,29),
        listOf(97,61,53,29,13),
        listOf(75,29,13),
        listOf(75,97,47,61,53),
        listOf(61,13,29),
        listOf(97,13,75,29,47),
      )
    )
  }
}