package de.smartsteuer.frank.adventofcode2024.day03

import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  val lines = lines("/adventofcode2024/day03/computer-memory.txt")
  println("part 1: ${part1b(lines)}")
  println("part 2: ${part2b(lines)}")
}

internal fun part1b(lines: List<String>): Int =
  """mul\((\d{1,3}),(\d{1,3})\)""".toRegex().let { regex ->
    lines.flatMap { line -> regex.findAll(line) }
      .sumOf { match -> match.groupValues[1].toInt() * match.groupValues[2].toInt() }
  }

internal fun part2b(lines: List<String>): Int =
  """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""".toRegex().let { regex ->
    lines.flatMap { line -> regex.findAll(line) }
      .fold(true to 0) { (on, sum), match ->
        when (match.groupValues[0].take(3)) {
          "mul" -> if (on) true to sum + (match.groupValues[1].toInt() * match.groupValues[2].toInt()) else false to sum
          "don" -> false to sum
          else  -> true to sum
        }
      }
  }.second
