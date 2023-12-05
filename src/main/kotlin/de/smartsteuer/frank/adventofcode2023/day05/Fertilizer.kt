package de.smartsteuer.frank.adventofcode2023.day05

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val almanac = parseAlmanac(lines("/adventofcode2023/day05/almanac.txt"))
  measureTime { println("part 1: ${part1(almanac)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(almanac)}") }.also { println("part 2 took $it") }
}

internal fun part1(almanac: Almanac): Long =
  almanac.findMinimumLocationWhichMapsToSeed(almanac.seeds.map { it..it })

internal fun part2(almanac: Almanac): Long =
  almanac.findMinimumLocationWhichMapsToSeed(almanac.seeds.chunked(2).map { it.first()..<it.first() + it.last() })

internal data class Almanac(val seeds: List<Long>, private val mappings: List<Mapping>) {
  private fun findSeed(location: Long): Long = mappings.reversed().fold(location) { acc, mapping -> mapping[acc] }

  fun findMinimumLocationWhichMapsToSeed(seeds: List<LongRange>): Long =
    allLocations().mapNotNull { location ->
      findSeed(location).let { seed -> if (seeds.any { seed in it }) location else null }
    }.first()

  private fun allLocations(): Sequence<Long> {
    val ranges: List<LongRange> = mappings.last().ranges.map { range -> range.destinationRange }
    val minimumLocation = ranges.minOf { it.first }
    return (if (minimumLocation > 0) ranges + listOf(0L..<minimumLocation) else ranges).sortedBy { it.first }.asSequence().flatten()
  }
}

internal data class Mapping(val from: String, val to: String, val ranges: List<Range>) {
  operator fun get(destination: Long): Long = ranges.find { destination in it }?.get(destination) ?: destination
}

internal data class Range(val destinationStart: Long, val sourceStart: Long, val length: Long) {
  val destinationRange = destinationStart..<destinationStart + length
  operator fun get(destination: Long): Long = sourceStart + (destination - destinationStart)
  operator fun contains(destination: Long) = destination in destinationRange
}

internal fun parseAlmanac(lines: List<String>): Almanac {
  tailrec fun parseMappings(index: Int, ranges: List<Range>, mapping: Mapping, mappings: List<Mapping>): List<Mapping> {
    if (index >= lines.size) return mappings + mapping.copy(ranges = ranges)
    if (lines[index].isBlank()) return parseMappings(index + 1, emptyList(), mapping, mappings + mapping.copy(ranges = ranges))
    if (lines[index].first().isDigit()) {
      val (destinationStart, sourceStart, rangeLength) = lines[index].split(" ").map { it.trim().toLong() }
      return parseMappings(index + 1, ranges + Range(destinationStart, sourceStart, rangeLength), mapping, mappings)
    }
    val (from, to) = """(\w+)-to-(\w+) map:""".toRegex().matchEntire(lines[index])?.groupValues?.drop(1) ?: throw IllegalStateException("could not parse ${lines[index]}")
    return parseMappings(index + 1, emptyList(), Mapping(from, to, emptyList()), mappings)
  }
  val seeds = lines.first().drop("seeds: ".length).split(" ").map { it.trim().toLong() }
  return Almanac(seeds, parseMappings(2, emptyList(), Mapping("", "", emptyList()), emptyList()))
}