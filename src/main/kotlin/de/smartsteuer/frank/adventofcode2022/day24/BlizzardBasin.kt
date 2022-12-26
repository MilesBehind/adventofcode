package de.smartsteuer.frank.adventofcode2022.day24

import de.smartsteuer.frank.adventofcode2022.GraphAlgorithms
import de.smartsteuer.frank.adventofcode2022.day24.Day24.Direction.*
import de.smartsteuer.frank.adventofcode2022.day24.Day24.part1
import de.smartsteuer.frank.adventofcode2022.day24.Day24.part2
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.system.measureTimeMillis

fun main() {
  val input = lines("/adventofcode2022/day24/valley.txt")
  measureTimeMillis {
    println("day 24, part 1: ${part1(input)}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 24, part 2: ${part2(input)}")
  }.also { println("took $it ms") }
}

object Day24 {
  fun part1(input: List<String>): Int {
    val valley = parseValley(input)
    val path = valley.findPath()
    return path.size - 1
  }

  fun part2(input: List<String>): Int {
    return 0
  }

  data class Coordinate(val x: Int, val y: Int)

  operator fun Int.times(coordinate: Coordinate) = Coordinate(this * coordinate.x, this * coordinate.y)

  data class CoordinateXYT(val x: Int, val y: Int, val t: Int) {
    fun neighbours(): List<CoordinateXYT> = neighbourDeltas.map { CoordinateXYT(x + it.x, y + it.y, t + 1) }
    override fun toString() = "(${x}, ${y}, t = ${t})"

    companion object {
      private val neighbourDeltas = setOf(North.delta, South.delta, West.delta, East.delta, Coordinate(0, 0))
    }
  }

  enum class Direction(val delta: Coordinate, val symbol: Char) {
    North(Coordinate(0, -1), '^'),
    South(Coordinate(0, +1), 'v'),
    West (Coordinate(-1, 0), '<'),
    East (Coordinate(+1, 0), '>')
  }

  data class Valley(val walls: Set<Coordinate>, val blizzards: Map<Coordinate, Direction>, val width: Int, val height: Int) {
    private val blizzardPositions = blizzards.keys

    fun findPath(): List<Coordinate> {
      val valley3D = toValley3D()
      val path = valley3D.findPath()
      //for (t in path.indices) {
      //  println("time = $t => move to ${path[t]}:")
      //  println(valley3D.toValley(t))
      //}
      return path.map { (x, y, _) -> Coordinate(x, y) }
    }

    fun toValley3D(): Valley3D {
      println("toValley3D()...")
      val duration = (width - 2 + height - 2) * 3  // just a heuristic, this should be enough time to go from entrance to exit
      val free = mutableSetOf<CoordinateXYT>()
      (0 until duration).forEach { time ->
        val blizzards3D = blizzards.map { (position, direction) -> blizzardPosition(position, direction, time) }.toSet()
        val walls3D     = walls.map { (x, y) -> CoordinateXYT(x, y, time) }.toSet()
        val blocked     = blizzards3D + walls3D
        (0 until height).forEach { y ->
          (0 until width).forEach { x ->
            val freeNode = CoordinateXYT(x, y, time)
            if (freeNode !in blocked) {
              free += freeNode
            }
          }
        }
      }
      return Valley3D(free, width, height, duration)
    }

    fun blizzardPosition(blizzardPosition: Coordinate, direction: Direction, time: Int): CoordinateXYT {
      val x = blizzardPosition.x + time * direction.delta.x
      val y = blizzardPosition.y + time * direction.delta.y
      return CoordinateXYT((x - 1).mod(width - 2) + 1, (y - 1).mod(height - 2) + 1, time)
    }

    override fun toString() = buildString {
      (0 until height).forEach { y ->
        (0 until width).forEach { x ->
          when (val position = Coordinate(x, y)) {
            in walls             -> append('#')
            in blizzardPositions -> append(blizzards[position]?.symbol)
            else                 -> append('.')
          }
        }
        appendLine()
      }
    }
  }

  data class Edge3D(val from: CoordinateXYT, val to: CoordinateXYT): GraphAlgorithms.Edge<CoordinateXYT> {
    override fun from()     = from
    override fun to()       = to
    override fun distance() = 1
    override fun toString() = "$from --> $to"
  }

  data class Valley3D(val freeCells: Set<CoordinateXYT>, val width: Int, val height: Int, val duration: Int) {
    private val edges = createAllEdges(0, freeCells.filter { (_, _, t) -> t == 0 }.toSet(), emptySet()).also { println("created all 3d edges (${it.size})") }

    private tailrec fun createAllEdges(time: Int, freeCellsAtCurrentTime: Set<CoordinateXYT>, edges: Set<Edge3D>): Set<Edge3D> {
      if (time == duration) return edges
      val freeCellsAtNextTime            = freeCells.filter { (_, _, t) -> t == time + 1 }.toSet()
      val edgesFromCurrentTimeToNextTime = createEdgesBetweenTimes(freeCellsAtCurrentTime, freeCellsAtNextTime)
      return createAllEdges(time + 1, freeCellsAtNextTime, edges + edgesFromCurrentTimeToNextTime)
    }

    private fun createEdgesBetweenTimes(freeCellsAtCurrentTime: Set<CoordinateXYT>, freeCellsAtNextTime: Set<CoordinateXYT>): Set<Edge3D> =
      freeCellsAtCurrentTime.flatMap { freeCell ->
        freeCell.neighbours().filter { neighbour -> neighbour in freeCellsAtNextTime }.map { neighbour -> Edge3D(freeCell, neighbour) }
      }.toSet()

    fun findPath(): List<CoordinateXYT> {
      val entrance = CoordinateXYT(1, 0, 0)
      val exit     = Coordinate(width - 2, height - 1)
      println("dijkstra...")
      val shortestPathTree: Map<CoordinateXYT, CoordinateXYT> = GraphAlgorithms.dijkstraMutable(entrance, edges, biDirectionalEdges = false)
      //println("shortestPathTree = $shortestPathTree")
      val possibleEnds = shortestPathTree.keys.filter { (x, y, _) ->  x == exit.x && y == exit.y }
      println("possibleEnds = $possibleEnds")
      //possibleEnds.forEach { end ->
      //  val path = GraphAlgorithms.shortestPath(shortestPathTree, entrance, end)
      //  println("path to end at time ${end.t} = $path")
      //}
      val earliestEnd = possibleEnds.minBy { it.t }
      val path = GraphAlgorithms.shortestPath(shortestPathTree, entrance, earliestEnd)
      println("path to end at time ${earliestEnd.t} = $path")
      return path
    }

    fun toValley(time: Int) =
      Valley(freeCells.filter { (_, _, t) -> t == time }.map { (x, y, _) -> Coordinate(x, y) }.toSet(), emptyMap(), width, height)
  }

  fun parseValley(input: List<String>): Valley {
    val walls = input.flatMapIndexed { y, line ->
      line.mapIndexedNotNull { x, char ->
        when (char) {
          '#'  -> Coordinate(x, y)
          else -> null
        }
      }
    }
    val blizzards = input.flatMapIndexed { y, line ->
      line.mapIndexedNotNull { x, char ->
        when (char) {
          '>'  -> Coordinate(x, y) to East
          'v'  -> Coordinate(x, y) to South
          '<'  -> Coordinate(x, y) to West
          '^'  -> Coordinate(x, y) to North
          else -> null
        }
      }
    }
    return Valley(walls.toSet(), blizzards.toMap(), input.first().length, input.size)
  }
}
