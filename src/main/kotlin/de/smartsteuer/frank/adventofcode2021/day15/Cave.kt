package de.smartsteuer.frank.adventofcode2021.day15

import de.smartsteuer.frank.adventofcode2021.lines
import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun main() {
  val cave1 = Cave(lines("/day15/risks.txt"))
  println ("part1: ${measureTimeMillis { println(cave1.lowestRiskPathRisk()) }.toDuration(DurationUnit.MILLISECONDS)}")
  val cave2 = Cave(lines("/day15/risks.txt"), repeat = true)
  println ("part1: ${measureTimeMillis { println(cave2.lowestRiskPathRisk()) }.toDuration(DurationUnit.MILLISECONDS)}")
}

class Cave(private val riskLevels: List<List<Int>>) {
  companion object {
    private fun List<List<Int>>.expand(): List<List<Int>> {
      val expandedRight = map { row -> (1 until 5).fold(row) { acc, step -> acc + row.increasedAndCapped(step) } }
      return (1 until 5).fold(expandedRight) { acc, step -> acc + expandedRight.increased(step) }
    }
    private fun List<List<Int>>.increased(by: Int) = map { row -> row.increasedAndCapped(by) }
    private fun List<Int>.increasedAndCapped(by: Int) = map { level -> (level + by).let { if (it > 9) it - 9 else it } }
  }

  constructor(lines: List<String>, repeat: Boolean = false) : this(lines.map { row -> row.toCharArray().map { it.digitToInt() } }.run {
    if (repeat) expand() else this
  })

  fun lowestRiskPathRisk(): Int {
    val dist = Array(riskLevels.size) { Array(riskLevels.first().size) { Int.MAX_VALUE } }.apply { get(0)[0] = 0 }

    data class Coordinate(val y: Int, val x: Int): Comparable<Coordinate> {
      override fun compareTo(other: Coordinate): Int = dist[y][x] compareTo dist[other.y][other.x]
      fun neighbours(): List<Coordinate> {
        return arrayOf((-1 to 0), (1 to 0), (0 to -1), (0 to 1)).map { (dy, dx) -> Coordinate(y + dy, x + dx) }
          .filter { (x, y) -> x in riskLevels.indices && y in riskLevels.first().indices }
      }
    }

    val toVisit = PriorityQueue<Coordinate>()
    val visited = mutableSetOf(Coordinate(0, 0))
    toVisit.add(Coordinate(0, 0))

    while (toVisit.isNotEmpty()) {
      val coordinate = toVisit.poll().also { visited.add(it) }

      coordinate.neighbours().forEach { (nY, nX) ->
        if (!visited.contains(Coordinate(nY, nX))) {
          val newDistance = dist[coordinate.y][coordinate.x] + riskLevels[nY][nX]
          if (newDistance < dist[nY][nX]) {
            dist[nY][nX] = newDistance
            toVisit.add(Coordinate(nY, nX))
          }
        }
      }
    }

    return dist[dist.lastIndex][dist.last().lastIndex]
  }
}
