package de.smartsteuer.frank.adventofcode2024.day20

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines
import kotlin.math.absoluteValue

fun main() {
  RaceCondition.execute(lines("/adventofcode2024/day20/racetrack.txt"))
}

object RaceCondition: Day<Int> {
  override fun part1(input: List<String>): Int =
    input.parseRaceTrack().findCheats(minimumTimeSaved = 100, maximumCheatLength = 2).values.sum()

  override fun part2(input: List<String>): Int =
    input.parseRaceTrack().findCheats(minimumTimeSaved = 100, maximumCheatLength = 20).values.sum()

  data class Pos(val x: Int, val y: Int) {
    companion object {
      val circle =
        (-20..20).flatMap { dx ->
          (-20..20).map { dy ->
            Pos(dx, dy)
          }
        }.filter { delta -> delta.x.absoluteValue + delta.y.absoluteValue in 2..20 }
    }
    fun length() = x.absoluteValue + y.absoluteValue
    infix fun distanceTo(other: Pos) = (other - this).length()
    fun neighbours(): List<Pos> = listOf(Pos(x - 1, y), Pos(x, y - 1), Pos(x + 1, y), Pos(x, y + 1))
    fun circleOf(radius: Int): List<Pos> = circle.filter { it.length() <= radius }.map { it + this }
    operator fun plus (other: Pos) = Pos(x + other.x, y + other.y)
    operator fun minus(other: Pos) = Pos(other.x - x, other.y - y)
  }

  data class RaceTrack(val track: Set<Pos>, val walls: Set<Pos>, val start: Pos, val end: Pos) {
    internal val trackPositionsWithoutCheating = computeTrackPositionsWithoutCheating()
    internal val width  = walls.maxOf { it.x } + 1
    internal val height = walls.maxOf { it.y } + 1

    private fun computeTrackPositionsWithoutCheating(): Set<Pos> {
      tailrec fun computeTrackPositionsWithoutCheating(pos: Pos, result: Set<Pos>): Set<Pos> {
        if (pos == end) return result
        val nextPos = pos.neighbours().first { it !in walls && it !in result }
        return computeTrackPositionsWithoutCheating(nextPos, result + nextPos)
      }
      return computeTrackPositionsWithoutCheating(start, setOf(start))
    }

    fun time(trackPosition: Pos): Int =
      trackPositionsWithoutCheating.indexOf(trackPosition)

    fun findCheats(minimumTimeSaved: Int, maximumCheatLength: Int): Map<Int, Int> =
      trackPositionsWithoutCheating.fold<Pos, MutableList<Int>>(mutableListOf()) { cheats, trackPosition ->
        cheats.also {
          trackPosition.circleOf(maximumCheatLength).filter { it in track }.forEach { candidate ->
            val savedTime = time(candidate) - time(trackPosition) - (candidate distanceTo trackPosition)
            if (savedTime >= minimumTimeSaved) {
              //printTrack(neighbour, candidate, savedTime)
              cheats.add(savedTime)
              println(cheats.size)
            }
          }
        }
      }.groupBy { it }.mapValues { it.value.size }

    private fun printTrack(cheatFrom: Pos, cheatTo: Pos, timeSaved: Int) {
      println("cheat from $cheatFrom to $cheatTo saved $timeSaved")
      (0 until height).forEach { y ->
        (0 until width).forEach { x ->
          val pos = Pos(x, y)
          print(when (pos) {
            cheatFrom -> '1'
            cheatTo   -> '2'
            start     -> 'S'
            end       -> 'E'
            in walls  -> '#'
            else      -> "."
          })
        }
        println()
      }
      println()
    }
  }

  fun List<String>.parseRaceTrack(): RaceTrack =
    flatMapIndexed { y, line ->
      line.mapIndexed { x, char ->
        Pos(x, y) to char
      }
    }.toMap().let { grid ->
      RaceTrack(track = grid.filterValues { it in setOf('.', 'S', 'E') }.keys,
                walls = grid.filterValues { it == '#' }.keys,
                start = grid.filterValues { it == 'S' }.keys.first(),
                end   = grid.filterValues { it == 'E' }.keys.first())
    }
}