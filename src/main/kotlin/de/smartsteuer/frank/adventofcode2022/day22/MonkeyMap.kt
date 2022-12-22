package de.smartsteuer.frank.adventofcode2022.day22

import de.smartsteuer.frank.adventofcode2022.day22.Day22.Action.*
import de.smartsteuer.frank.adventofcode2022.day22.Day22.part1
import de.smartsteuer.frank.adventofcode2022.day22.Day22.part2
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.system.measureTimeMillis

fun main() {
  val input = lines("/adventofcode2022/day22/monkey-map.txt")
  measureTimeMillis {
    println("day 22, part 1: ${part1(input)}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 22, part 2: ${part2(input)}")
  }.also { println("took $it ms") }
}

object Day22 {
  fun part1(input: List<String>): Int {
    val (map, path) = parseInput(input)
    val finalState = path.process(map)
    return finalState.password()
  }

  fun part2(input: List<String>): Long {
    return 0
  }

  data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
  }

  enum class Direction(val delta: Coordinate) {
    RIGHT(Coordinate( 1,  0)),
    DOWN (Coordinate( 0,  1)),
    LEFT (Coordinate(-1,  0)),
    UP   (Coordinate( 0, -1));

    fun turnLeft()  = values()[(ordinal - 1 + values().size) % values().size]

    fun turnRight() = values()[(ordinal + 1) % values().size]
  }

  data class Path(val actions: List<Action>) {
    fun process(map: GroveMap): State = actions.fold(State(map.start, Direction.RIGHT)) { state, action -> state.apply(action, map) }
  }

  data class State(val position: Coordinate, val direction: Direction) {
    constructor(position: Pair<Int, Int>, direction: Direction): this(Coordinate(position.first, position.second), direction)

    fun password() = 1_000 * (position.y + 1) + 4 * (position.x + 1) + direction.ordinal

    fun apply(action: Action, map: GroveMap): State =
      when (action) {
        is TurnLeft  -> State(position, direction.turnLeft())
        is TurnRight -> State(position, direction.turnRight())
        is Forward   -> State((1..action.steps).fold(position) { currentPosition, _ ->
          val nextPosition = map.nextTile(State(currentPosition, direction))
          if (map.isWall(nextPosition)) currentPosition else nextPosition
        }, direction)
      }
  }

  sealed interface Action {
    data class Forward(val steps: Int): Action
    object TurnLeft:  Action
    object TurnRight: Action
  }

  data class GroveMap(val openTiles: Set<Coordinate>, val walls: Set<Coordinate>) {
    private val allTiles = openTiles + walls
    private val xRange   = allTiles.minOf { it.x }..allTiles.maxOf { it.x }
    private val yRange   = allTiles.minOf { it.y }..allTiles.maxOf { it.y }
    private val xRanges  = yRange.map { y -> allTiles.filter { it.y == y }.let { tilesInRow -> tilesInRow.minOf { it.x }..tilesInRow.maxOf { it.x } } }
    private val yRanges  = xRange.map { x -> allTiles.filter { it.x == x }.let { tilesInCol -> tilesInCol.minOf { it.y }..tilesInCol.maxOf { it.y } } }

    val start = openTiles.filter { it.y == yRange.first }.minBy { it.x }

    fun nextTile(state: State): Coordinate {
      val newPosition = state.position + state.direction.delta
      if (newPosition in allTiles) return newPosition
      return if (state.direction.delta.x != 0) {
        val xRange = xRanges[newPosition.y]
        if (newPosition.x < xRange.first) Coordinate(xRange.last, newPosition.y) else Coordinate(xRange.first, newPosition.y)
      } else {
        val yRange = yRanges[newPosition.x]
        if (newPosition.y < yRange.first) Coordinate(newPosition.x, yRange.last) else Coordinate(newPosition.x, yRange.first)
      }
    }

    fun isWall(position: Coordinate) = position in walls

    override fun toString() = buildString {
      yRange.forEach { y ->
        val line = xRange.map { x ->
          when (Coordinate(x, y)) {
            in openTiles -> '.'
            in walls     -> '#'
            else         -> ' '
          }
        }.joinToString(separator = "").trimEnd()
        appendLine(line)
      }
    }
  }

  fun parseInput(input: List<String>): Pair<GroveMap, Path> = parseMap(input.dropLast(2)) to parsePath(input.last())

  private fun parseMap(input: List<String>): GroveMap {
    val tiles = input.flatMapIndexed { y, line ->
      line
        .withIndex()
        .filter { it.value in "#." }
        .map { (x, char) -> Coordinate(x, y) to (char == '#') }
    }
    val (walls, openTiles) = tiles.partition { (_, isWall) -> isWall }.toList().map { it.map { (coordinate, _) -> coordinate }.toSet() }
    return GroveMap(openTiles, walls)
  }

  private fun parsePath(input: String): Path {
    tailrec fun parsePath(input: String, actions: List<Action>): List<Action> {
      if (input.isEmpty()) return actions
      return when {
        input[0].isDigit() -> parsePath(input.dropWhile { it.isDigit() }, actions + Forward(input.takeWhile { it.isDigit() }.toInt()))
        input[0] == 'L'    -> parsePath(input.drop(1),                    actions + TurnLeft)
        input[0] == 'R'    -> parsePath(input.drop(1),                    actions + TurnRight)
        else               -> throw IllegalArgumentException("input contains illegal character: '${input[0]}'")
      }
    }
    return Path(parsePath(input, emptyList()))
  }
}