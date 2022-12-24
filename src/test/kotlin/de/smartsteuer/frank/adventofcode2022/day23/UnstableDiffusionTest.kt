package de.smartsteuer.frank.adventofcode2022.day23

import de.smartsteuer.frank.adventofcode2022.day23.Day23.parseGround
import de.smartsteuer.frank.adventofcode2022.day23.Day23.part1
import de.smartsteuer.frank.adventofcode2022.day23.Day23.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class UnstableDiffusionTest {
  private val input = listOf(
    "....#..",
    "..###.#",
    "#...#.#",
    ".#...##",
    "#.###..",
    "##.#.##",
    ".#..#..",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 110  // non-example result: 4049
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 20  // non-example result: 1021
  }

  @Test
  fun `ground can be parsed`() {
    parseGround(input).toString() shouldBe """
      ....#..
      ..###.#
      #...#.#
      .#...##
      #.###..
      ##.#.##
      .#..#..
     
    """.trimIndent()
  }
}
