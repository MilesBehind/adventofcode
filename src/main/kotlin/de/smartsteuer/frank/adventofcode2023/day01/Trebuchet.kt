package de.smartsteuer.frank.adventofcode2023.day01

import de.smartsteuer.frank.adventofcode2023.lines

fun main() {
  val calibration: List<String> = lines("/adventofcode2023/day01/calibration.txt")
  part1(calibration)
  part2(calibration)
}

internal fun part1(calibration: List<String>): Int {
  val calibrationValues = calibration.map { calibrationValue(it) }
  val sum = calibrationValues.sum()
  println("part 1: calibration values = $calibrationValues,\nsum = $sum")
  return sum
}

internal fun part2(calibration: List<String>): Int {
  val calibrationValues = calibration.map { calibrationValueIncludingText(it) }
  val sum = calibrationValues.sum()
  println("part 2: calibration values = $calibrationValues,\nsum = $sum")
  return sum
}

private fun calibrationValue(input: String): Int =
  (input.find     { it.isDigit() }?.digitToInt() ?: 0) * 10 +
  (input.findLast { it.isDigit() }?.digitToInt() ?: 0)

private val numbers = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)

private fun calibrationValueIncludingText(input: String): Int {
  val first = input.indices.map            { index -> startToDigit(input.substring(index))        }.first { it != null } ?: 0
  val last  = input.indices.reversed().map { index -> endToDigit  (input.substring(0, index + 1)) }.first { it != null } ?: 0
  return first * 10 + last
}

internal fun startToDigit(input: String): Int? = when {
  input.first().isDigit() -> input.first().digitToInt()
  else                    -> numbers.entries.find { input.startsWith(it.key) }?.value
}

internal fun endToDigit(input: String): Int? = when {
  input.last().isDigit() -> input.last().digitToInt()
  else                   -> numbers.entries.find { input.endsWith(it.key) }?.value
}
