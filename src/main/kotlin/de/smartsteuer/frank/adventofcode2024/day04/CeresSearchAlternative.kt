package de.smartsteuer.frank.adventofcode2024.day04

import de.smartsteuer.frank.adventofcode2024.day04.CeresSearchAlternative.part1
import de.smartsteuer.frank.adventofcode2024.day04.CeresSearchAlternative.part2
import de.smartsteuer.frank.adventofcode2024.day04.CeresSearchAlternative.toWords
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  val words = lines("/adventofcode2024/day04/words.txt").toWords()
  println("part 1: ${part1(words)}")
  println("part 2: ${part2(words)}")
}

object CeresSearchAlternative {
  fun part1(words: Words): Int =
    words.chars.filterValues { it == 'X' }.keys.sumOf { pos ->
      pos.wordIndexes().count { wordIndexes -> wordIndexes.map { words.chars[it] }.joinToString("") == "MAS" }
    }

  fun part2(words: Words): Int =
    words.chars.filterValues { it == 'A' }.keys.count { pos ->
      pos.crossIndexes().map { words.chars[it] }.joinToString("") in listOf("MSMS", "SMSM", "MSSM", "SMMS")
    }

  data class Pos(val x: Int, val y: Int) {
    companion object {
      val neighbours = listOf(Pos(1, 0), Pos(-1, 0), Pos(0, 1), Pos(0, -1), Pos(1, 1), Pos(1, -1), Pos(-1, 1), Pos(-1, -1))
    }
    fun wordIndexes() = neighbours.map { neighbour -> (1..3).map { Pos(it * neighbour.x + x, it * neighbour.y + y) } }
    fun crossIndexes() = listOf(Pos(x - 1, y - 1), Pos(x + 1, y + 1),   Pos(x + 1, y - 1), Pos(x - 1, y + 1))
  }

  data class Words(val chars: Map<Pos, Char>, val width: Int, val height: Int)

  fun List<String>.toWords(): Words =
    Words(flatMapIndexed { y, line ->
      line.mapIndexed { x, char ->
        Pos(x, y) to char
      }
    }.toMap(), this.first().length, this.size)
}
