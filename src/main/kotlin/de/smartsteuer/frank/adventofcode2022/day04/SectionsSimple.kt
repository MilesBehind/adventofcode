@file:Suppress("ConvertTwoComparisonsToRangeCheck")

package de.smartsteuer.frank.adventofcode2022.day04

import de.smartsteuer.frank.adventofcode2022.linesSequence
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun main() {
  println("day 04, part 1: ${part1Simple(linesSequence("/adventofcode2022/day04/sections.txt"))}")
  println("day 04, part 2: ${part2Simple(linesSequence("/adventofcode2022/day04/sections.txt"))}")
  repeat(10) {
    part1Simple(linesSequence("/adventofcode2022/day04/sections.txt"))
    part2Simple(linesSequence("/adventofcode2022/day04/sections.txt"))
  }
  measureTime {
    repeat(100) {
      part1Simple(linesSequence("/adventofcode2022/day04/sections.txt"))
      part2Simple(linesSequence("/adventofcode2022/day04/sections.txt"))
    }
  }.let { duration -> println("duration: ${duration / 100}") }
}

fun part1Simple(lines: Sequence<String>): Int =
  parseLines(lines).count { (f1, t1, f2, t2) ->
    f1 <= f2 && f2 <= t2 && t2 <= t1 || f2 <= f1 && f1 <= t1 && t1 <= t2
  }

fun part2Simple(lines: Sequence<String>): Int =
  parseLines(lines).count { (f1, t1, f2, t2) ->
    t1 >= f2 && t2 >= f1
  }

fun parseLines(lines: Sequence<String>): Sequence<List<Int>> = lines.map { line ->
  val chars = line.toCharArray()
  var i = 0
  var f1 = 0
  var t1 = 0
  var f2 = 0
  var t2 = 0
  while (chars[i].isDigit()) {
    f1 = f1 * 10 + (chars[i++].code - '0'.code)
  }
  while (chars[i].isDigit()) {
    t1 = t1 * 10 + (chars[i++].code - '0'.code)
  }
  while (chars[i++].isDigit()) {
    f2 = f2 * 10 + (chars[i++].code - '0'.code)
  }
  while (i < chars.size && chars[i].isDigit()) {
    t2 = t2 * 10 + (chars[i++].code - '0'.code)
  }
  listOf(f1, t1, f2, t2)
}

