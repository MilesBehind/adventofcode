package de.smartsteuer.frank.adventofcode2023.day12

import de.smartsteuer.frank.adventofcode2023.day12.SpringMap.*
import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val springMap = parseSpringMap(lines("/adventofcode2023/day12/spring-map.txt"))
  measureTime { println("part 1: ${part1(springMap)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(springMap)}") }.also { println("part 2 took $it") }
}

internal fun part1(springMap: SpringMap): Long =
  springMap.rows.sumOf { it.countSolutions() }

internal fun part2(springMap: SpringMap): Long =
  springMap.rows.sumOf { it.unfold().countSolutions() }

internal data class SpringMap(val rows: List<Row>) {
  data class Row(val tiles: String, val records: List<Int>) {
    private val cachedResults = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun countSolutions(tiles: String = this.tiles, records: List<Int> = this.records): Long {
      if (tiles.isEmpty()) return (if (records.isEmpty()) 1 else 0)
      if (records.isEmpty()) return (if ('#' in tiles) 0 else 1)
      return cachedResults.getOrPut(tiles to records) {
        (if (tiles.first() in ".?") tryOperationalTile(tiles, records) else 0) +
        (if (tiles.first() in "#?") tryDamagedTile    (tiles, records) else 0)
      }
    }

    private fun tryOperationalTile(tiles: String, records: List<Int>): Long =
      countSolutions(tiles.drop(1), records)

    private fun tryDamagedTile(tiles: String, records: List<Int>): Long {
      val record                   = records.first()
      val recordFits               = tiles.length >= record
      val recordIsNotFinishedEarly = '.' !in tiles.take(record)
      val tilesEndWithRecord       = tiles.length == record
      val recordIsTerminated       = tilesEndWithRecord || (recordFits && tiles[record] != '#')
      return if (recordIsNotFinishedEarly && recordIsTerminated) countSolutions(tiles.drop(record + 1), records.drop(1)) else 0
    }

    fun unfold() = Row(List(5) { tiles }.joinToString(separator = "?"),
                       List(5) { records }.flatten())
  }
}

internal fun parseSpringMap(lines: List<String>): SpringMap =
  SpringMap(lines.map { line ->
    val (tiles, records) = line.split(" ")
    Row(tiles, records.split(",").map { it.toInt() })
  })
