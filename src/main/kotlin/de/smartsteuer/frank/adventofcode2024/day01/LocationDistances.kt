package de.smartsteuer.frank.adventofcode2024.day01

import de.smartsteuer.frank.adventofcode2024.lines
import kotlin.math.abs

fun main() {
  val locations: List<List<Int>> = lines("/adventofcode2024/day01/location-IDs.txt").parseLocations()
  println("part 1: ${part1(locations)}")
  println("part 2: ${part2(locations)}")
}

internal fun part1(locations: List<List<Int>>): Int =
  (locations.map { it.first() }.sorted() zip locations.map { it.last()  }.sorted())
    .sumOf { abs(it.first - it.second) }

internal fun part2(locations: List<List<Int>>): Int =
  (locations.map { it.first() } to locations.map { it.last() }).let { (a, b) ->
    a.sumOf { number -> number * b.count { it == number } }
  }

internal fun List<String>.parseLocations(): List<List<Int>> =
  map { line -> line.split("""\s+""".toRegex()).map { it.toInt() } }

