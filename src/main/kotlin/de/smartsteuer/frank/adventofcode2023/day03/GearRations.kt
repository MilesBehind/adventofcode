package de.smartsteuer.frank.adventofcode2023.day03

import de.smartsteuer.frank.adventofcode2023.day03.Coordinate.Companion.c
import de.smartsteuer.frank.adventofcode2023.lines

fun main() {
  val grid = parseGrid(lines("/adventofcode2023/day03/grid.txt"))
  println("part 1: ${part1(grid)}")
  println("part 2: ${part2(grid)}")
}

internal fun part1(grid: Grid): Int {
  val numbers = grid.findNumbers()
  val symbolCoordinates = grid.findSymbols().map { it.cell }
  return numbers.filter { number ->
    number.cells.any { cell -> cell.neighbours().any { neighbour -> neighbour in symbolCoordinates } }
  }.sumOf { it.value }
}

internal fun part2(grid: Grid): Int {
  val numbers = grid.findNumbers()
  val gears = grid.findSymbols().filter { it.symbol == '*' }
  val adjacentNumbers = gears.map { gear ->
    gear.cell.neighbours().map { neighbour ->
      numbers.filter { number -> neighbour in number.cells }
    }.filter { it.isNotEmpty() }.toSet().flatten()
  }.filter { it.size == 2 }
  return adjacentNumbers.sumOf { it.first().value * it.last().value }
}

internal data class Coordinate(val x: Int, val y: Int) {
  companion object {
    fun c(x: Int, y: Int) = Coordinate(x, y)
  }
  fun neighbours() = listOf(
    c(x - 1, y - 1), c(x, y - 1), c(x + 1, y - 1),
    c(x - 1, y    ),              c(x + 1, y    ),
    c(x - 1, y + 1), c(x, y + 1), c(x + 1, y + 1),
  )
}

internal data class Grid(val cells: Map<Coordinate, Char>, val width: Int, val height: Int) {
  data class Number(val value: Int, val cells: Set<Coordinate>)
  data class Symbol(val symbol: Char, val cell: Coordinate)

  fun findNumbers(): List<Number> {
    val result = mutableListOf<Number>()
    (0..height).forEach { y ->
      var number: Number? = null
      (0..width).forEach { x ->
        val c = c(x, y)
        val digit = if (cells[c]?.isDigit() == true) cells[c]?.digitToInt()!! else -1
        if (digit >= 0) {
          number = if (number == null) Number(digit, setOf(c)) else Number(number!!.value * 10 + digit, number!!.cells + c)
        } else {
          if (number != null) result += number!!
          number = null
        }
      }
    }
    return result
  }

  fun findSymbols(): List<Symbol> =
    cells.filter { cells[it.key] != null && cells[it.key]?.isDigit() == false }.map { Symbol(it.value, it.key) }
}

internal fun parseGrid(lines: List<String>): Grid {
  val cells = buildMap {
    lines.mapIndexed { y, line ->
      line.mapIndexed { x, c ->
        if (c != '.') put(Coordinate(x, y), c)
      }
    }
  }
  return Grid(cells, lines.first().length, lines.size)
}