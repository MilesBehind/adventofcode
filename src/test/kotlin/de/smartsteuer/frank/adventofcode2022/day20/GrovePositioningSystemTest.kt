package de.smartsteuer.frank.adventofcode2022.day20

import de.smartsteuer.frank.adventofcode2022.day20.Day20.part1
import de.smartsteuer.frank.adventofcode2022.day20.Day20.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GrovePositioningSystemTest {
  private val input = sequenceOf(
    "1",
    "2",
    "-3",
    "3",
    "-2",
    "0",
    "4",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 3
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 1_623_178_306
  }
}
