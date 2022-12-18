package de.smartsteuer.frank.adventofcode2022.day17

import de.smartsteuer.frank.adventofcode2022.day17.Day17.findRepeatingPattern
import de.smartsteuer.frank.adventofcode2022.day17.Day17.part1
import de.smartsteuer.frank.adventofcode2022.day17.Day17.part2
import io.kotest.assertions.assertSoftly
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

  @Test
  fun `repeating pattern is found`() {
    assertSoftly {
      listOf(emptyList(), listOf(1), listOf(1, 2, 3, 4, 5)).forEach { it.findRepeatingPattern() shouldBe (0 to 0) }
      val tooShortRepeatedPattern = (1..19).toList()
      (listOf(1, 2, 3, 4, 5) + tooShortRepeatedPattern + tooShortRepeatedPattern).findRepeatingPattern() shouldBe (0 to 0)
      val repeatedPattern = (1..20).toList()
      (listOf(1, 2, 3, 4, 5) + repeatedPattern + repeatedPattern).findRepeatingPattern() shouldBe (5 to 20)
    }
  }
}
