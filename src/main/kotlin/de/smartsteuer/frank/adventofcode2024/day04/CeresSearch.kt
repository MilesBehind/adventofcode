package de.smartsteuer.frank.adventofcode2024.day04

import de.smartsteuer.frank.adventofcode2024.day04.CeresSearch.part1
import de.smartsteuer.frank.adventofcode2024.day04.CeresSearch.part2
import de.smartsteuer.frank.adventofcode2024.day04.CeresSearch.toWords
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  val words = lines("/adventofcode2024/day04/words.txt").toWords()
  println("part 1: ${part1(words)}")
  println("part 2: ${part2(words)}")
}

object CeresSearch {
  fun part1(words: Words): Int =
    (0 until words.width).sumOf { x ->
      (0 until words.height).sumOf { y ->
        Pos(x, y).wordIndexes().count { indexes ->
          indexes.map { pos -> words.chars[pos] }.joinToString(separator = "") == "XMAS"
        }
      }
    }

  fun part2(words: Words): Int =
    (0 until words.width).sumOf { x ->
      (0 until words.height).count { y ->
        Pos(x, y).crossIndexes().map { pos -> words.chars[pos] }.joinToString(separator = "") in listOf("MASMAS", "MASSAM", "SAMMAS", "SAMSAM")
      }
    }

  data class Pos(val x: Int, val y: Int) {
    fun wordIndexes() = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1, 1 to 1, 1 to -1, -1 to 1, -1 to -1)
      .map { (dx, dy) -> (0..3).map { (it * dx) to (it * dy) }.map { (dx, dy) -> Pos(x + dx, y + dy) } }

    fun crossIndexes() = listOf(Pos(x, y), Pos(x + 1, y + 1), Pos(x + 2, y + 2),   Pos(x + 2, y), Pos(x + 1, y + 1), Pos(x, y + 2))
  }

  data class Words(val chars: Map<Pos, Char>, val width: Int, val height: Int)

  fun List<String>.toWords(): Words =
    Words(flatMapIndexed { y, line ->
      line.mapIndexed { x, char ->
        Pos(x, y) to char
      }
    }.toMap(), this.first().length, this.size)
}
