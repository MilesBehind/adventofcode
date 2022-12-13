package de.smartsteuer.frank.adventofcode2022.day12

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val input = lines("/adventofcode2022/day12/height-map.txt")
  println("day 12, part 1: ${Day12.part1(input)}")
  println("day 12, part 2: ${Day12.part2(input)}")
}

@Suppress("SimplifiableCallChain")
object Day12 {
  fun part1(input: List<String>): Int {
    val heightMap        = parseHeightMap(input)
    val shortestPathTree = heightMap.dijkstra(heightMap.end)
    val path             = heightMap.shortestPath(shortestPathTree, heightMap.start, heightMap. end)
    return path.size - 1
  }

  fun part2(input: List<String>): Int {
    val heightMap        = parseHeightMap(input)
    val shortestPathTree = heightMap.dijkstra(heightMap.end)
    val starts           = heightMap.findPossibleStartCoordinates()
    val paths = starts.map { start -> heightMap.shortestPath(shortestPathTree, start, heightMap. end) }
    return paths.map { it.size - 1 }.min()
  }

  @Suppress("DuplicatedCode")
  data class HeightMap(val map: Map<Coordinate, Int>, val start: Coordinate, val end: Coordinate) {
    private fun findValidNeighbours(coordinate: Coordinate) =
      coordinate.neighbours()
        .filter { neighbour -> neighbour in map.keys }
        .filter { neighbour -> (map[coordinate] ?: 0) - (map[neighbour] ?: 0) <= 1
    }

    fun findPossibleStartCoordinates(): List<Coordinate> =
      map.keys
        .filter { coordinate -> map[coordinate] == 0 }
        .filter { coordinate ->
          coordinate.neighbours()
            .filter { neighbour -> neighbour in map.keys }
            .any    { neighbour -> (map[neighbour] ?: 0) - (map[coordinate] ?: 0) == 1 }
        }

    data class State(val result: Map<Coordinate, Int>, val predecessors: Map<Coordinate, Coordinate>)

    fun dijkstra(end: Coordinate): Map<Coordinate, Coordinate> {
      tailrec fun go(active: Set<Coordinate>, state: State): State {
        if (active.isEmpty()) return state
        val node = active.minBy { state.result.getValue(it) }
        val cost = state.result.getValue(node)
        val neighboursAndCosts = findValidNeighbours(node)
          .filter { neighbour -> cost + 1 < state.result.getOrDefault(neighbour, Int.MAX_VALUE) }
          .associateWith { cost + 1 }
        val active1 = active - node + neighboursAndCosts.keys
        val predecessors = neighboursAndCosts.mapValues { node }
        return go(active1, State(state.result + neighboursAndCosts, state.predecessors + predecessors))
      }
      return go(setOf(end), State(mapOf(end to 0), emptyMap())).predecessors
    }

    fun shortestPath(shortestPathTree: Map<Coordinate, Coordinate>, start: Coordinate, end: Coordinate): List<Coordinate> {
      tailrec fun pathTo(start: Coordinate, end: Coordinate, result: List<Coordinate>): List<Coordinate> =
        if (shortestPathTree[end] == null) result + listOf(end) else pathTo(start, shortestPathTree[end]!!, result + listOf(end))
      return pathTo(end, start, emptyList())
    }
  }

  fun parseHeightMap(input: List<String>): HeightMap {
    val heights = input.flatMapIndexed { y, line ->
      line.mapIndexed { x, char ->
        Coordinate(x, y) to char
      }
    }
    val start = findCoordinate(heights, 'S')
    val end   = findCoordinate(heights, 'E')
    val map   = heights.toMap().mapValues { (_, char) ->
      when (char) {
        'S'  -> 'a'
        'E'  -> 'z'
        else -> char
      }.code - 'a'.code
    }
    return HeightMap(map, start, end)
  }

  private fun findCoordinate(heights: List<Pair<Coordinate, Char>>, toFind: Char) =
    heights.find { (_, char) -> char == toFind }?.first ?: throw IllegalArgumentException("could not find '$toFind'")

  data class Coordinate(val x: Int, val y: Int) {
    fun neighbours() = listOf(Coordinate(x - 1, y), Coordinate(x + 1, y), Coordinate(x, y - 1), Coordinate(x, y + 1))
  }
}
