package de.smartsteuer.frank.adventofcode2023.day05

import de.smartsteuer.frank.adventofcode2023.lines

fun main() {
  val almanac = parseAlmanac(lines("/adventofcode2023/day05/almanac.txt"))
  println("part 1: ${part1(almanac)}")
  println("part 2: ${part2(almanac)}")
}

internal fun part1(almanac: Almanac): Long =
  almanac.seeds.minOf { seed -> almanac.findLocation(seed) }

internal fun part2(almanac: Almanac): Long {
  val seedRanges = almanac.seeds
    .chunked(2)
    .map { it.first()..<it.first() + it.last() }
  val rangeMinimumLocations = seedRanges
    .map { seedRange -> seedRange.minOf { seed -> almanac.findLocation(seed) }.also { println("$seedRange => $it") } }
  return rangeMinimumLocations.min()
}

internal data class Almanac(val seeds: List<Long>, private val mappings: List<Mapping>) {
  internal fun findMapping(sourceName: String) = mappings.find { it.from == sourceName } ?: throw IllegalArgumentException("could not find mapping for $sourceName")
  fun findLocation(seed: Long): Long {
    tailrec fun findLocation(source: Long, sourceName: String, result: Long): Long {
      if (sourceName == "location") return result
      val mapping = findMapping(sourceName)
      return findLocation(mapping[source], mapping.to, mapping[source])
    }
    return findLocation(seed, "seed", 0)
  }
}

internal data class Mapping(val from: String, val to: String, val ranges: List<Range>) {
  operator fun get(source: Long): Long =
    ranges.find { source in it }?.get(source) ?: source
}

internal data class Range(val destinationStart: Long, val sourceStart: Long, val length: Long) {
  operator fun get(source: Long): Long = destinationStart + (source - sourceStart)
  operator fun contains(source: Long) = source >= sourceStart && source < sourceStart + length
}

internal fun parseAlmanac(lines: List<String>): Almanac {
  val seeds = lines.first().drop("seeds: ".length).split(" ").map { it.trim().toLong() }
  val mappingIndexes = lines.indices.filter { lines[it].isNotEmpty() && !lines[it].first().isDigit() }.drop(1)
  val mappings = mappingIndexes.map { index ->
    val (from, to) = """(\w+)-to-(\w+) map:""".toRegex().matchEntire(lines[index])?.groupValues?.drop(1) ?: throw IllegalStateException("could not parse ${lines[index]}")
    val firstRangeIndex = index + 1
    val lastRangeIndex  = (lines.indices.drop(firstRangeIndex).firstOrNull { lines[it].isBlank() } ?: lines.size) - 1
    val ranges = (firstRangeIndex..lastRangeIndex).map { lines[it] }.map { line ->
      val (destinationStart, sourceStart, rangeLength) = line.split(" ").map { it.trim().toLong() }
      Range(destinationStart, sourceStart, rangeLength)
    }
    Mapping(from, to, ranges)
  }
  return Almanac(seeds, mappings)
}