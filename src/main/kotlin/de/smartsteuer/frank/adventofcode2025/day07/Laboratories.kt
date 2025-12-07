package de.smartsteuer.frank.adventofcode2025.day07

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Laboratories.execute(lines("/adventofcode2025/day07/splitters.txt"))
}

object Laboratories: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseSplitters().countSplits()

  override fun part2(input: List<String>): Long =
    input.parseSplitters().countQuantumSplits()
}

internal data class Splitters(val start: Int, val splitters: List<Set<Int>>) {
  fun countSplits(): Long {
    tailrec fun countSplits(splitters: List<Set<Int>>, beams: List<Int>, result: Long): Long {
      if (splitters.isEmpty()) return result
      val newBeams = beams.map { beam -> if (beam in splitters.first()) setOf(beam - 1, beam + 1) else setOf(beam) }
      val splits = newBeams.count { it.size > 1 }
      return countSplits(splitters.drop(1), newBeams.flatten().distinct(), result + splits)
    }
    return countSplits(splitters, listOf(start), 0)
  }

  fun countQuantumSplits(): Long {
    tailrec fun countQuantumSplits(splitters: List<Set<Int>>, beams: Map<Int, Long>): Long {
      if (splitters.isEmpty()) return beams.values.sum()
      val newBeams: Map<Int, Long> = beams.entries.fold(mutableMapOf()) { acc, (beam, count) ->
        acc.also {
          if (beam in splitters.first()) {
            acc.merge(beam - 1, count, Long::plus)
            acc.merge(beam + 1, count, Long::plus)
          } else {
            acc.merge(beam, count, Long::plus)
          }
        }
      }
      return countQuantumSplits(splitters.drop(1), newBeams)
    }
    return countQuantumSplits(splitters, mapOf(start to 1L))
  }
}

internal fun List<String>.parseSplitters() =
  Splitters(start     = this.first().indexOf('S'),
            splitters = filter { line -> line.contains('^') }.map { line ->
              line.mapIndexedNotNull<Int> { x, char -> if (char == '^') x else null }.toSet()
            })