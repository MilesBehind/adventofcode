package de.smartsteuer.frank.adventofcode2022.day04

import de.smartsteuer.frank.adventofcode2022.linesSequence

fun main() {
  println("day 04, part 1: ${part1(linesSequence("/adventofcode2022/day04/sections.txt"))}")
  println("day 04, part 2: ${part2(linesSequence("/adventofcode2022/day04/sections.txt"))}")
}

fun part1(lines: Sequence<String>): Int =
  sectionRanges(lines).count { (first, second) ->
    first contains second || second contains first
  }

fun part2(lines: Sequence<String>): Int =
  sectionRanges(lines).count { (first, second) ->
    (first intersect second).size > 0
  }

fun sectionRanges(lines: Sequence<String>): Sequence<List<IntRange>> =
  """(\d+)-(\d+),(\d+)-(\d+)""".toRegex().let { regex ->
    lines.map { line ->
      regex.matchEntire(line)?.let { matchResult ->
        matchResult.groupValues
          .drop(1)
          .map { it.toInt() }
          .chunked(2)
          .map { (from, to) -> (from..to) }
      } ?: throw IllegalArgumentException("line not parsable: $line")
    }
  }

infix fun IntRange.contains(other: IntRange) = this.first <= other.first && last >= other.last

infix fun IntRange.intersect(other: IntRange): IntRange = first.coerceAtLeast(other.first)..last.coerceAtMost(other.last)

val IntRange.size: Int get() = last - first + 1
