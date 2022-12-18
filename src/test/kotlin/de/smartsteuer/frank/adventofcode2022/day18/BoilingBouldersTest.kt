package de.smartsteuer.frank.adventofcode2022.day18

import de.smartsteuer.frank.adventofcode2022.day18.Day18.Cube
import de.smartsteuer.frank.adventofcode2022.day18.Day18.parseDroplets
import de.smartsteuer.frank.adventofcode2022.day18.Day18.part1
import de.smartsteuer.frank.adventofcode2022.day18.Day18.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BoilingBouldersTest {
  private val input = listOf(
    "2,2,2",
    "1,2,2",
    "3,2,2",
    "2,1,2",
    "2,3,2",
    "2,2,1",
    "2,2,3",
    "2,2,4",
    "2,2,6",
    "1,2,5",
    "3,2,5",
    "2,1,5",
    "2,3,5",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 64
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 1707
  }

  @Test
  fun `droplets can be parsed`() {
    parseDroplets(input) shouldBe listOf(
      Cube(2, 2, 2),
      Cube(1, 2, 2),
      Cube(3, 2, 2),
      Cube(2, 1, 2),
      Cube(2, 3, 2),
      Cube(2, 2, 1),
      Cube(2, 2, 3),
      Cube(2, 2, 4),
      Cube(2, 2, 6),
      Cube(1, 2, 5),
      Cube(3, 2, 5),
      Cube(2, 1, 5),
      Cube(2, 3, 5),
    )
  }
}