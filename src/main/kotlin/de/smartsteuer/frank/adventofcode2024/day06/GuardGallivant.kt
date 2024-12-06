package de.smartsteuer.frank.adventofcode2024.day06

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  GuardGallivant.execute(lines("/adventofcode2024/day06/map.txt"))
}

object GuardGallivant: Day {

  override fun part1(input: List<String>): Long {
    val (map, guardState) = input.parseMap()
    return findGuardPositions(map, guardState).size.toLong()
  }

  override fun part2(input: List<String>): Long {
    val (map, guard) = input.parseMap()
    val candidatesForNewObstacle = findGuardPositions(map, guard) - guard.pos
    return candidatesForNewObstacle.filter { candidate ->
      guardIsStuckInLoop(map.copy(obstacles = map.obstacles + (candidate to true)), guard)
    }.size.toLong()
  }

  private fun findGuardPositions(map: GuardMap, guardState: GuardState): Set<Pos> {
    tailrec fun findGuardPositions(guardState: GuardState, guardPositions: MutableSet<Pos>): Set<Pos> {
      if (guardState.pos !in map.obstacles) return guardPositions
      return findGuardPositions(guardState.nextGuardState(map), guardPositions.apply { add(guardState.pos) })
    }
    return findGuardPositions(guardState, mutableSetOf())
  }

  private fun guardIsStuckInLoop(map: GuardMap, guardState: GuardState): Boolean {
    tailrec fun guardIsStuckInLoop(guardState: GuardState, guardStates: MutableSet<GuardState>): Boolean {
      if (guardState.pos !in map.obstacles) return false
      val nextGuardState = guardState.nextGuardState(map)
      if (nextGuardState in guardStates) return true
      return guardIsStuckInLoop(nextGuardState, guardStates.apply { add(guardState) })
    }
    return guardIsStuckInLoop(guardState, mutableSetOf())
  }

  data class Pos(val x: Int, val y: Int) {
    operator fun plus(direction: Direction): Pos = Pos(x + direction.dx, y + direction.dy)
  }

  data class Direction(val dx: Int, val dy: Int) {
    fun turnRight() = Direction(-dy, dx)
  }

  data class GuardState(val pos: Pos, val direction: Direction) {
    fun nextGuardState(map: GuardMap) =
      (pos + direction).let { nextPos ->
        if (map.obstacles[nextPos] == true) GuardState(pos, direction.turnRight()) else GuardState(nextPos, direction)
      }
  }

  data class GuardMap(val obstacles: Map<Pos, Boolean>, val width: Int, val height: Int)

  fun List<String>.parseMap(): Pair<GuardMap, GuardState> {
    val width  = this.first().length
    val height = this.size
    val mapPositions = this.flatMapIndexed { y, line ->
      line.mapIndexed { x, c ->
        Pos(x, y) to c
      }
    }
    val obstacles: Map<Pos, Boolean> = mapPositions.toMap().mapValues { it.value == '#' }
    val (guardPos, directionChar) = mapPositions.first { (_, c) -> c in setOf('<', '^', '>', 'v') }
    val direction = when (directionChar) {
      '<'  -> Direction(-1,  0)
      '>'  -> Direction( 1,  0)
      '^'  -> Direction( 0, -1)
      else -> Direction( 0,  1)
    }
    return GuardMap(obstacles, width, height) to GuardState(guardPos, direction)
  }
}