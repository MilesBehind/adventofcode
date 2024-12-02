package de.smartsteuer.frank.adventofcode2023.day23//import static org.junit.jupiter.api.Assertions.*;

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class ALongWalkTest {
  private val lines = listOf(
  //           1111111111222
  // 01234567890123456789012
    "#.#####################",  //  0
    "#.......#########...###",  //  1
    "#######.#########.#.###",  //  2
    "###.....#.>.>.###.#.###",  //  3
    "###v#####.#v#.###.#.###",  //  4
    "###.>...#.#.#.....#...#",  //  5
    "###v###.#.#.#########.#",  //  6
    "###...#.#.#.......#...#",  //  7
    "#####.#.#.#######.#.###",  //  8
    "#.....#.#.#.......#...#",  //  9
    "#.#####.#.#.#########v#",  // 10
    "#.#...#...#...###...>.#",  // 11
    "#.#.#v#######v###.###v#",  // 12
    "#...#.>.#...>.>.#.###.#",  // 13
    "#####v#.#.###v#.#.###.#",  // 14
    "#.....#...#...#.#.#...#",  // 15
    "#.#########.###.#.#.###",  // 16
    "#...###...#...#...#.###",  // 17
    "###.###.#.###v#####v###",  // 18
    "#...#...#.#.>.>.#.>.###",  // 19
    "#.###.###.#.###.#.#v###",  // 20
    "#.....###...###...#...#",  // 21
    "#####################.#",  // 22
  )

  @Test
  fun `part 1`() {
    part1(parseTrailMap(lines)) shouldBe 94
  }

  @Test
  fun `part 2`() {
    part2(parseTrailMap(lines)) shouldBe 154
  }

  @Test
  fun `trail map can be parsed`() {
    val map = parseTrailMap(lines)
    map.width  shouldBe 23
    map.height shouldBe 23
    map.start  shouldBe Pos( 1,  0)
    map.goal   shouldBe Pos(21, 22)
    map.visualize() shouldBe lines
  }

  private fun TrailMap.visualize(): List<String> =
    (0..<height).map { y ->
      buildString {
        (0..<height).forEach { x ->
          append(when (this@visualize.tiles[Pos(x, y)]) {
                   TileType.FORREST     -> '#'
                   TileType.SLOPE_NORTH -> '^'
                   TileType.SLOPE_EAST  -> '>'
                   TileType.SLOPE_SOUTH -> 'v'
                   TileType.SLOPE_WEST  -> '<'
                   else                 -> '.'
                 })
        }
      }
    }
}