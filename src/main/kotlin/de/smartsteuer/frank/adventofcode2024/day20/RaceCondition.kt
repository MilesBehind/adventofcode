package de.smartsteuer.frank.adventofcode2024.day20

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines
import kotlin.math.absoluteValue

fun main() {
  RaceCondition.execute(lines("/adventofcode2024/day20/racetrack.txt"))
}

object RaceCondition: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseRaceTrack().findCheats(minimumTimeSaved = 100, maximumCheatLength = 2).values.sum()

  override fun part2(input: List<String>): Long =
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
    fun circleWithRadius(radius: Int): List<Pos> = circle.filter { it.length() <= radius }.map { it + this }
    operator fun plus (other: Pos) = Pos(x + other.x, y + other.y)
    operator fun minus(other: Pos) = Pos(other.x - x, other.y - y)
  }

  data class RaceTrack(val track: Set<Pos>, val walls: Set<Pos>, val start: Pos, val end: Pos) {
    internal val trackPositionsWithoutCheating = computeTrackPositionsWithoutCheating().withIndex().associate { it.value to it.index  }

    private fun computeTrackPositionsWithoutCheating(): Set<Pos> {
      tailrec fun computeTrackPositionsWithoutCheating(pos: Pos, result: Set<Pos>): Set<Pos> {
        if (pos == end) return result
        val nextPos = pos.neighbours().first { it !in walls && it !in result }
        return computeTrackPositionsWithoutCheating(nextPos, result + nextPos)
      }
      return computeTrackPositionsWithoutCheating(start, setOf(start))
    }

    fun time(trackPosition: Pos): Int = trackPositionsWithoutCheating[trackPosition] ?: 0

    fun findCheats(minimumTimeSaved: Int, maximumCheatLength: Int): Map<Int, Long> =
      trackPositionsWithoutCheating.keys.fold(mutableMapOf()) { cheats, trackPosition ->
        cheats.also {
          trackPosition.circleWithRadius(maximumCheatLength).filter { it in track }.forEach { candidate ->
            val savedTime = time(candidate) - time(trackPosition) - (candidate distanceTo trackPosition)
            if (savedTime >= minimumTimeSaved) {
              cheats[savedTime] = cheats.getOrDefault(savedTime, 0) + 1
            }
          }
        }
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