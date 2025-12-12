package de.smartsteuer.frank.adventofcode2025.day12

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  ChristmasTreeFarm.execute(lines("/adventofcode2025/day12/presents.txt"))
}

object ChristmasTreeFarm: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parsePresentsAndRegions().countFittingRegions().toLong()

  override fun part2(input: List<String>): Long = 0
}

internal data class Coordinate(val x: Int, val y: Int)

internal data class Present(val index: Int, val occupied: Set<Coordinate>) {
  val area = occupied.size
}

internal data class Region(val width: Int, val height: Int, val presentCounts: List<Int>) {
  val area = width * height
  fun remainingArea(presents: List<Present>): Int = area - presentCounts.mapIndexed { index, count -> count * presents[index].area }.sum()
}

internal data class PresentsAndRegions(val presents: List<Present>, val regions: List<Region>) {
  fun countFittingRegions(): Int {
    val remainingAreasForAllRegions = regions.map { it.remainingArea(presents) }
    println("all unique missing spaces in regions that do not fit present areas: ${remainingAreasForAllRegions.filter { it < 0 }.toSet()}")
    println("minimum space remaining in regions that fit present areas:          ${remainingAreasForAllRegions.filter { it >= 0 }.min()}")
    return regions.count { it.remainingArea(presents) >= 0 }
  }
}

internal fun List<String>.parsePresentsAndRegions(): PresentsAndRegions {
  val blankLineIndexes = listOf(-1) + withIndex().filter { (_, line) -> line.isBlank() }.map { it.index }
  val presentParts = blankLineIndexes.zipWithNext().map { (from, to) -> drop(from + 1).take(to - from) }
  val presents = presentParts.map { lines ->
    val presentIndex = lines.first().dropLast(1).toInt()
    val presentCoordinates = lines.drop(1).mapIndexed { y, line ->
      line.mapIndexedNotNull { x, c ->
        if (c == '#') Coordinate(x, y) else null
      }
    }
    Present(presentIndex, presentCoordinates.flatten().toSet())
  }
  val regionPattern = """(\d+)x(\d+): (.*)""".toRegex()
  val regions = drop(blankLineIndexes.last() + 1).map { line ->
    val (width, height, indexes) = regionPattern.matchEntire(line)?.destructured ?: error("could not parse line '$line'")
    val presentCounts = indexes.split(' ').map { it.toInt() }
    Region(width.toInt(), height.toInt(), presentCounts)
  }
  return PresentsAndRegions(presents, regions)
}