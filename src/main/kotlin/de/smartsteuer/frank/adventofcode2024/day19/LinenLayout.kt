package de.smartsteuer.frank.adventofcode2024.day19

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  LinenLayout.execute(lines("/adventofcode2024/day19/towels.txt"))
}

object LinenLayout: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseTowels().countPossibleDesigns().toLong()

  override fun part2(input: List<String>): Long =
    input.parseTowels().countAllPossibleDesigns()

  data class Towels(val patterns: List<String>, val designs: List<String>) {
    private val lengthsToPatterns: Map<Int, Set<String>> = patterns.groupBy { it.length }.mapValues { it.value.toSet() }
    private val patternLengths = lengthsToPatterns.keys

    private fun String.findStartPatternLengths(start: Int): Map<Int, Int> =
      patternLengths.associateWith { length ->
        if (start + length > this.length) 0 else lengthsToPatterns.getValue(length).count { it == substring(start, start + length) }
      }.filterValues { it != 0 }

    fun countPossibleDesigns(): Int =
      designs.count { findDesignSolutions(it) > 0 }

    fun countAllPossibleDesigns(): Long =
      designs.sumOf { findDesignSolutions(it) }

    private fun findDesignSolutions(design: String): Long {
      tailrec fun findDesignSolutions(positionsToPositionCount: Map<Int, Long> = mapOf(0 to 1), solutions: Long = 0): Long {
        val newSolutions = solutions + positionsToPositionCount.filter { (position, _) -> position == design.length }.values.sum()
        val newPositionsToNewPositionCount = mutableMapOf<Int, Long>()
        positionsToPositionCount.entries.forEach { (position, count) ->
          design.findStartPatternLengths(position).forEach { (newPosition, newCount) ->
            newPositionsToNewPositionCount[position + newPosition] = newPositionsToNewPositionCount.getOrDefault(position + newPosition, 0) + newCount * count
          }
        }
        return if (newPositionsToNewPositionCount.isEmpty()) newSolutions else findDesignSolutions(newPositionsToNewPositionCount, newSolutions)
      }
      return findDesignSolutions()
    }
  }

  private fun List<String>.parseTowels() =
    Towels(first().split(", "), drop(2))
}
