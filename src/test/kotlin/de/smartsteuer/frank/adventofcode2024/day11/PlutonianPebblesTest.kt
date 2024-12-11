package de.smartsteuer.frank.adventofcode2024.day11

import de.smartsteuer.frank.adventofcode2024.day11.PlutonianPebbles.part1
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PlutonianPebblesTest {
   private val input1 = listOf("125 17")

  @Test
  fun `part 1 should return expected result`() {
    part1(input1) shouldBe 55312
  }
}