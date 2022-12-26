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
    val path = valley.findPath(from = Coordinate(1, 0), to = Coordinate(valley.width - 2, valley.height - 1))
    return path.size - 1
  }

  fun part2(input: List<String>): Int {
    val valley         = parseValley(input)
    val entrance       = Coordinate(1, 0)
    val exit           = Coordinate(valley.width - 2, valley.height - 1)
    val pathThere      = valley.findPath(entrance, exit,     startTime = 0)
    val pathBack       = valley.findPath(exit,     entrance, startTime = pathThere.size - 1)
    val pathThereAgain = valley.findPath(entrance, exit,     startTime = pathThere.size - 1 + pathBack.size - 1)
    return pathThere.size - 1 + pathBack.size - 1 + pathThereAgain.size - 1
  }

  data class Coordinate(val x: Int, val y: Int)

  data class CoordinateXYT(val x: Int, val y: Int, val t: Int) {
    fun neighbours(): List<CoordinateXYT> = neighbourDeltas.map { CoordinateXYT(x + it.x, y + it.y, t + 1) }
    override fun toString() = "(${x}, ${y}, t = ${t})"

    companion object {
      private val neighbourDeltas = setOf(North.delta, South.delta, West.delta, East.delta, Coordinate(0, 0))
    }
  }

  data class Edge3D(val from: CoordinateXYT, val to: CoordinateXYT): GraphAlgorithms.Edge<CoordinateXYT> {
    override fun from()     = from
    override fun to()       = to
    override fun distance() = 1
    override fun toString() = "$from --> $to"
  }

  enum class Direction(val delta: Coordinate, val symbol: Char) {
    North(Coordinate(0, -1), '^'),
    South(Coordinate(0, +1), 'v'),
    West (Coordinate(-1, 0), '<'),
    East (Coordinate(+1, 0), '>')
  }

  data class Valley(val walls: Set<Coordinate>, val blizzards: Map<Coordinate, Direction>, val width: Int, val height: Int) {
    private val blizzardPositions = blizzards.keys

    fun findPath(from: Coordinate, to: Coordinate, startTime: Int = 0): List<Coordinate> {
      val valley3D = toValley3D(startTime)
      val path     = valley3D.findPath(from, to)
      return path.map { (x, y, _) -> Coordinate(x, y) }
    }

    fun toValley3D(startTime: Int, endTime: Int = startTime + (width - 2 + height - 2) * 3): Valley3D {  // endTime computation is simple heuristic
      println("toValley3D()...")
      val nodes = createNodes(startTime, endTime)
      val edges = createEdges(nodes, startTime, endTime)
      return Valley3D(nodes, edges, width, height, startTime)
    }

    private fun createNodes(startTime: Int, endTime: Int): MutableSet<CoordinateXYT> {
      val free = mutableSetOf<CoordinateXYT>()
      (startTime until endTime).forEach { time ->
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
      return free
    }

    private fun createEdges(nodes: Set<CoordinateXYT>, startTime: Int, endTime: Int): Set<Edge3D> {
      val edges = mutableSetOf<Edge3D>()
      (startTime..endTime).forEach { time ->
        val nodesAtCurrentTime = nodes.filter { (_, _, t) -> t == time }.toSet()
        nodesAtCurrentTime.forEach { node ->
          node.neighbours().filter { neighbour -> neighbour in nodes }.forEach { neighbour ->
            edges += Edge3D(node, neighbour)
          }
        }
      }
      return edges
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

  data class Valley3D(val nodes: Set<CoordinateXYT>, val edges: Set<Edge3D>, val width: Int, val height: Int, val startTime: Int) {
    fun findPath(from: Coordinate, to: Coordinate): List<CoordinateXYT> {
      val entrance = CoordinateXYT(from.x, from.y, startTime)
      println("dijkstra...")
      val shortestPathTree: Map<CoordinateXYT, CoordinateXYT> = GraphAlgorithms.dijkstraMutable(entrance, edges, biDirectionalEdges = false)
      val possibleEnds = shortestPathTree.keys.filter { (x, y, _) -> x == to.x && y == to.y }
      val earliestEnd = possibleEnds.minBy { it.t }
      return GraphAlgorithms.shortestPath(shortestPathTree, entrance, earliestEnd)
    }

    fun toValley(time: Int) =
      Valley(nodes.filter { (_, _, t) -> t == time }.map { (x, y, _) -> Coordinate(x, y) }.toSet(), emptyMap(), width, height)
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
