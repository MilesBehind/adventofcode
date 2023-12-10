package de.smartsteuer.frank.adventofcode2023.day09

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val histories = parseHistories(lines("/adventofcode2023/day09/histories.txt"))
  measureTime { println("part 1: ${part1(histories)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(histories)}") }.also { println("part 2 took $it") }
}

internal fun part1(histories: List<History>): Long = histories.sumOf { it.computeNextNumber().toLong() }

internal fun part2(histories: List<History>): Long = histories.sumOf { it.copy(numbers = it.numbers.reversed()).computeNextNumber().toLong() }

internal data class History(val numbers: List<Int>) {
  private tailrec fun computeDifferences(result: List<List<Int>>): List<List<Int>> = when {
    result.last().all { it == 0 } -> result
    else                          -> computeDifferences(result + listOf(result.last().computeDifferences()))
  }

  fun computeNextNumber(): Int = computeDifferences(listOf(numbers)).sumOf { it.last() }
}

internal fun List<Int>.computeDifferences(): List<Int> = this.zipWithNext().map { (first, second) -> second - first }

internal fun parseHistories(lines: List<String>): List<History> = lines.map { line -> History(line.split(" ").map { it.toInt() }) }
