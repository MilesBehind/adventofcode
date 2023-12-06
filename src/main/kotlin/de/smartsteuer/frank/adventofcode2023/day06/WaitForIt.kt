package de.smartsteuer.frank.adventofcode2023.day06

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val times = parseTimes(lines("/adventofcode2023/day06/times.txt"))
  measureTime { println("part 1: ${part1(times)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(times)}") }.also { println("part 2 took $it") }
}

internal fun part1(times: Times): Long =
  times.times.mapIndexed { index, time ->
    val minimumDistance = times.distances[index]
    times.computeWinStrategyCountForTimeAndDistance(time, minimumDistance)
  }.fold(1) { acc, time -> acc * time}

internal fun part2(times: Times): Long =
  times.computeWinStrategyCountForTimeAndDistance(times.times.combine(), times.distances.combine())

internal data class Times(val times: List<Long>, val distances: List<Long>) {
  fun computeWinStrategyCountForTimeAndDistance(time: Long, minimumDistance: Long): Long =
    (1..<time).filter { pressButtonTime ->
      val distance = (time - pressButtonTime) * pressButtonTime
      distance > minimumDistance
    }.size.toLong()
}

internal fun List<Long>.combine() = this.joinToString(separator = "").toLong()

internal fun parseTimes(lines: List<String>): Times =
  lines.map { line ->
    line.split(":")[1].split("""\s+""".toRegex())
      .filter { it.isNotBlank() }
      .map { it.toLong() }
  }.let { numbers ->
    Times(numbers[0], numbers[1])
  }