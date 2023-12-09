package de.smartsteuer.frank.adventofcode2023.day03

import de.smartsteuer.frank.adventofcode2023.day03.Coordinate.Companion.c
import de.smartsteuer.frank.adventofcode2023.day03.Grid.Symbol
import de.smartsteuer.frank.adventofcode2023.day03.Grid.Number
import de.smartsteuer.frank.adventofcode2023.lines

fun main() {
  val grid = parseGrid(lines("/adventofcode2023/day03/grid.txt"))
  println("part 1: ${part1(grid)}")
  println("part 2: ${part2(grid)}")
}

internal fun part1(grid: Grid): Int {
  val symbolCoordinates = grid.symbols.map { it.cell }.toSet()
  return grid.numbers.filter { number ->
    number.cells.any { cell -> cell.neighbours().any { neighbour -> neighbour in symbolCoordinates } }
  }.sumOf { it.value }
}

internal fun part2(grid: Grid): Int {
  val gears = grid.symbols.filter { it.symbol == '*' }
  val adjacentNumbers = gears.map { gear ->
    gear.cell.neighbours().map { neighbour ->
      grid.numbers.filter { number -> neighbour in number.cells }
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

internal data class Grid(val numbers: Set<Number>, val symbols: Set<Symbol>) {
  data class Number(val value: Int, val cells: Set<Coordinate>)
  data class Symbol(val symbol: Char, val cell: Coordinate)
}

internal fun parseGrid(lines: List<String>): Grid {
  tailrec fun parseGrid(x: Int, y: Int, number: Number, numbers: Set<Number>, symbols: Set<Symbol>, digits: Boolean): Grid {
    val newNumbers = if (digits) numbers + number else numbers
    if (x >= lines.first().length) return parseGrid(0, y + 1, number, newNumbers, symbols, false)
    if (y >= lines.size) return Grid(newNumbers, symbols)
    return when {
      lines[y][x].isDigit() -> parseGrid(x + 1, y, Number((if (digits) number.value * 10 else 0) + lines[y][x].digitToInt(),
                                                          (if (digits) number.cells else emptySet()) + c(x, y)), numbers, symbols, true)
      lines[y][x] == '.'    -> parseGrid(x + 1, y, number, newNumbers, symbols, false)
      else                  -> parseGrid(x + 1, y, number, newNumbers, symbols + Symbol(lines[y][x], c(x, y)), false)
    }
  }
  return parseGrid(0, 0, Number(0, emptySet()), emptySet(), emptySet(), false)
}