package de.smartsteuer.frank.adventofcode2023.day01

import de.smartsteuer.frank.adventofcode2023.lines

fun main() {
  val calibrationInput: List<String> = lines("/adventofcode2023/day01/calibration.txt")
  println("part 1: ${part1(calibrationInput)}")
  println("part 2: ${part2(calibrationInput)}")
}

internal fun part1(calibrationInput: List<String>): Int = computeSum(calibrationInput, digits)

internal fun part2(calibrationInput: List<String>): Int = computeSum(calibrationInput, digits + words)

private fun computeSum(calibrationInput: List<String>, numbers: Map<String, Int>): Int =
  calibrationInput.map { calibrationValue(it, numbers) }.also { println("values: $it") }.sum()

private fun calibrationValue(input: String, numbers: Map<String, Int>): Int =
  toDigits(input, numbers).let { digits -> digits.first() * 10 + digits.last() }

private fun toDigits(input: String, numbers: Map<String, Int>) =
  input.indices
    .map { input.substring(it) }
    .mapNotNull { subInput -> numbers.entries.find { (word, _) -> subInput.startsWith(word) }?.value }

private val digits = mapOf("1"   to 1, "2"   to 2, "3"     to 3, "4"    to 4, "5"    to 5, "6"   to 6, "7"     to 7, "8"     to 8, "9"    to 9)
private val words  = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
