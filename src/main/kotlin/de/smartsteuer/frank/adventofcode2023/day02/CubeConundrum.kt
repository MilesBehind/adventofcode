package de.smartsteuer.frank.adventofcode2023.day02

import de.smartsteuer.frank.adventofcode2023.day02.CubeColor.*
import de.smartsteuer.frank.adventofcode2023.lines

fun main() {
  val games: List<Game> = parseGames(lines("/adventofcode2023/day02/games.txt"))
  println("part 1: ${part1(games)}")
  println("part 2: ${part2(games)}")
}

internal enum class CubeColor { RED, GREEN, BLUE }

internal val availableCubes = mapOf(RED to 12, GREEN to 13, BLUE to 14)

internal fun part1(games: List<Game>): Int =
  games
    .filter { game -> game.sets.all { set -> set.entries.all { (cube, count) -> (availableCubes[cube] ?: 0) >= count } } }
    .sumOf { it.id }

@Suppress("SimplifiableCallChain")
internal fun part2(games: List<Game>): Int =
  games.map { game ->
    val maxCountsPerColor = CubeColor.entries.associateWith { color ->
      game.sets.maxOfOrNull { set ->
        set.entries.firstOrNull { it.key == color }?.value ?: 0
      } ?: 0
    }
    maxCountsPerColor.values.fold(1) { acc, count -> acc * count}
  }.sum()

internal fun parseGames(games: List<String>): List<Game> =
  games.map { line ->
    val parts = line.split(":", ";")
    val id = parts[0].drop("Game ".length).toInt()
    val cubeParts = parts.drop(1)
    val sets = cubeParts.map { part ->
      part.split(",").associate { countAndColor ->
        val (countAsString, color) = countAndColor.trim().split(" ")
        valueOf(color.trim().uppercase()) to countAsString.trim().toInt()
      }
    }
    Game(id, sets)
  }

internal data class Game(val id: Int, val sets: List<Map<CubeColor, Int>>)