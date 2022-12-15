package de.smartsteuer.frank.adventofcode2022.day15

import de.smartsteuer.frank.adventofcode2022.clamp
import de.smartsteuer.frank.adventofcode2022.day04.size
import de.smartsteuer.frank.adventofcode2022.day15.Day15.part1
import de.smartsteuer.frank.adventofcode2022.day15.Day15.part2
import de.smartsteuer.frank.adventofcode2022.extractNumbers
import de.smartsteuer.frank.adventofcode2022.lines
import de.smartsteuer.frank.adventofcode2022.merge
import kotlin.math.abs


fun main() {
  val input = lines("/adventofcode2022/day15/sensors-and-beacons.txt")
  println("day 15, part 1: ${part1(input, 2_000_000)}")
  println("day 15, part 2: ${part2(input, 0..4_000_000)}")
}

object Day15 {
  fun part1(input: List<String>, y: Int, printCells: Boolean = false): Int {
    val space = Space.parse(input)
    if (printCells) {
      println("$space\n${space.computeCoverageForEachCell()}")
    }
    return space.countNonBeaconCells(y)
  }

  fun part2(input: List<String>, acceptedRange: IntRange, printCells: Boolean = false): Long {
    val space = Space.parse(input)
    if (printCells) {
      println("$space\n${space.computeCoverageForEachCell()}")
    }
    val rowWithAtLeastTwoIntervals: Pair<Int, List<IntRange>> = acceptedRange.map { y ->
      y to space.computeCoverage(y)
            .map { interval -> interval.clamp(acceptedRange) }
            .filter { !it.isEmpty() }
    }.find { (_, intervals) -> intervals.size >= 2 } ?: throw IllegalStateException("empty intervals")
    val y = rowWithAtLeastTwoIntervals.first
    val x = rowWithAtLeastTwoIntervals.second.first().last + 1
    return x * 4_000_000L + y
  }

  data class Space(val sensors: List<Pair<Coordinate, Coordinate>> = emptyList(), val coverage: Set<Coordinate> = emptySet()) {
    data class Coordinate(val x: Int, val y: Int)

    fun computeCoverage(y: Int): List<IntRange> =
      sensors.fold(emptyList()) { intervals, (sensor, beacon) ->
        val sensorCoverage = sensorCoverage(sensor, beacon, y)
        if (sensorCoverage.isEmpty()) intervals else intervals.merge(sensorCoverage)
      }

    fun countNonBeaconCells(y: Int): Int {
      val intervals = computeCoverage(y)
      val covered = intervals.sumOf { it.size }
      val beacons = sensors
        .map { it.second }
        .toSet()
        .count { beacon -> beacon.y == y && intervals.any { interval -> beacon.x in interval } }
      return covered - beacons
    }

    fun computeCoverageForEachCell(): Space {
      fun computeSensorCoverage(sensor: Coordinate, beacon: Coordinate): Set<Coordinate> {
        val radius = abs(sensor.x - beacon.x) + abs(sensor.y - beacon.y)
        val deltaRange = (-radius)..radius
        return deltaRange
          .flatMap { y -> deltaRange.map { x -> Coordinate(x, y) }
          .filter { (x, y) -> abs(x) + abs(y) <= radius }
        }.map { (x, y) -> Coordinate(x + sensor.x, y + sensor.y) }.toSet()
      }

      tailrec fun computeCoverage(sensors: List<Pair<Coordinate, Coordinate>>, coverage: Set<Coordinate>): Set<Coordinate> {
        if (sensors.isEmpty()) return coverage
        val (sensor, beacon) = sensors.first()
        return computeCoverage(sensors.drop(1), coverage + computeSensorCoverage(sensor, beacon))
      }
      return copy(coverage = computeCoverage(sensors, emptySet()))
    }

    override fun toString(): String {
      val cells = (sensors.map { it.first } + sensors.map { it.second } + coverage)
      val minX  = cells.minBy { it.x }.x
      val maxX  = cells.maxBy { it.x }.x
      val minY  = cells.minBy { it.y }.y
      val maxY  = cells.maxBy { it.y }.y
      return buildString {
        (minY..maxY).forEach { y ->
          appendLine((minX..maxX).joinToString(separator = "") { x ->
            when {
              sensors.any { it.first  == Coordinate(x, y) } -> "S"
              sensors.any { it.second == Coordinate(x, y) } -> "B"
              Coordinate(x, y) in coverage                  -> "#"
              else                                          -> "."
            }
          })
        }
      }
    }

    companion object {
      private val regex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
      fun parse(input: List<String>): Space =
        input.asSequence().extractNumbers(regex).map { (sensorX, sensorY, beaconX, beaconY) ->
          Pair(Coordinate(sensorX, sensorY), Coordinate(beaconX, beaconY))
        }.fold(Space()) { space, (sensor, beacon) -> Space(space.sensors + (sensor to beacon)) }

      fun sensorCoverage(sensor: Coordinate, beacon: Coordinate, y: Int): IntRange {
        val radius = abs(sensor.x - beacon.x) + abs(sensor.y - beacon.y)
        if (y < sensor.y - radius || y > sensor.y + radius) return IntRange.EMPTY
        val relativeY = y - sensor.y
        val deltaX    = radius - abs(relativeY)
        return (sensor.x - deltaX)..(sensor.x + deltaX)
      }
    }
  }
}
