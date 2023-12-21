package de.smartsteuer.frank.adventofcode2023.day21

import de.smartsteuer.frank.adventofcode2023.lines
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class StepCounterTest {
  private val lines = listOf(
    "...........",
    ".....###.#.",
    ".###.##..#.",
    "..#.#...#..",
    "....#.#....",
    ".##..S####.",
    ".##..#...#.",
    ".......##..",
    ".##.#.####.",
    ".##..##.##.",
    "...........",
  )
  
  private val reachableGardenTiles = listOf(
    "...........",
    "...........",
    "........O..",
    ".O.O.O.O...",
    "O.O.....O..",
    "...O.O.....",
    "....O.O....",
    ".O.O.O.....",
    "...........",
    "...O.......",
    "...........",
  )

  @Test
  fun `part 1`() {
    part1(parseGardenMap(lines), 6) shouldBe 16
  }

  @Test
  @Disabled
  fun `full part 1`() {
    val map = parseGardenMap(lines("/adventofcode2023/day21/map.txt"))

    val steps129 = map.findReachableGardenTiles(stepCount = 129)
    println()
    println(map.visualize(steps129).joinToString(separator = "\n"))

    val steps130 = map.findReachableGardenTiles(stepCount = 130)
    println()
    println(map.visualize(steps130).joinToString(separator = "\n"))

    println((128..136).associateWith { map.findReachableGardenTiles(stepCount = it).size })
  }

  @Test
  fun `reachable garden tiles can be found`() {
    val gardenMap = parseGardenMap(lines)
    val tiles = gardenMap.findReachableGardenTiles(stepCount = 6)
    println(gardenMap.visualize(tiles).joinToString(separator = "\n"))
    tiles shouldBe reachableGardenTiles.toSteps()
  }

  @Test
  fun `garden map can be parsed`() {
    parseGardenMap(lines).visualize() shouldBe lines
  }

  private fun GardenMap.visualize(steps: Set<Pos> = emptySet()): List<String> {
    return buildList {
      (0..<height).forEach { y ->
        add(buildString {
          (0..<width).forEach { x ->
            append(when {
                     Pos(x, y) == start && steps.isEmpty() -> 'S'
                     Pos(x, y) in steps                    -> 'O'
                     Pos(x, y) in rocks                    -> '#'
                     else                                  -> '.'
                   })
          }
        })
      }
    }
  }

  private fun List<String>.toSteps(): Set<Pos> = buildSet {
    this@toSteps.forEachIndexed { y: Int, line: String ->
      line.forEachIndexed { x, c ->
        if (c == 'O') add(Pos(x, y))
      }
    }
  }
}