package de.smartsteuer.frank.adventofcode2023.day08

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
  val map = parseMap(lines("/adventofcode2023/day08/map.txt"))
  measureTime { println("part 1: ${part1(map)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(map)}") }.also { println("part 2 took $it") }
}

internal fun part1(desertMap: DesertMap): Long =
  desertMap.followDirections("AAA") { it == "ZZZ" }

internal fun part2(desertMap: DesertMap): Long =
  desertMap.branches.keys.filter { it.endsWith('A') }.let { startPositions ->
    lcm(startPositions.map { startPosition -> desertMap.followDirections(startPosition) { it.endsWith('Z') } })
  }

internal data class DesertMap(val directions: String, val branches: Map<String, Pair<String, String>>) {
  fun followDirections(start: String, isTarget: (String) -> Boolean): Long {
    tailrec fun followDirections(currentPosition: String, count: Long): Long {
      if (isTarget(currentPosition)) return count
      val direction   = directions[(count % directions.length).toInt()]
      val branch      = branches[currentPosition] ?: throw IllegalStateException("invalid state: position = $currentPosition => no branch found")
      val newPosition = if (direction == 'L') branch.first else branch.second
      return followDirections(newPosition, count + 1)
    }
    return followDirections(start, 0)
  }
}

internal fun lcm(values: List<Long>): Long =
  values.drop(1).fold(values.first()) { acc, value -> max(acc, lcm(acc, value)) }

internal fun lcm(number1: Long, number2: Long): Long =
  if (number1 == 0L || number2 == 0L) 0 else abs(number1 * number2) / gcd(number1, number2)

internal tailrec fun gcd(number1: Long, number2: Long): Long {
  if (number1 == 0L || number2 == 0L) return number1 + number2
  val absNumber1 = abs(number1)
  val absNumber2 = abs(number2)
  val biggerValue = max(absNumber1, absNumber2)
  val smallerValue = min(absNumber1, absNumber2)
  return gcd(biggerValue % smallerValue, smallerValue)
}

internal fun parseMap(lines: List<String>): DesertMap {
  val directions = lines.first()
  val regex = """(\w{3}) = \((\w{3}), (\w{3})\)""".toRegex()
  val branches = buildMap {
    lines.drop(2).map { line ->
      val matchResult = regex.matchEntire(line) ?: throw IllegalArgumentException("could not parse line $line")
      val (_, from, left, right) = matchResult.groupValues
      this[from] = left to right
    }
  }
  return DesertMap(directions, branches)
}