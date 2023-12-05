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
  val locationRanges: List<LongRange> = almanac.allLocations().sortedBy { it.first }
  val seedRanges = almanac.seeds.chunked(2).map { it.first()..<it.first() + it.last() }
  locationRanges.forEach { locationRange ->
    println("testing location range $locationRange")
    locationRange.forEach { location ->
      val seed = almanac.findSeed(location)
      if (seedRanges.any { seed in it }) {
        return location
      }
    }
  }
  throw IllegalStateException("could not find minimum location")
}

internal data class Almanac(val seeds: List<Long>, private val mappings: List<Mapping>) {
  internal fun findMappingBySource(sourceName: String) = mappings.find { it.from == sourceName } ?: throw IllegalArgumentException("could not find mapping for source $sourceName")
  internal fun findMappingByDestination(destinationName: String) = mappings.find { it.to == destinationName } ?: throw IllegalArgumentException("could not find mapping for destination $destinationName")

  fun findLocation(seed: Long): Long {
    tailrec fun findLocation(source: Long, sourceName: String, result: Long): Long {
      if (sourceName == "location") return result
      val mapping = findMappingBySource(sourceName)
      return findLocation(mapping.getDestination(source), mapping.to, mapping.getDestination(source))
    }
    return findLocation(seed, "seed", 0)
  }

  fun findSeed(location: Long): Long {
    tailrec fun findSeed(destination: Long, destinationName: String, result: Long): Long {
      if (destinationName == "seed") return result
      val mapping = findMappingByDestination(destinationName)
      return findSeed(mapping.getSource(destination), mapping.from, mapping.getSource(destination))
    }
    return findSeed(location, "location", 0)
  }

  fun allLocations(): List<LongRange> {
    val ranges: List<LongRange> = findMappingByDestination("location").ranges.map { range -> range.destinationStart..<range.destinationStart + range.length }
    val minimumLocation = ranges.minOf { it.first }
    return if (minimumLocation > 0) ranges + listOf(0L..<minimumLocation) else ranges
  }
}

internal data class Mapping(val from: String, val to: String, val ranges: List<Range>) {
  fun getDestination(source: Long): Long =
    ranges.find { it.containsSource(source) }?.getDestination(source) ?: source
  fun getSource(destination: Long): Long =
    ranges.find { it.containsDestination(destination) }?.getSource(destination) ?: destination
}

internal data class Range(val destinationStart: Long, val sourceStart: Long, val length: Long) {
  fun getDestination(source: Long): Long = destinationStart + (source - sourceStart)
  fun getSource(destination: Long): Long = sourceStart + (destination - destinationStart)
  fun containsSource(source: Long) = source >= sourceStart && source < sourceStart + length
  fun containsDestination(destination: Long) = destination >= destinationStart && destination < destinationStart + length
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