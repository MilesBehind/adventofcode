package de.smartsteuer.frank.adventofcode2025.day09

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines
import kotlin.math.max
import kotlin.math.min

fun main() {
  MovieTheater.execute(lines("/adventofcode2025/day09/red-tile-locations.txt"))
}

object MovieTheater: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseTheaterFloor().findLargestRectangle()

  override fun part2(input: List<String>): Long =
    input.parseTheaterFloor().findLargestRectangleOfRedOrGreenTiles()
}

internal data class TheaterFloor(val redTiles: List<Coordinate>) {
  fun findLargestRectangle(): Long =
    redTiles.flatMapIndexed { index, corner1 ->
      redTiles.drop(index + 1).map { corner2 ->
        Rectangle(corner1, corner2).area
      }
    }.maxOf { it }

  fun findLargestRectangleOfRedOrGreenTiles(): Long {
    val lines = (redTiles + redTiles.first()).windowed(2).map { (corner1, corner2) -> Line(corner1, corner2) }
    return redTiles.flatMapIndexed { index, corner1 ->
      redTiles.drop(index + 1).map { corner2 ->
        val rectangle = Rectangle(corner1, corner2)
        if (rectangle.inner().let { inner -> lines.any { line -> inner crosses line } }) 0 else rectangle.area
      }
    }.maxOf { it }
  }
}

internal class Coordinate(val x: Int, val y: Int)

internal class Line(start: Coordinate, end: Coordinate) {
  val isHorizontal: Boolean = start.y == end.y
  val start = if ((isHorizontal && start.x <= end.x) || !isHorizontal && start.y <= end.y) start else end
  val end   = if ((isHorizontal && start.x <= end.x) || !isHorizontal && start.y <= end.y) end   else start

  infix fun crosses(other: Line): Boolean {
    if (isHorizontal == other.isHorizontal) return false
    return if (isHorizontal) other.start.x in start.x..end.x             && start.y       in other.start.y..other.end.y
    else                     start.x       in other.start.x..other.end.x && other.start.y in start.y..end.y
  }
}

internal class Rectangle(corner1: Coordinate, corner2: Coordinate) {
  val topLeft     = Coordinate(min(corner1.x, corner2.x), min(corner1.y, corner2.y))
  val bottomRight = Coordinate(max(corner1.x, corner2.x), max(corner1.y, corner2.y))
  val borders: List<Line> =
    if (topLeft.x <= bottomRight.x && topLeft.y <= bottomRight.y)
      listOf(
        Line(corner1, Coordinate(corner2.x, corner1.y)),
        Line(corner1, Coordinate(corner1.x, corner2.y)),
        Line(corner2, Coordinate(corner2.x, corner1.y)),
        Line(corner2, Coordinate(corner1.x, corner2.y)),
      )
    else emptyList()

  fun inner(): Rectangle =
    if (topLeft.x < bottomRight.x - 1 && topLeft.y < bottomRight.y - 1) Rectangle(Coordinate(topLeft.x     + 1, topLeft.y     + 1),
                                                                                  Coordinate(bottomRight.x - 1, bottomRight.y - 1))
    else Rectangle(Coordinate(0, 0), Coordinate(0, 0))  // effectively empty rectangle

  infix fun crosses(line: Line): Boolean =
    borders.any { border -> border crosses line }

  val area: Long get() =
    (bottomRight.x - topLeft.x + 1).toLong() * (bottomRight.y - topLeft.y + 1).toLong()
}

internal fun List<String>.parseTheaterFloor(): TheaterFloor =
  TheaterFloor(map { line ->
    val (x, y) = line.split(",").map { it.toInt() }
    Coordinate(x, y)
  })

