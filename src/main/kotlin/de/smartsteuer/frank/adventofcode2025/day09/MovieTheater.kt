package de.smartsteuer.frank.adventofcode2025.day09

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines
import kotlin.collections.forEach
import kotlin.math.abs
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

data class Coordinate(val x: Int, val y: Int) {
  override fun toString(): String = "($x, $y)"
}

class Line(start: Coordinate, end: Coordinate) {
  val isHorizontal: Boolean = start.y == end.y
  val start = if ((isHorizontal && start.x <= end.x) || !isHorizontal && start.y <= end.y) start else end
  val end   = if ((isHorizontal && start.x <= end.x) || !isHorizontal && start.y <= end.y) end   else start

  @Suppress("DuplicatedCode")
  infix fun crosses(other: Line): Boolean {
    if (isHorizontal == other.isHorizontal) return false
    return if (isHorizontal) other.start.x in start.x..end.x && start.y in other.start.y..other.end.y
    else start.x in other.start.x..other.end.x && other.start.y in start.y..end.y
  }

  fun render(paper: MutableList<StringBuilder>, mark: Char = 'X') {
    if (start.x < 0) return
    if (isHorizontal) {
      (start.x..end.x).forEach { x -> paper[start.y][x] = mark }
    } else {
      (start.y..end.y).forEach { y -> paper[y][start.x] = mark }
    }
  }

  override fun toString(): String = "$start -> $end"
}

class Rectangle(corner1: Coordinate, corner2: Coordinate) {
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
    if (topLeft.x < bottomRight.x - 1 && topLeft.y < bottomRight.y - 1) Rectangle(Coordinate(topLeft.x + 1, topLeft.y + 1),
                                                                                  Coordinate(bottomRight.x - 1, bottomRight.y - 1))
    else Rectangle(Coordinate(-10, -10), Coordinate(-10, -10))

  infix fun crosses(line: Line): Boolean =
    borders.any { border -> border crosses line }

  fun render(paper: MutableList<StringBuilder>) {
    borders.forEach { line -> line.render(paper, 'O') }
  }

  override fun toString(): String = "rect($topLeft -> $bottomRight)"
}

data class TheaterFloor(val redTiles: List<Coordinate>) {
  fun findLargestRectangle(): Long =
    redTiles.flatMapIndexed { index, corner1 ->
      redTiles.drop(index + 1).map { corner2 ->
        (abs(corner2.x - corner1.x) + 1).toLong() * (abs(corner2.y - corner1.y) + 1).toLong()
      }
    }.maxOf { it }

  fun findLargestRectangleOfRedOrGreenTiles(): Long {
    val lines = (redTiles + redTiles.first()).windowed(2).map { (corner1, corner2) -> Line(corner1, corner2) }
    return redTiles.flatMapIndexed { index, corner1 ->
      redTiles.drop(index + 1).map { corner2 ->
        val rectangle = Rectangle(corner1, corner2).inner()
        lines.forEach { line ->
          println("$rectangle and $line")
          val paper = paper().also { paper ->
            rectangle.render(paper)
            line.render(paper)
          }
          println(paper.joinToString("\n"))
          println(if (rectangle.inner() crosses line) "rectangle crosses line" else "no crossing")
          println()
        }
        val crossingLines = lines.filter { rectangle.inner() crosses it }
        crossingLines.forEach { line -> println("crosses $line") }
        println("-".repeat(160))
        if (lines.any { line -> rectangle crosses line }) 0
        else (abs(corner2.x - corner1.x) + 1).toLong() * (abs(corner2.y - corner1.y) + 1).toLong()
      }
    }.maxOf { it }
  }
}

internal fun List<String>.parseTheaterFloor(): TheaterFloor =
  TheaterFloor(map { line ->
    val (x, y) = line.split(",").map { it.toInt() }
    Coordinate(x, y)
  })

internal fun TheaterFloor.paper(): MutableList<StringBuilder> {
  val width  = redTiles.maxOf { it.x } + 1
  val height = redTiles.maxOf { it.y } + 1
  val paper  = MutableList(height) { StringBuilder(width).append(".".repeat(width)) }
  /*
  redTiles.forEach { (x, y) -> paper[y][x] = '#' }
  (redTiles + redTiles.first()).windowed(2).forEach { (corner1, corner2) ->
    if (corner1.x == corner2.x) {
      (if (corner2.y >= corner1.y) (corner1.y..corner2.y) else (corner2.y..corner1.y)).forEach { y -> paper[y][corner1.x] = 'X' }
    } else {
      (if (corner2.x >= corner1.x) (corner1.x..corner2.x) else (corner2.x..corner1.x)).forEach { x -> paper[corner1.y][x] = 'X' }
    }
    paper[corner1.y][corner1.x] = '#'
  }
  */
  return paper
}

