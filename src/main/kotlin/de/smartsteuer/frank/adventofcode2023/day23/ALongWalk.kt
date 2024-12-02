package de.smartsteuer.frank.adventofcode2023.day23

import de.smartsteuer.frank.adventofcode2023.day23.TileType.*
import de.smartsteuer.frank.adventofcode2023.lines
import io.vavr.collection.HashSet as VavrSet
import kotlin.time.measureTime

fun main() {
  val map = parseTrailMap(lines("/adventofcode2023/day23/map.txt"))
  measureTime { println("part 1: ${part1(map)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(map)}") }.also { println("part 2 took $it") }
}

internal fun part1(map: TrailMap): Int =
  map.findLongestTrail()

internal fun part2(map: TrailMap): Int =
  map.convertSlopes().findLongestTrail()

internal data class Pos(val x: Int, val y: Int) {
  operator fun plus(pos: Pos) = Pos(x + pos.x, y + pos.y)
  override fun toString(): String = "$x/$y"
}

internal enum class TileType(val neighbours: List<Pos>) {
  PATH       (listOf(Pos( 0, -1), Pos(1, 0), Pos(0, 1), Pos(-1, 0))),
  FORREST    (emptyList()),
  SLOPE_NORTH(listOf(Pos( 0, -1))),
  SLOPE_EAST (listOf(Pos( 1,  0))),
  SLOPE_SOUTH(listOf(Pos( 0,  1))),
  SLOPE_WEST (listOf(Pos(-1,  0)))
}

internal data class TrailMap(val width: Int, val height: Int, val tiles: Map<Pos, TileType>) {
  val start = Pos((0..<width).first { x -> tiles[Pos(x, 0)]          == PATH }, 0)
  val goal  = Pos((0..<width).first { x -> tiles[Pos(x, height - 1)] == PATH }, height - 1)

  fun findLongestTrail(): Int {
    var count = 0
    tailrec fun findAllTrails(steps: List<Step>, stepsThatReachedGoal: List<Step>): List<Step> {
      // println(visualize(steps.map { it.pos }).joinToString(separator = "\n", postfix = "\n"))
      if (++count % 100 == 0) println("count = $count")
      val steps2 = if (count % 100 == 0) steps.filter { step -> step.pos !in steps.map { it.pos }.toSet() } else steps
      val nextSteps = steps.filter { !it.goal }.flatMap { step ->
        val reachableNeighbours: List<Pos> = tiles[step.pos]?.neighbours
          ?.map { neighbourDelta -> step.pos + neighbourDelta }
          ?.filter { neighbour ->
            val neighbourTileType = tiles[neighbour]
            neighbourTileType != null && neighbourTileType != FORREST && neighbour !in step.visited
          } ?: emptyList()
        reachableNeighbours.map { neighbour -> Step(neighbour, step, step.visited.add(neighbour), neighbour == goal) }
      }
      if (nextSteps.isEmpty()) return stepsThatReachedGoal  // last remaining path is found
      return findAllTrails(nextSteps, stepsThatReachedGoal + (nextSteps.filter { it.goal }))
    }
    val startStep = Step(start, null, VavrSet.of(start))
    return findAllTrails(listOf(startStep), emptyList())
      .filter { it.goal }.maxOfOrNull { it.countStepsToStart() } ?: 0
  }

  fun convertSlopes(): TrailMap =
    copy(tiles = tiles.mapValues { (_, tileType) ->
      if (tileType == FORREST || tileType == PATH) tileType else PATH
    })
}

internal data class Step(val pos: Pos, val previous: Step?, val visited: VavrSet<Pos>, val goal: Boolean = false) {
  fun countStepsToStart(): Int = (visited.size() - 1)
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    return visited == (other as Step).visited
  }
  override fun hashCode(): Int = visited.hashCode()
}

private const val ANSI_RED   = "\u001B[31m"
private const val ANSI_GREEN = "\u001B[32m"
private const val ANSI_RESET = "\u001B[0m"

private fun TrailMap.visualize(currentSteps: List<Pos>): List<String> =
  (0..<height).map { y ->
    buildString {
      (0..<height).forEach { x ->
        append(
          if (Pos(x, y) in currentSteps) "${ANSI_RED}o$ANSI_RESET"
          else when (this@visualize.tiles[Pos(x, y)]) {
            FORREST     -> "$ANSI_GREEN#$ANSI_RESET"
            SLOPE_NORTH -> '^'
            SLOPE_EAST  -> '>'
            SLOPE_SOUTH -> 'v'
            SLOPE_WEST  -> '<'
            else        -> '.'
          })
      }
    }
  }


internal fun parseTrailMap(lines: List<String>): TrailMap {
  val tiles = buildMap {
    lines.forEachIndexed { y, line ->
      line.forEachIndexed { x, c ->
        this[Pos(x, y)] = when (c) {
          '#'  -> FORREST
          '^'  -> SLOPE_NORTH
          '>'  -> SLOPE_EAST
          'v'  -> SLOPE_SOUTH
          '<'  -> SLOPE_WEST
          else -> PATH
        }
      }
    }
  }
  return TrailMap(lines.first().length, lines.size, tiles)
}
