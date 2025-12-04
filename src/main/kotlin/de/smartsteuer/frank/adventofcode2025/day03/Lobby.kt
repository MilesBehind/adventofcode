package de.smartsteuer.frank.adventofcode2025.day03

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Lobby.execute(lines("/adventofcode2025/day03/batteries.txt"))
}

object Lobby: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseBatteryBanks().sumOf { it.maximumJoltage(2) }

  override fun part2(input: List<String>): Long =
    input.parseBatteryBanks().sumOf { it.maximumJoltage(12) }
}

data class BatteryBank(val batteries: List<Int>) {
  fun maximumJoltage(digits: Int): Long {
    fun findLargestDigit(fromIndex: Int, remainingDigits: Int): Pair<Int, Int> { // index to digit
      if (remainingDigits == 0) return 0 to 0
      val toIndex = batteries.size - remainingDigits
      return (fromIndex..toIndex).map { it to batteries[it] }.maxBy { (_, digit) -> digit }
    }
    tailrec fun findLargestDigits(index: Int, remainingDigits: Int, result: List<Int>): List<Int> {
      if (remainingDigits == 0) return result
      val (indexOfLargestDigit, largestDigit) = findLargestDigit(index, remainingDigits)
      return findLargestDigits(indexOfLargestDigit + 1, remainingDigits - 1, result + largestDigit)
    }
    return findLargestDigits(0, digits, emptyList()).joinToString(separator = "").toLong()
  }
}

internal fun List<String>.parseBatteryBanks(): List<BatteryBank> =
  map { line ->
    BatteryBank(line.map { it.digitToInt() })
  }
