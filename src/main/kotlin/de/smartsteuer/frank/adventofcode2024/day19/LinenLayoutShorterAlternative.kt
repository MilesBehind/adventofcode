package de.smartsteuer.frank.adventofcode2024.day19

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  LinenLayoutShorterAlternative.execute(lines("/adventofcode2024/day19/towels.txt"))
}

object LinenLayoutShorterAlternative: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseTowels().countPossibleDesigns().toLong()

  override fun part2(input: List<String>): Long =
    input.parseTowels().countAllPossibleDesigns()

  data class Towels(val patterns: List<String>, val designs: List<String>) {
    fun countPossibleDesigns(): Int = designs.count { findDesignSolutions(it) > 0 }

    fun countAllPossibleDesigns(): Long = designs.sumOf { findDesignSolutions(it) }

    private val cache = mutableMapOf<String, Long>()

    private fun findDesignSolutions(design: String): Long = cache.getOrPut(design) {
      if (design.isEmpty()) 1 else patterns.sumOf { pattern -> if (design.startsWith(pattern)) findDesignSolutions(design.drop(pattern.length)) else 0 }
    }
  }

  private fun List<String>.parseTowels() = Towels(first().split(", "), drop(2))
}
