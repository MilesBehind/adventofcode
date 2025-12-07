package de.smartsteuer.frank.adventofcode2025.day07

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines
import de.smartsteuer.frank.adventofcode2025.day07.Coordinate.Companion.c

fun main() {
  Laboratories.execute(lines("/adventofcode2025/day07/splitters.txt"))
}

object Laboratories: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseSplitters().countSplits()

  override fun part2(input: List<String>): Long =
    input.parseSplitters().countQuantumSplits()
}

internal data class Coordinate(val x: Int, val y: Int) {
  companion object {
    fun c(x: Int, y: Int) = Coordinate(x, y)
  }
  operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)
}


internal data class Splitters(val start: Coordinate, val splitters: Set<Coordinate>, val width: Int, val height: Int) {
  fun countSplits(): Long {
    tailrec fun countSplits(beams: Set<Coordinate>, result: Long): Long {
      val nextY = beams.maxOf { it.y } + 1
      if (nextY >= height) return result
      val newBeams = beams.map { beam ->
        val newBeam = beam + c(0, 1)
        if (newBeam in splitters) setOf(newBeam + c(-1, 0), newBeam + c(1, 0)) else setOf(newBeam)
      }
      val splits = newBeams.count { it.size > 1 }
      return countSplits(newBeams.flatten().toSet(), result + splits)
    }
    return countSplits(setOf(start), 0)
  }

  fun countQuantumSplits(): Long {
    tailrec fun countQuantumSplits(beams: Map<Coordinate, Long>): Long {
      val nextY = beams.keys.maxOf { it.y } + 1
      if (nextY >= height) return beams.values.sum()
      val newBeams: Map<Coordinate, Long> = beams.entries.fold(mutableMapOf()) { acc, (beam, count) ->
        acc.also {
          val newBeam = beam + c(0, 1)
          if (newBeam in splitters) {
            acc.merge(newBeam + c(-1, 0), count, Long::plus)
            acc.merge(newBeam + c( 1, 0), count, Long::plus)
          } else {
            acc.merge(newBeam, count, Long::plus)
          }
        }
      }
      return countQuantumSplits(newBeams)
    }
    return countQuantumSplits(mapOf(start to 1L))
  }
}

internal fun List<String>.parseSplitters(): Splitters {
  val symbols = flatMapIndexed { y, line ->
    line.mapIndexed { x, char ->
      c(x, y) to char
    }
  }
  return Splitters(symbols.first { it.second == 'S' }.first, symbols.filter { it.second == '^' }.map { it.first }.toSet(), first().length, size)
}
