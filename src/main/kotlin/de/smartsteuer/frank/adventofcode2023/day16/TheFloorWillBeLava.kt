package de.smartsteuer.frank.adventofcode2023.day16

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val layout = parseLayout(lines("/adventofcode2023/day16/layout.txt"))
  measureTime { println("part 1: ${part1(layout)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(layout)}") }.also { println("part 2 took $it") }
}

internal fun part1(layout: Layout): Int =
  layout.computeEnergizedTiles().size

internal fun part2(layout: Layout): Int =
  layout.findStartStates().maxOf { start -> layout.computeEnergizedTiles(start).size }

internal data class Pos(val x: Int, val y: Int) {
  operator fun plus(pos: Pos) = Pos(x + pos.x, y + pos.y)
}

internal data class BeamState(val pos: Pos, val direction: Pos) {
  fun next() = BeamState(pos + direction, direction)
  fun withDir(direction: Pos) = copy(direction = direction)
}

internal data class Layout(val width: Int, val height: Int, val tiles: Map<Pos, Char>) {
  fun computeEnergizedTiles(start: BeamState = BeamState(Pos(0, 0), Pos(1, 0))): Set<Pos> {
    tailrec fun computeBeamStates(state: BeamState, startStates: List<BeamState>, result: MutableSet<BeamState>): Set<BeamState> {
      val tile = tiles[state.pos]
      if (tile == null || state in result) {
        if (startStates.isEmpty()) return result
        return computeBeamStates(startStates.first(), startStates.drop(1), result)
      }
      val tileDoesNotInterfere = tile == '.' ||
          (tile == '-' && state.direction.y == 0) ||
          (tile == '|' && state.direction.x == 0)
      return when {
        tileDoesNotInterfere -> computeBeamStates(state.next(), startStates, result.apply { add(state) })
        tile == '-'          -> computeBeamStates(state.withDir(Pos(-1, 0)), startStates + state.withDir(Pos(1, 0)), result.apply { add(state) })
        tile == '|'          -> computeBeamStates(state.withDir(Pos(0, -1)), startStates + state.withDir(Pos(0, 1)), result.apply { add(state) })
        tile == '\\'         -> computeBeamStates(state.withDir(Pos( state.direction.y,  state.direction.x)).next(), startStates, result.apply { add(state) })
        tile == '/'          -> computeBeamStates(state.withDir(Pos(-state.direction.y, -state.direction.x)).next(), startStates, result.apply { add(state) })
        else                 -> throw IllegalStateException("unexpected tile: '$tile'")
      }
    }
    return computeBeamStates(start, mutableListOf(), mutableSetOf()).map { it.pos }.toSet()
  }

  fun findStartStates(): List<BeamState> =
    (0..<width).map  { x -> BeamState(Pos(x,         0),          Pos( 0,  1)) } +
    (0..<width).map  { x -> BeamState(Pos(x,         height - 1), Pos( 0, -1)) } +
    (0..<height).map { y -> BeamState(Pos(0,         y),          Pos( 1,  0)) } +
    (0..<height).map { y -> BeamState(Pos(width - 1, y),          Pos(-1,  0)) }
}

internal fun parseLayout(input: List<String>): Layout =
  Layout(input.first().length, input.size, buildMap {
    input.forEachIndexed { y, line ->
      line.forEachIndexed { x, c ->
        this[Pos(x, y)] = c
      }
    }
  })