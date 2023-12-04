package de.smartsteuer.frank.adventofcode2023.day04

import de.smartsteuer.frank.adventofcode2023.lines

fun main() {
  val scratchCards = parseCards(lines("/adventofcode2023/day04/scratch-cards.txt"))
  println("part 1: ${part1(scratchCards)}")
  println("part 2: ${part2(scratchCards)}")
}

internal fun part1(scratchCards: List<ScratchCard>): Int =
  scratchCards.sumOf { scratchCard ->
    val hits = scratchCard.computeHits()
    (if (hits.isNotEmpty()) (1 shl (hits.size - 1)) else 0)
  }

internal fun part2(scratchCards: List<ScratchCard>): Int {
  tailrec fun computeCardCopies(scratchCardIndex: Int, copyIndex: Int, copyCounts: List<Int>): List<Int> {
    if (scratchCardIndex >= scratchCards.size) return copyCounts
    if (copyIndex >= copyCounts[scratchCardIndex]) return computeCardCopies(scratchCardIndex + 1, 0, copyCounts)
    val scratchCard = scratchCards[scratchCardIndex]
    val hitCount = scratchCard.computeHits().size
    val copies   = scratchCard.id..<scratchCard.id + hitCount
    val newCopyCounts = copies.fold(copyCounts) { acc, index -> acc.increment(index) }
    return computeCardCopies(scratchCardIndex, copyIndex + 1, newCopyCounts)
  }
  return computeCardCopies(0, 0, List(scratchCards.size) { 1 }).sum()
}

private fun List<Int>.increment(index: Int): List<Int> = this.toMutableList().apply { set(index, get(index) + 1) }

internal data class ScratchCard(val id: Int, val winningNumbers: Set<Int>, val numbers: Set<Int>) {
  fun computeHits() = winningNumbers intersect numbers
}

internal fun parseCards(lines: List<String>): List<ScratchCard> =
  """Card\s+(\d+):(.*)\|(.*)""".toRegex().let { regex ->
    lines.mapNotNull { line ->
      regex.matchEntire(line)?.let { matchResult ->
        ScratchCard(matchResult.groupValues[1].trim().toInt(),
                    splitNumbers(matchResult.groupValues[2]),
                    splitNumbers(matchResult.groupValues[3]))
      }
    }
  }

private fun splitNumbers(numbers: String) =
  numbers.trim().split("""\s+""".toRegex()).map { it.trim().toInt() }.toSet()
