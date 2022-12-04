@file:Suppress("ConvertTwoComparisonsToRangeCheck")

package de.smartsteuer.frank.adventofcode2022.day04

import de.smartsteuer.frank.adventofcode2022.linesSequence
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
  println("day 04, part 1: ${part1Simple(linesSequence("/adventofcode2022/day04/sections.txt"))}")
  println("day 04, part 2: ${part2Simple(linesSequence("/adventofcode2022/day04/sections.txt"))}")
  repeat(8_000) {
    part1Simple(linesSequence("/adventofcode2022/day04/sections.txt"))
    part2Simple(linesSequence("/adventofcode2022/day04/sections.txt"))
  }
  measureTime {
    repeat(1_000) {
      part1Simple(linesSequence("/adventofcode2022/day04/sections.txt"))
      part2Simple(linesSequence("/adventofcode2022/day04/sections.txt"))
    }
  }.let { duration -> println("duration: ${duration / 1_000}") }
}

fun part1Simple(lines: Sequence<String>): Int =
  parseLines(lines).count { (from1, to1, from2, to2) ->
    from1 <= from2 && from2 <= to2 && to2 <= to1 || from2 <= from1 && from1 <= to1 && to1 <= to2
  }

fun part2Simple(lines: Sequence<String>): Int =
  parseLines(lines).count { (from1, to1, from2, to2) ->
    to1 >= from2 && to2 >= from1
  }

fun parseLines(lines: Sequence<String>): Sequence<List<Int>> = lines.map { line ->
  val chars = line.toCharArray()
  var i     = 0
  var from1 = 0
  var to1   = 0
  var from2 = 0
  var to2   = 0
  while (chars[i].isDigit()) {
    from1 = from1 * 10 + (chars[i++].code - '0'.code)
  }
  i++
  while (chars[i].isDigit()) {
    to1 = to1 * 10 + (chars[i++].code - '0'.code)
  }
  i++
  while (chars[i].isDigit()) {
    from2 = from2 * 10 + (chars[i++].code - '0'.code)
  }
  i++
  while (i < chars.size && chars[i].isDigit()) {
    to2 = to2 * 10 + (chars[i++].code - '0'.code)
  }
  listOf(from1, to1, from2, to2)
}

