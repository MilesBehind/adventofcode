package de.smartsteuer.frank.adventofcode2022.day12

import de.smartsteuer.frank.adventofcode2022.day12.Day12.HeightMap
import de.smartsteuer.frank.adventofcode2022.day12.Day12.parseHeightMap
import de.smartsteuer.frank.adventofcode2022.day12.Day12.part1
import de.smartsteuer.frank.adventofcode2022.day12.Day12.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class HillClimbingAlgorithmTest {

  @Suppress("SpellCheckingInspection")
  private val input = listOf(
    "Sabqponm",
    "abcryxxl",
    "accszExk",
    "acctuvwj",
    "abdefghi",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 31
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 29
  }

  @Test
  fun `height map can be parsed`() {
    fun c(x: Int, y: Int) = Day12.Coordinate(x, y)
    parseHeightMap(input) shouldBe HeightMap(
      mapOf(
        c(0, 0) to 0, c(1, 0) to 0, c(2, 0) to 1, c(3, 0) to 16, c(4, 0) to 15, c(5, 0) to 14, c(6, 0) to 13, c(7, 0) to 12,
        c(0, 1) to 0, c(1, 1) to 1, c(2, 1) to 2, c(3, 1) to 17, c(4, 1) to 24, c(5, 1) to 23, c(6, 1) to 23, c(7, 1) to 11,
        c(0, 2) to 0, c(1, 2) to 2, c(2, 2) to 2, c(3, 2) to 18, c(4, 2) to 25, c(5, 2) to 25, c(6, 2) to 23, c(7, 2) to 10,
        c(0, 3) to 0, c(1, 3) to 2, c(2, 3) to 2, c(3, 3) to 19, c(4, 3) to 20, c(5, 3) to 21, c(6, 3) to 22, c(7, 3) to  9,
        c(0, 4) to 0, c(1, 4) to 1, c(2, 4) to 3, c(3, 4) to  4, c(4, 4) to  5, c(5, 4) to  6, c(6, 4) to  7, c(7, 4) to  8,
      ),
      c(0, 0), c(5, 2)
    )

  }
}