package de.smartsteuer.frank.adventofcode2024.day01

import de.smartsteuer.frank.adventofcode2024.lines
import kotlin.math.abs

fun main() {
  val locations: List<List<Int>> = lines("/adventofcode2024/day01/location-IDs.txt").parseLocations()
  println("part 1: ${part1a(locations)}")
  println("part 2: ${part2a(locations)}")
}

internal fun part1a(locations: List<List<Int>>): Int =
  locations.transpose().map { it.sorted() }.let { (a, b) -> a zip b }.sumOf { (a, b) -> abs(a - b) }

internal fun part2a(locations: List<List<Int>>): Int =
  locations.transpose().let { (a, b) -> a.sumOf { number -> number * b.count { it == number } } }

internal fun <T> List<List<T>>.transpose(): List<List<T>> =
  if (this.isEmpty()) emptyList() else first().indices.map { column -> this.map { it[column] } }
