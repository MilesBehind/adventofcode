package de.smartsteuer.frank.adventofcode2023.day11

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
  val image = parseImage(lines("/adventofcode2023/day11/image.txt"))
  measureTime { println("part 1: ${part1(image)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(image)}") }.also { println("part 2 took $it") }
}

internal fun part1(image: Image): Int =
  image.expand().distances().sum()

internal fun part2(image: Image): Long =
  image.expand(1_000_000).distances().sumOf { it.toLong() }

internal data class Image(val width: Int, val height: Int, val galaxies: Set<Pos>) {
  fun findEmptyColumns(): List<Int> = (0..<width).filter  { x -> galaxies.none { it.x == x } }
  fun findEmptyRows():    List<Int> = (0..<height).filter { y -> galaxies.none { it.y == y } }

  fun expand(expansion: Int = 2): Image {
    val emptyColumns = findEmptyColumns()
    val emptyRows    = findEmptyRows()
    return Image(width + emptyColumns.size, height + emptyRows.size, galaxies.map { galaxy ->
      Pos(emptyColumns.count { it < galaxy.x } * (expansion - 1) + galaxy.x,
          emptyRows.count    { it < galaxy.y } * (expansion - 1) + galaxy.y)
    }.toSet())
  }

  fun distances(): List<Int> =
    galaxies.toList().uniquePairs().map { (galaxy1, galaxy2) ->
      abs(galaxy2.x - galaxy1.x) + abs(galaxy2.y - galaxy1.y)
    }
}

fun <T> List<T>.pairs(): List<Pair<T, T>> = flatMap { first -> map { second -> Pair(first, second) } }

fun <T> List<T>.uniquePairs(): List<Pair<T, T>> =
  pairs()
    .filter { (first, second) -> first != second }
    .map { setOf(it.first, it.second) }
    .toSet()
    .map { Pair(it.first(), it.last()) }

internal data class Pos(val x: Int, val y: Int)

internal fun parseImage(lines: List<String>): Image =
  Image(lines.first().length, lines.size, lines.flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, c ->
      if (c == '#') Pos(x, y) else null
    }
  }.toSet())