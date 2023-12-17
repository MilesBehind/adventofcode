package de.smartsteuer.frank.adventofcode2023.day16

import io.kotest.matchers.*
import io.kotest.matchers.collections.shouldContainAll
import org.junit.jupiter.api.Test

class TheFloorWillBeLavaTest {
  private val input = listOf(
    """.|...\....""",
    """|.-.\.....""",
    """.....|-...""",
    """........|.""",
    """..........""",
    """.........\""",
    """..../.\\..""",
    """.-.-/..|..""",
    """.|....-|.\""",
    """..//.|....""",
  )

  @Test
  fun `part 1`() {
    part1(parseLayout(input)) shouldBe 46  // full part 1: 7979
  }

  @Test
  fun `part 2`() {
    part2(parseLayout(input)) shouldBe 51
  }

  @Test
  fun `layout can be parsed`() {
    val tiles = parseLayout(input).tiles
    tiles[Pos(9, 0)] shouldBe '.'
    tiles[Pos(0, 9)] shouldBe '.'
    tiles[Pos(5, 2)] shouldBe '|'
  }

  @Test
  fun `energized tiles can be computed`() {
    parseLayout(input).computeEnergizedTiles() shouldBe parseEnergizedTiles(listOf(
      "######....",
      ".#...#....",
      ".#...#####",
      ".#...##...",
      ".#...##...",
      ".#...##...",
      ".#..####..",
      "########..",
      ".#######..",
      ".#...#.#..",
    ))
  }

  @Test
  fun `start states can be found`() {
    parseLayout(input).findStartStates() shouldContainAll listOf(
      BeamState(Pos(0, 0), Pos( 1, 0)), BeamState(Pos(0, 0), Pos(0,  1)),
      BeamState(Pos(9, 0), Pos(-1, 0)), BeamState(Pos(9, 0), Pos(0,  1)),
      BeamState(Pos(0, 9), Pos( 1, 0)), BeamState(Pos(0, 9), Pos(0, -1)),
      BeamState(Pos(9, 9), Pos(-1, 0)), BeamState(Pos(9, 9), Pos(0, -1)),
    )
  }

  private fun parseEnergizedTiles(tiles: List<String>): Set<Pos> =
    buildSet {
      tiles.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
          if (c == '#') add(Pos(x, y))
        }
      }
    }
}