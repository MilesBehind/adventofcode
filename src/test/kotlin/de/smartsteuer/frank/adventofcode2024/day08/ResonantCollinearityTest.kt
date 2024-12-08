package de.smartsteuer.frank.adventofcode2024.day08

import de.smartsteuer.frank.adventofcode2024.day08.ResonantCollinearity.Pos
import de.smartsteuer.frank.adventofcode2024.day08.ResonantCollinearity.parseMap
import de.smartsteuer.frank.adventofcode2024.day08.ResonantCollinearity.part1
import de.smartsteuer.frank.adventofcode2024.day08.ResonantCollinearity.part2
import de.smartsteuer.frank.adventofcode2024.day08.ResonantCollinearity.pickTwo
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ResonantCollinearityTest {
  private val input = listOf(
    "............",
    "........0...",
    ".....0......",
    ".......0....",
    "....0.......",
    "......A.....",
    "............",
    "............",
    "........A...",
    ".........A..",
    "............",
    "............",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 14
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 34
  }

  @Test
  fun `antiNodes are computed as expected`() {
    val map = input.parseMap()
    map.antiNodes(Pos(4, 3), Pos(5, 5)) shouldBe (Pos(3, 1) to Pos(6, 7))
    map.antiNodes(Pos(5, 5), Pos(4, 3)) shouldBe (Pos(6, 7) to Pos(3, 1))
  }

  @Test
  fun `antiNodes in line are computed as expected`() {
    val map = input.parseMap()
    map.antiNodesInLine(Pos(0, 0), Pos(1, 2)) shouldBe setOf(Pos(0, 0), Pos(1, 2), Pos(2, 4), Pos(3, 6), Pos(4, 8), Pos(5, 10))
    map.antiNodesInLine(Pos(1, 2), Pos(0, 0)) shouldBe setOf(Pos(0, 0), Pos(1, 2), Pos(2, 4), Pos(3, 6), Pos(4, 8), Pos(5, 10))

    map.antiNodesInLine(Pos(0, 0), Pos(3, 1)) shouldBe setOf(Pos(0, 0), Pos(3, 1), Pos(6, 2), Pos(9, 3))
    map.antiNodesInLine(Pos(3, 1), Pos(0, 0)) shouldBe setOf(Pos(0, 0), Pos(3, 1), Pos(6, 2), Pos(9, 3))
  }

  @Test
  fun `antennas with same  frequency can be found`() {
    input.parseMap().findAntennasOfSameFrequencies() shouldBe listOf(
      listOf(Pos(8, 1), Pos(5, 2), Pos(7, 3), Pos(4, 4)),
      listOf(Pos(6, 5), Pos(8, 8), Pos(9, 9))
    )
  }

  @Test
  fun `all combinations of 2 elements can be picked from a list`() {
    emptyList<Any>().pickTwo() shouldBe emptyList<Any>()
    listOf(1).pickTwo() shouldBe emptyList<Any>()
    listOf(1, 2).pickTwo() shouldBe listOf(1 to 2)
    listOf(1, 2, 3).pickTwo() shouldBe listOf(1 to 2, 1 to 3, 2 to 3)
    listOf(1, 2, 3, 4).pickTwo() shouldBe listOf(1 to 2, 1 to 3, 1 to 4, 2 to 3, 2 to 4, 3 to 4)
  }
}