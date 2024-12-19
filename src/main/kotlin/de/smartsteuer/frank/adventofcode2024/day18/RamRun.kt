package de.smartsteuer.frank.adventofcode2024.day18

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  RamRun.execute(lines("/adventofcode2024/day18/byte-coordinates.txt"))
}

object RamRun: Day<String> {
  override fun part1(input: List<String>): String =
    input.parseGrid().let { grid ->
      grid.findPath(nanosecondsStart = if (grid.size < 10) 12 else 1024)
    }.toString()

  override fun part2(input: List<String>): String =
    input.parseGrid().let { grid ->
      val minimum = if (grid.size < 10) 12 else 1024
      val maximum = grid.obstacles.size - 1
      val nanoSecondsWidthNoPath = (0..maximum).zipWithNext().binarySearch(minimum) { (nanoseconds1, nanoseconds2) ->
        val result1 = grid.findPath(nanosecondsStart = nanoseconds1)
        val result2 = grid.findPath(nanosecondsStart = nanoseconds2)
        when {
          result1 != null && result2 != null -> -1
          result1 != null && result2 == null ->  0
          else                               ->  1
        }
      }
      grid.obstacles.drop(nanoSecondsWidthNoPath).first()
    }.let { pos -> "${pos.x},${pos.y}" }

  data class Pos(val x: Int, val y: Int) {
    fun neighbours() = listOf(Pos(x - 1, y), Pos(x, y - 1), Pos(x + 1, y), Pos(x, y + 1))
  }

  data class Grid(val size: Int, val obstacles: Set<Pos>) {
    fun findPath(nanosecondsStart: Int = 0): Int? {
      tailrec fun findPath(positions: Set<Pos>, visited: MutableSet<Pos>, result: Int): Int? {
        //println("result: $result, grid: \n${this.toString(nanosecondsStart, positions, visited)}")
        val nextPositions: Set<Pos> = positions.flatMap { pos ->
          if (pos.atEnd()) return result
          if (pos.corrupted(nanosecondsStart)) emptyList<Pos>()
          pos.next(nanosecondsStart).filter { it !in visited }
        }.toSet()
        if (nextPositions.isEmpty()) return null
        return findPath(nextPositions, visited.apply { addAll(nextPositions) }, result + 1)
      }
      return findPath(setOf(Pos(0, 0)), mutableSetOf(Pos(0, 0)), 0)
    }

    //fun toString(nanoseconds: Int, positions: Set<Pos>, visited: Set<Pos>) = buildString {
    //  (0 until size).forEach { y ->
    //    (0 until size).forEach { x ->
    //      val pos = Pos(x, y)
    //      append (when {
    //                pos in positions           -> '✶'
    //                pos.corrupted(nanoseconds) -> '#'
    //                pos in visited             -> 'O'
    //                else                       -> '.'
    //      })
    //      append("")
    //    }
    //    appendLine()
    //  }
    //}

    private fun Pos.atEnd() = x == size - 1 && y == size - 1
    private fun Pos.corrupted(nanoseconds: Int): Boolean = this in obstacles.take(nanoseconds)
    private fun Pos.inGrid() = this.x in 0 until size && this.y in 0 until size
    private fun Pos.next(nanoseconds: Int) = this.neighbours().filter { it.inGrid() && !it.corrupted(nanoseconds) }
  }

  private fun List<String>.parseGrid() =
    Grid(if (this.size > 50) 71 else 7, this.map { line ->
      val (x, y) = line.split(",").map { it.toInt() }
      Pos(x, y)
    }.toSet())
}