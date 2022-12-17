package de.smartsteuer.frank.adventofcode2022.day17

import de.smartsteuer.frank.adventofcode2022.day17.Day17.part1
import de.smartsteuer.frank.adventofcode2022.day17.Day17.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PyroclasticFlowTest {
  private val input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 3_068
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 1_514_285_714_288L
  }
}
