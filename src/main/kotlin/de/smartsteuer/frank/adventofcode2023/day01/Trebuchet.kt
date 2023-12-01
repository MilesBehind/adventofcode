package de.smartsteuer.frank.adventofcode2023.day01

import de.smartsteuer.frank.adventofcode2023.lines

fun main() {
  val calibrationInput: List<String> = lines("/adventofcode2023/day01/calibration.txt")
  println("part 1: ${part1(calibrationInput)}")
  println("part 2: ${part2(calibrationInput)}")
}

internal fun part1(calibrationInput: List<String>): Int =
  calibrationInput.map { calibrationValue(it, toDigits) }.also { println("values: $it") }.sum()

internal fun part2(calibrationInput: List<String>): Int =
  calibrationInput.map { calibrationValue(it, toDigitsIncludingText) }.also { println("values: $it") }.sum()

private fun calibrationValue(input: String, converter: (String) -> List<Int>): Int =
  converter(input).let { digits -> digits.first() * 10 + digits.last() }

private val toDigits = { input: String -> input.filter { it.isDigit() }.map { it.digitToInt() } }

private val toDigitsIncludingText = { input: String ->
  input.indices
    .map { input.substring(it) }
    .mapNotNull { subInput ->
      when {
        subInput.first().isDigit() -> subInput.first().digitToInt()
        else                       -> numbers.entries.find { (word, _) -> subInput.startsWith(word) }?.value
      }
    }
}

private val numbers = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
