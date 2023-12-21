package de.smartsteuer.frank.adventofcode2023.day21

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val map = parseGardenMap(lines("/adventofcode2023/day21/map.txt"))
  measureTime { println("part 1: ${part1(map)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(map)}") }.also { println("part 2 took $it") }
}

internal fun part1(map: GardenMap, stepCount: Int = 64): Int =
  map.findReachableGardenTiles(stepCount = stepCount).size

internal fun part2(map: GardenMap, stepCount: Int = 26_501_365): Long {
  require(map.width == map.height)
  val size = map.width
  require(stepCount % size == size / 2)
  require(size % 2 == 1)
  val mapRepeatCount = stepCount / size - 1

  val oddCount  = (mapRepeatCount       / 2 * 2 + 1).squared()
  val evenCount = ((mapRepeatCount + 1) / 2 * 2    ).squared()

  val oddSteps = map.findReachableGardenTiles(map.start, size + 2).size.toLong()
  val evenStep = map.findReachableGardenTiles(map.start, size + 1).size.toLong()

  val cornerTopSteps    = map.findReachableGardenTiles(Pos(map.start.x, size - 1   ), size - 1).size.toLong()
  val cornerRightSteps  = map.findReachableGardenTiles(Pos(0,           map.start.y), size - 1).size.toLong()
  val cornerBottomSteps = map.findReachableGardenTiles(Pos(map.start.x, 0          ), size - 1).size.toLong()
  val cornerLeftSteps   = map.findReachableGardenTiles(Pos(size - 1,    map.start.y), size - 1).size.toLong()

  val smallTopRightSteps    = map.findReachableGardenTiles(Pos(0,        size - 1), size / 2 - 1).size.toLong()
  val smallTopLeftSteps     = map.findReachableGardenTiles(Pos(size - 1, size - 1), size / 2 - 1).size.toLong()
  val smallBottomRightSteps = map.findReachableGardenTiles(Pos(0,        0       ), size / 2 - 1).size.toLong()
  val smallBottomLeftSteps  = map.findReachableGardenTiles(Pos(size - 1, 0       ), size / 2 - 1).size.toLong()

  val largeTopRightSteps    = map.findReachableGardenTiles(Pos(0,        size - 1), size * 3 / 2 - 1).size.toLong()
  val largeTopLeftSteps     = map.findReachableGardenTiles(Pos(size - 1, size - 1), size * 3 / 2 - 1).size.toLong()
  val largeBottomRightSteps = map.findReachableGardenTiles(Pos(0,        0       ), size * 3 / 2 - 1).size.toLong()
  val largeBottomLeftSteps  = map.findReachableGardenTiles(Pos(size - 1, 0       ), size * 3 / 2 - 1).size.toLong()
  return oddCount  * oddSteps +
         evenCount * evenStep +
         cornerTopSteps + cornerRightSteps + cornerBottomSteps + cornerLeftSteps +
         (mapRepeatCount + 1) * (smallTopRightSteps + smallTopLeftSteps + smallBottomRightSteps + smallBottomLeftSteps) +
         mapRepeatCount       * (largeTopRightSteps + largeTopLeftSteps + largeBottomRightSteps + largeBottomLeftSteps)
}

internal data class Pos(val x: Int, val y: Int) {
  fun neighbours() = listOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y - 1), Pos (x, y + 1))
}

internal data class GardenMap(val width: Int, val height: Int, val rocks: Set<Pos>, val start: Pos) {
  fun findReachableGardenTiles(start: Pos = this.start, stepCount: Int): Set<Pos> {
    tailrec fun findReachableGardenTiles(stepCount: Int, result: Set<Pos>): Set<Pos> {
      if (stepCount == 0) return result
      val steps = result.flatMap { it.neighbours() }.filter { it !in rocks && it.x in 0..<width && it.y in 0..< height }
      return findReachableGardenTiles(stepCount - 1, steps.toSet())
    }
    return findReachableGardenTiles(stepCount, setOf(start))
  }
}

private fun Int.squared() = this.toLong() * this.toLong()

internal fun parseGardenMap(lines: List<String>): GardenMap {
  val rocks = buildSet {
    lines.forEachIndexed { y, line ->
      line.forEachIndexed { x, c ->
        if (c == '#') add(Pos(x, y))
      }
    }
  }
  val startY = lines.indexOfFirst { line -> 'S' in line }
  val startX = lines[startY].indexOf('S')
  return GardenMap(lines.first().length, lines.size, rocks, Pos(startX, startY))
}
