package de.smartsteuer.frank.adventofcode2023.day10

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val pipeMap = parsePipeMap(lines("/adventofcode2023/day10/pipe-map.txt"))
  measureTime { println("part 1: ${part1(pipeMap)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(pipeMap)}") }.also { println("part 2 took $it") }
}

internal fun part1(pipeMap: PipeMap): Int =
  (pipeMap.computeCycle().size + 1) / 2

internal fun part2(pipeMap: PipeMap): Int =
  pipeMap.computeInnerArea().size

internal data class PipeMap(val start: Tile, val width: Int, val height: Int, val tiles: Map<Pos, Tile>) {
  fun computeCycle(): List<Pos> {
    tailrec fun computeCycle(pos: Pos, visited: MutableSet<Pos>): List<Pos> {
      if (pos == start.pos) return visited.toList()
      val tile = tiles.getValue(pos)
      val next = if (tile.to in visited && tile.to != start.pos) tile.from else tile.to
      return computeCycle(next, visited.apply { add(pos) })
    }
    return computeCycle(start.to, mutableSetOf(start.pos))
  }

  fun computeInnerArea(): Set<Pos> {
    val loop = computeCycle().toSet()
    tailrec fun computeInnerArea(pos: Pos, inside: Boolean, result: Set<Pos>): Set<Pos> {
      if (pos.y >= height) return result
      if (pos.x >= width) return computeInnerArea(Pos(0, pos.y + 1), false, result)
      val isLoopTile  = pos in loop
      val next        = Pos(pos.x + 1, pos.y)
      val isNorthTile = isLoopTile && tiles.getValue(pos).isNorthTile
      return if (isNorthTile) computeInnerArea(next, !inside, result)
             else             computeInnerArea(next, inside,  if (inside && !isLoopTile) result + pos else result)
    }
    return computeInnerArea(Pos(0, 0), false, emptySet())
  }
}

internal data class Tile(val pos: Pos, val from: Pos, val to: Pos) {
  val isNorthTile: Boolean = from.y < pos.y || to.y < pos.y
}

internal data class Pos(val x: Int, val y: Int) {
  fun neighbours() = ((y - 1)..(y + 1)).flatMap { ny -> ((x - 1)..(x + 1)).map { nx -> Pos(nx, ny) } }.toSet() - this
  override fun toString() = "$x/$y"
}

internal fun parsePipeMap(lines: List<String>): PipeMap {
  val width    = lines.first().length
  val height   = lines.size
  val startPos = lines.flatMapIndexed { y, line -> line.mapIndexed { x, c -> if (c == 'S') Pos(x, y) else null } }.filterNotNull().first()
  val tiles    = lines.flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
      val pos = Pos(x, y)
      when (c) {
        '|'  -> Tile(pos, Pos(pos.x,     pos.y - 1), Pos(pos.x,     pos.y + 1))
        '-'  -> Tile(pos, Pos(pos.x - 1, pos.y),     Pos(pos.x + 1, pos.y))
        'L'  -> Tile(pos, Pos(pos.x,     pos.y - 1), Pos(pos.x + 1, pos.y))
        'J'  -> Tile(pos, Pos(pos.x,     pos.y - 1), Pos(pos.x - 1, pos.y))
        '7'  -> Tile(pos, Pos(pos.x,     pos.y + 1), Pos(pos.x - 1, pos.y))
        'F'  -> Tile(pos, Pos(pos.x,     pos.y + 1), Pos(pos.x + 1, pos.y))
        else -> null
      }
    }
  }.filterNotNull().associateBy { it.pos }
  val start = startPos.neighbours()
    .mapNotNull { tiles[it] }
    .filter { neighbourTile -> startPos in setOf(neighbourTile.from, neighbourTile.to) }
    .map { it.pos }
    .let { startNeighbours -> Tile(startPos, startNeighbours.first(), startNeighbours.last()) }
  return PipeMap(start, width, height, tiles + (startPos to start))
}