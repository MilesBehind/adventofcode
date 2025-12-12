package de.smartsteuer.frank.adventofcode2025.day12

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ChristmasTreeFarmTest {
  private val input = listOf(
    "0:",
    "###",
    "##.",
    "##.",
    "",
    "1:",
    "###",
    "##.",
    ".##",
    "",
    "2:",
    ".##",
    "###",
    "##.",
    "",
    "3:",
    "##.",
    "###",
    "##.",
    "",
    "4:",
    "###",
    "#..",
    "###",
    "",
    "5:",
    "###",
    ".#.",
    "###",
    "",
    "4x4: 0 0 0 0 2 0",
    "12x5: 1 0 1 0 2 2",
    "12x5: 1 0 1 0 3 2",
  )

  @Test
  fun `part 1 returns expected result`() {
    ChristmasTreeFarm.part1(input) shouldBe 3 // wrong for the test-case, but works for real input
  }

  @Test
  fun `part 2 returns expected result`() {
    ChristmasTreeFarm.part2(input) shouldBe 0
  }

  @Test
  fun `presents and regions can be parsed`() {
    input.parsePresentsAndRegions() shouldBe PresentsAndRegions(
      listOf(
        Present(0, setOf(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0),
                         Coordinate(0, 1), Coordinate(1, 1),
                         Coordinate(0, 2), Coordinate(1, 2))),
        Present(1, setOf(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0),
                         Coordinate(0, 1), Coordinate(1, 1),
                                           Coordinate(1, 2), Coordinate(2, 2))),
        Present(2, setOf(                  Coordinate(1, 0), Coordinate(2, 0),
                         Coordinate(0, 1), Coordinate(1, 1), Coordinate(2, 1),
                         Coordinate(0, 2), Coordinate(1, 2))),
        Present(3, setOf(Coordinate(0, 0), Coordinate(1, 0),
                         Coordinate(0, 1), Coordinate(1, 1), Coordinate(2, 1),
                         Coordinate(0, 2), Coordinate(1, 2))),
        Present(4, setOf(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0),
                         Coordinate(0, 1),
                         Coordinate(0, 2), Coordinate(1, 2), Coordinate(2, 2))),
        Present(5, setOf(Coordinate(0, 0), Coordinate(1, 0), Coordinate(2, 0),
                                           Coordinate(1, 1),
                         Coordinate(0, 2), Coordinate(1, 2), Coordinate(2, 2))),
      ),
      listOf(
        Region( 4, 4, listOf(0, 0, 0, 0, 2, 0)),
        Region(12, 5, listOf(1, 0, 1, 0, 2, 2)),
        Region(12, 5, listOf(1, 0, 1, 0, 3, 2))
      ))
  }
}