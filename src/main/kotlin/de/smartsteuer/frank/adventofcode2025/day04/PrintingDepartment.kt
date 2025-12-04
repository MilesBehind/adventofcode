package de.smartsteuer.frank.adventofcode2025.day04

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  PrintingDepartment.execute(lines("/adventofcode2025/day04/paper-rolls.txt"))
}

object PrintingDepartment: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parsePaperRolls().findAccessiblePaperRolls().size.toLong()

  override fun part2(input: List<String>): Long =
    input.parsePaperRolls().findRemovablePaperRolls().size.toLong()
}

data class PaperRolls(val positions: Set<Coordinate>) {
  fun findAccessiblePaperRolls(): List<Coordinate> =
    positions.filter { position ->
      position.neighbours().count { it in positions } < 4
    }

  fun findRemovablePaperRolls(): List<Coordinate> {
    tailrec fun findRemovablePaperRolls(paperRolls: PaperRolls, result: List<Coordinate>): List<Coordinate> {
      val accessiblePaperRolls = paperRolls.findAccessiblePaperRolls()
      if (accessiblePaperRolls.isEmpty()) return result
      return findRemovablePaperRolls(PaperRolls(paperRolls.positions - accessiblePaperRolls.toSet()), result + accessiblePaperRolls)
    }
    return findRemovablePaperRolls(this, emptyList())
  }
}

data class Coordinate(val x: Int, val y: Int) {
  companion object {
    val neighbours = listOf(Coordinate( 0, -1), Coordinate( 0, 1), Coordinate(-1,  0), Coordinate(1, 0),
                            Coordinate(-1, -1), Coordinate(-1, 1), Coordinate( 1, -1), Coordinate(1, 1))
    fun c(x: Int, y: Int) = Coordinate(x, y)
  }
  operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)

  fun neighbours(): List<Coordinate> = neighbours.map { this + it }
}

internal fun List<String>.parsePaperRolls(): PaperRolls =
  PaperRolls(flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, char -> if (char == '@') Coordinate(x, y) else null }
  }.toSet())
