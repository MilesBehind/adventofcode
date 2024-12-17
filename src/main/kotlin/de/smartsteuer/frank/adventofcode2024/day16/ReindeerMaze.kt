package de.smartsteuer.frank.adventofcode2024.day16

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  ReindeerMaze.execute(lines("/adventofcode2024/day16/maze.txt"))
}

object ReindeerMaze: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseMaze().findShortestPath().toLong()

  override fun part2(input: List<String>): Long {
    TODO("Not yet implemented")
  }

  data class Pos(val x: Int, val y: Int) {
    fun neighbours() = listOf(Pos(x - 1, y), Pos(x, y - 1), Pos(x + 1, y), Pos(x, y + 1))
    operator fun plus(direction: Direction) = Pos(x + direction.x, y + direction.y)
    operator fun minus(pos: Pos) = Direction(x - pos.x, y - pos.y)
  }

  data class Direction(val x: Int, val y: Int) {
    fun turnRight() = Direction(-y,  x)
    fun turnLeft()  = Direction( y, -x)
  }

  data class State(val pos: Pos, val direction: Direction, val score: Int) {
    fun findPossibleNextStates(walls: Set<Pos>): List<State> {
      val result = mutableListOf<State>()
      if (pos + direction             !in walls) result += State(pos + direction, direction, score + 1)
      if (pos + direction.turnRight() !in walls) result += State(pos, direction.turnRight(), score + 1_000)
      if (pos + direction.turnLeft()  !in walls) result += State(pos, direction.turnLeft(),  score + 1_000)
      return result
    }

    //override fun toString(): String = "(${pos.x},${pos.y}), facing (${direction.x},${direction.y}), score $score"
  }

  data class Maze(val walls: Set<Pos>, val width: Int, val height: Int, val start: Pos, val end: Pos) {
    fun findShortestPath(): Int {
      tailrec fun findShortestPath(endStates: List<State>, nonEndStates: List<State>, previousStates: Set<State>): Int {
        val (newEndStates, newNonEndStates) = nonEndStates.partition { it.pos == end }
        //println("end: ${endStates.size + newEndStates.size}, non end: ${newNonEndStates.size}, previous: ${previousStates.size}")
        if (newNonEndStates.isEmpty()) return (endStates + newEndStates).minOf { it.score } //.also { println((endStates + newEndStates).filter { state -> state.score == it }) }
        val possibleNextStates = newNonEndStates.flatMap { state -> state.findPossibleNextStates(walls) }
        val nextStates = possibleNextStates.filter { state ->
          previousStates.none { it.pos == state.pos && it.direction == state.direction && it.score < state.score }
        }
        return findShortestPath(endStates + newEndStates, nextStates, previousStates + nextStates)
      }
      return findShortestPath(emptyList(), listOf(State(start, Direction(1, 0), 0)), setOf())
    }
  }

  private fun List<String>.parseMaze(): Maze {
    val positions = this.flatMapIndexed { y, line ->
      line.mapIndexed { x, char ->
        Pos(x, y) to char
      }
    }
    return Maze(walls  = positions.filter { it.second == '#' }.map { it.first }.toSet(),
                width  = first().length,
                height = size,
                start  = positions.first { it.second == 'S' }.first,
                end    = positions.first { it.second == 'E' }.first)
  }
}