package de.smartsteuer.frank.adventofcode2024.day19

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  LinenLayout.execute(lines("/adventofcode2024/day19/towels.txt"))
}

object LinenLayout: Day<Int> {
  override fun part1(input: List<String>): Int =
    input.parseTowels().countPossibleDesigns()

  override fun part2(input: List<String>): Int =
    TODO()

  data class Towels(val patterns: List<String>, val designs: List<String>) {
    private val lengthsToPatterns: Map<Int, Set<String>> = patterns.groupBy { it.length }.mapValues { it.value.toSet() }
    private val patternLengths = lengthsToPatterns.keys

    private fun String.startPatternLengths(start: Int): List<Int> =
      patternLengths.filter { length ->
        start + length <= this.length && lengthsToPatterns.getValue(length).contains(substring(start, start + length))
      }

    fun countPossibleDesigns(): Int =
      designs.count { isPossibleDesign(it) }

    private fun isPossibleDesign(design: String): Boolean {
      //println("design: $design")
      tailrec fun isPossibleDesign(positions: Set<Int>): Boolean {
        //println("positions: $positions")
        val newPositions = positions.flatMap { position ->
          if (position == design.length) return true
          design.startPatternLengths(position).map { it + position }
        }.toSet()
        return if (newPositions.isEmpty()) false else isPossibleDesign(newPositions)
      }
      return isPossibleDesign(setOf(0))
    }
  }

  private fun List<String>.parseTowels() =
    Towels(first().split(", "), drop(2))
}
