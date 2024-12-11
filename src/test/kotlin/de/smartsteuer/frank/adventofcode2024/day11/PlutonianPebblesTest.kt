package de.smartsteuer.frank.adventofcode2024.day11

import de.smartsteuer.frank.adventofcode2024.day11.PlutonianPebbles.blink
import de.smartsteuer.frank.adventofcode2024.day11.PlutonianPebbles.part1
import de.smartsteuer.frank.adventofcode2024.day11.PlutonianPebbles.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PlutonianPebblesTest {
   private val input1 = listOf("125 17")
   private val input2 = listOf(125, 17).map { it.toLong() }
   private val input3 = listOf(0, 1, 10, 99, 999).map { it.toLong() }

  @Test
  fun `part 1 should return expected result`() {
    val input3 = input3.blink(1)
    input3 shouldBe listOf(1, 2024, 1, 0, 9, 9, 2021976).map { it.toLong() }
    part1(input1) shouldBe 55312
  }

  @Test
  fun `part 2 should return expected result`() {
    part2(input1) shouldBe 0
  }
}