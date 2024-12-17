package de.smartsteuer.frank.adventofcode2024.day10

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  HikingTrails.execute(lines("/adventofcode2024/day10/height-map.txt"))
}

object HikingTrails: Day<Long> {

  override fun part1(input: List<String>): Long =
    input.parseHeightMap().findTrails().size.toLong()

  override fun part2(input: List<String>): Long =
    input.parseHeightMap().findTrails(multiplePathsToSameEndAllowed = true).size.toLong()

  data class Pos(val x: Int, val y: Int) {
    fun neighbours() = listOf(Pos(x + 1, y), Pos(x - 1, y), Pos(x, y + 1), Pos(x, y - 1))
  }

  data class HeightMap(val width: Int, val height: Int, val heightMap: Map<Pos, Int>) {

    fun findTrails(multiplePathsToSameEndAllowed: Boolean = false): List<Trail> {
      val startPositions = heightMap.entries.filter { it.value == 0 }.map { it.key }
      return startPositions.flatMap { startPosition ->
        tailrec fun findTrails(start: Pos, current: List<Pos>): List<Trail> {
          val ends = current.filter { heightMap[it] == 9 }
          if (ends.size == current.size) return current.map { Trail(start, it) }
          val nextPositions = current.flatMap { pos ->
            pos.neighbours().filter { nextPos ->
              heightMap[nextPos] == heightMap.getValue(pos) + 1
            }
          }
          return findTrails(start, if (multiplePathsToSameEndAllowed) nextPositions + ends else (nextPositions + ends).distinct())
        }
        findTrails(startPosition, listOf(startPosition))
      }
    }
  }

  data class Trail(val start: Pos, val end: Pos)

  private fun List<String>.parseHeightMap(): HeightMap =
    HeightMap(first().length, size, flatMapIndexed { y: Int, line: String ->
      line.mapIndexed { x, c ->
        Pos(x, y) to c.digitToInt()
      }
    }.toMap())
}
