package de.smartsteuer.frank.adventofcode2022.day25

import de.smartsteuer.frank.adventofcode2022.day25.Day25.part1
import de.smartsteuer.frank.adventofcode2022.day25.Day25.part2
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.system.measureTimeMillis

fun main() {
  val input = lines("/adventofcode2022/day25/snafu-numbers.txt")
  measureTimeMillis {
    println("day 25, part 1: ${part1(input)}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 25, part 2: ${part2(input)}")
  }.also { println("took $it ms") }
}

object Day25 {
  fun part1(input: List<String>): String = input.sumOf { Snafu(it).toLong() }.toSnafu().string

  fun part2(input: List<String>): String {
    return ""
  }

  @JvmInline
  value class Snafu(val string: String) {
    fun toLong(): Long = string.reversed().fold(0L to 1L) { (result, factor), digit -> (result + factor * digitValues.getValue(digit)) to factor * 5 }.first
    companion object {
      private val digitValues = mapOf('2' to 2, '1' to 1, '0' to 0, '-' to -1, '=' to -2)
    }
  }

  fun Long.toSnafu(): Snafu {
    val fiveBasedDigits = toString(5).map { it.digitToInt() }.reversed()
    tailrec fun convert(digits: List<Int>, result: String): String {
      if (digits.isEmpty()) return result
      return when (val digit = digits.first()) {
        0, 1, 2 -> convert(digits.drop(1), "$digit$result")
        3       -> convert(listOf(digits.getOrElse(1) { 0 } + 1) + digits.drop(2), "=$result")
        4       -> convert(listOf(digits.getOrElse(1) { 0 } + 1) + digits.drop(2), "-$result")
        5       -> convert(listOf(digits.getOrElse(1) { 0 } + 1) + digits.drop(2), "0$result")
        else    -> throw IllegalStateException("unexpected digit: $digit")
      }
    }
    return Snafu(convert(fiveBasedDigits, ""))
  }
}