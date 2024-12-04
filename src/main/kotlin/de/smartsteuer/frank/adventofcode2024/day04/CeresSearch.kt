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
          indexes.map { pos -> words.chars[pos] } == listOf('X', 'M', 'A', 'S')
        }
      }
    }

  fun part2(words: Words): Int =
    (0 until words.width).sumOf { x ->
      (0 until words.height).sumOf { y ->
        Pos(x, y).crossIndexes().count { indexes ->
          indexes.map { pos -> words.chars[pos] } == listOf('M', 'A', 'S',   'M', 'A', 'S')
        }
      }
    }

  data class Pos(val x: Int, val y: Int) {
    fun wordIndexes() = listOf(
      listOf(Pos(x, y), Pos(x + 1, y    ), Pos(x + 2, y    ), Pos(x + 3, y    )),
      listOf(Pos(x, y), Pos(x - 1, y    ), Pos(x - 2, y    ), Pos(x - 3, y    )),
      listOf(Pos(x, y), Pos(x,     y + 1), Pos(x,     y + 2), Pos(x,     y + 3)),
      listOf(Pos(x, y), Pos(x,     y - 1), Pos(x,     y - 2), Pos(x,     y - 3)),
      listOf(Pos(x, y), Pos(x + 1, y + 1), Pos(x + 2, y + 2), Pos(x + 3, y + 3)),
      listOf(Pos(x, y), Pos(x + 1, y - 1), Pos(x + 2, y - 2), Pos(x + 3, y - 3)),
      listOf(Pos(x, y), Pos(x - 1, y + 1), Pos(x - 2, y + 2), Pos(x - 3, y + 3)),
      listOf(Pos(x, y), Pos(x - 1, y - 1), Pos(x - 2, y - 2), Pos(x - 3, y - 3)),
    )
    fun crossIndexes() = listOf(
      listOf(Pos(x,     y    ), Pos(x + 1, y + 1), Pos(x + 2, y + 2),   Pos(x + 2, y    ), Pos(x + 1, y + 1), Pos(x,     y + 2)),
      listOf(Pos(x,     y    ), Pos(x + 1, y + 1), Pos(x + 2, y + 2),   Pos(x,     y + 2), Pos(x + 1, y + 1), Pos(x + 2, y    )),
      listOf(Pos(x + 2, y + 2), Pos(x + 1, y + 1), Pos(x,     y    ),   Pos(x + 2, y    ), Pos(x + 1, y + 1), Pos(x,     y + 2)),
      listOf(Pos(x + 2, y + 2), Pos(x + 1, y + 1), Pos(x,     y    ),   Pos(x,     y + 2), Pos(x + 1, y + 1), Pos(x + 2, y    )),
    )
  }

  data class Words(val chars: Map<Pos, Char>, val width: Int, val height: Int)

  fun List<String>.toWords(): Words =
    Words(flatMapIndexed { y, line ->
      line.mapIndexed { x, char ->
        Pos(x, y) to char
      }
    }.toMap(), this.first().length, this.size)
}
