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
  toDigits(input).let { digits -> digits.first() * 10 + digits.last() }

private fun calibrationValueIncludingText(input: String): Int =
  toDigitsIncludingText(input).let { digits -> digits.first() * 10 + digits.last() }

private fun toDigits(input: String): List<Int> =
  input.filter { it.isDigit() }.map { it.digitToInt() }

private fun toDigitsIncludingText(input: String) =
  input.indices
    .map { input.substring(it) }
    .mapNotNull { subInput ->
      when {
        subInput.first().isDigit() -> subInput.first().digitToInt()
        else                       -> numbers.entries.find { (word, _) -> subInput.startsWith(word) }?.value
      }
    }

private val numbers = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
