package de.smartsteuer.frank.adventofcode2021.day15

import de.smartsteuer.frank.adventofcode2021.lines
import java.time.Duration

fun main() {
  val cave = Cave(lines("/day15/risks.txt"))
  val riskOfShortestPath = cave.riskLevelOfShortestPath()
  println("risk of shortest path = $riskOfShortestPath")

  val cave2 = Cave(lines("/day15/risks.txt"), 2, 2)
  val riskOfShortestPath2 = cave2.riskLevelOfShortestPath()
  println("risk of shortest path = $riskOfShortestPath2")

  val cave3 = Cave(lines("/day15/risks.txt"), 3, 3)
  val riskOfShortestPath3 = cave3.riskLevelOfShortestPath()
  println("risk of shortest path = $riskOfShortestPath3")
}

class Cave private constructor(private val risks: Map<Coordinate, Int>) {
  private val defaultRisk = 100

  constructor(lines: List<String>) : this(lines.mapIndexed { y, line ->
    line.mapIndexed { x, char -> Coordinate(x, y) to char.digitToInt() }
  }.flatten().toMap())

  constructor(lines: List<String>, repeatX: Int, repeatY: Int = repeatX) : this(lines.mapIndexed { y, line ->
    line.mapIndexed { x, char -> Coordinate(x, y) to char.digitToInt() }
  }.flatten().toMap().repeat(lines.first().length, lines.size, repeatX, repeatY))

  fun riskAt(x: Int, y: Int): Int = risks[Coordinate(x, y)] ?: defaultRisk
  fun riskAt(coordinate: Coordinate): Int = risks[coordinate] ?: defaultRisk
  fun maximumCoordinate(): Coordinate = risks.keys.maxByOrNull { it.x * it.y } ?: error("could not find max coordinate")

  fun riskLevelOfShortestPath(): Int = Path().shortestPath().drop(1).sumOf { riskAt(it) }

  data class Coordinate(val x: Int, val y: Int)

  inner class Path(private val startCoordinate: Coordinate = Coordinate(0, 0),
                   private val targetCoordinate: Coordinate = maximumCoordinate()) {

    private val shortestPathTree = computeShortestPathTree()

    private fun computeShortestPathTree(): Map<Coordinate, Coordinate?> {
        val start = System.currentTimeMillis()
        val result = dijkstra()
        println("dijkstra took ${Duration.ofMillis(System.currentTimeMillis() - start)} for ${String.format("%,d", risks.size)} nodes")
        return result
      }

    fun next(coordinate: Coordinate): Map<Coordinate, Int> = with(coordinate) {
      listOf(Coordinate(x, y - 1), Coordinate(x + 1, y), Coordinate(x, y + 1))  //, Coordinate(x - 1, y)
        .associateWith { riskAt(it) }
        .filter { it.isNotBorder() }
    }

    private fun Map.Entry<Coordinate, Int>.isNotBorder() = this.value <= 9

    private fun dijkstra(): Map<Coordinate, Coordinate?> {
      val finishedCoordinates: MutableSet<Coordinate> = mutableSetOf() // a subset of vertices, for which we know the true distance

      val delta: MutableMap<Coordinate, Int> = risks.keys
        .associateWith { Int.MAX_VALUE }
        .toMutableMap()
        .apply { this[startCoordinate] = 0 }

      val previous: MutableMap<Coordinate, Coordinate?> = risks.keys.associateWith { null }.toMutableMap()

      while (finishedCoordinates != risks.keys) {
        val coordinate: Coordinate = delta
          .filter { it.key !in finishedCoordinates }
          .minByOrNull { it.value }!!
          .key

        (next(coordinate) - finishedCoordinates).forEach { neighbour: Map.Entry<Coordinate, Int> ->
          val newPath = delta.getValue(coordinate) + neighbour.value

          if (newPath < delta.getValue(neighbour.key)) {
            delta[neighbour.key] = newPath
            previous[neighbour.key] = coordinate
          }
        }
        finishedCoordinates.add(coordinate)
      }

      return previous.toMap()
    }

    fun shortestPath(): List<Coordinate> {
      tailrec fun pathTo(start: Coordinate, end: Coordinate, result: List<Coordinate>): List<Coordinate> {
        if (shortestPathTree[end] == null) return listOf(start) + result
        return pathTo(start, shortestPathTree[end]!!, listOf(end) + result)
      }
      return pathTo(startCoordinate, targetCoordinate, emptyList())
    }
  }
}


private fun Map<Cave.Coordinate, Int>.repeat(sizeX: Int, sizeY: Int, repeatX: Int, repeatY: Int): Map<Cave.Coordinate, Int> {
  val result = mutableMapOf<Cave.Coordinate, Int>()
  (0 until sizeY).forEach { y ->
    (0 until repeatY).forEach { offsetY ->
      (0 until sizeX).forEach { x ->
        (0 until repeatX).forEach { offsetX ->
          val value = ((this[Cave.Coordinate(x, y)] ?: 100) + offsetX + offsetY - 1) % 9 + 1
          result[Cave.Coordinate((x + offsetX * sizeX), (y + offsetY * sizeY))] = value
        }
      }
    }
  }
  return result
}

internal fun Int.wrap(range: IntRange) = (this - range.first) % range.last + range.first
