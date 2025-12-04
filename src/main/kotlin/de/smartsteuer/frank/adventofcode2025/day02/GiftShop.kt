package de.smartsteuer.frank.adventofcode2025.day02

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  GiftShop.execute(lines("/adventofcode2025/day02/id-ranges.txt"))
}

object GiftShop: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseIdRanges().flatMap { it.findInvalidIds(::isInvalid) }.sum()

  override fun part2(input: List<String>): Long =
    input.parseIdRanges().flatMap { it.findInvalidIds(::isInvalid2) }.sum()
}

internal data class IdRange(val from: Long, val to: Long) {
  fun findInvalidIds(checker: (Long) -> Boolean): List<Long> =
    (from..to).filter { checker(it) }
}

internal fun isInvalid(id: Long): Boolean =
  id.toString().let { digits ->
    if (digits.length % 2 == 1) false else {
      val patternLength = digits.length / 2
      return digits.take(patternLength) == digits.takeLast(patternLength)
    }
  }

internal fun isInvalid2(id: Long): Boolean =
  id.toString().let { digits ->
    (1..(digits.length / 2)).any { patternLength ->
      if (digits.length % patternLength != 0) false else {
        val parts = digits.chunked(patternLength)
        parts.drop(1).all { it == parts.first() }
      }
    }
  }

internal fun List<String>.parseIdRanges(): List<IdRange> =
  first().split(",").map { range -> range.split("-").map { it.toLong() } }.map { IdRange(it[0], it[1]) }
