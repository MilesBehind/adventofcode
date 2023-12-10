package de.smartsteuer.frank.adventofcode2023.day10

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val pipeMap = parsePipeMap(lines("/adventofcode2023/day10/pipe-map.txt"))
  measureTime { println("part 1: ${part1(pipeMap)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(pipeMap)}") }.also { println("part 2 took $it") }
}

internal fun part1(pipeMap: PipeMap): Long {
  val path = pipeMap.computeCycle()
  return (path.size + 1) / 2L
}

internal fun part2(pipeMap: PipeMap): Long {
  return 0
}

internal data class PipeMap(val tiles: Map<Pos, Tile>) {
  private val start: Tile = tiles.values.first { it is Start }
  fun computeCycle(): Set<Pos> {
    tailrec fun computeCycle(pos: Pos, visited: Set<Pos>): Set<Pos> {
      if (pos == start.pos) return visited
      val tile = tiles.getValue(pos)
      val next = if (tile.to in visited && tile.to != start.pos) tile.from else tile.to
      return computeCycle(next, visited + pos)
    }
    return computeCycle(start.to, setOf(start.pos))
  }
}

internal data class Pos(val x: Int, val y: Int) {
  fun neighbours() = ((y - 1)..(y + 1)).flatMap { ny -> ((x - 1)..(x + 1)).map { nx -> Pos(nx, ny) } }.toSet() - this
  override fun toString() = "$x/$y"
}

internal sealed class Tile(val pos: Pos, val from: Pos, val to: Pos) {
  //override fun toString() = "$pos -> $from, $to"
  //override fun equals(other: Any?): Boolean {
  //  if (this === other) return true
  //  if (other !is Tile) return false
  //  if (pos != other.pos) return false
  //  if (from != other.from) return false
  //  if (to != other.to) return false
  //  return true
  //}
//
  //override fun hashCode(): Int {
  //  var result = pos.hashCode()
  //  result = 31 * result + from.hashCode()
  //  result = 31 * result + to.hashCode()
  //  return result
  //}
}
internal class NorthSouth(pos: Pos): Tile(pos, Pos(pos.x,     pos.y - 1), Pos(pos.x,     pos.y + 1))
internal class EastWest  (pos: Pos): Tile(pos, Pos(pos.x - 1, pos.y),     Pos(pos.x + 1, pos.y))
internal class NorthEast (pos: Pos): Tile(pos, Pos(pos.x,     pos.y - 1), Pos(pos.x + 1, pos.y))
internal class NorthWest (pos: Pos): Tile(pos, Pos(pos.x,     pos.y - 1), Pos(pos.x - 1, pos.y))
internal class SouthWest (pos: Pos): Tile(pos, Pos(pos.x,     pos.y + 1), Pos(pos.x - 1, pos.y))
internal class SouthEast (pos: Pos): Tile(pos, Pos(pos.x,     pos.y + 1), Pos(pos.x + 1, pos.y))
internal class Start     (pos: Pos, from: Pos, to: Pos): Tile(pos, from, to)

internal fun parsePipeMap(lines: List<String>): PipeMap {
  val tiles = lines.flatMapIndexed { y, line ->
    line.mapIndexed { x, c ->
      val pos = Pos(x, y)
      when (c) {
        '|'  -> NorthSouth(pos)
        '-'  -> EastWest(pos)
        'L'  -> NorthEast(pos)
        'J'  -> NorthWest(pos)
        '7'  -> SouthWest(pos)
        'F'  -> SouthEast(pos)
        'S'  -> Start(pos, pos, pos)
        else -> null
      }
    }
  }.filterNotNull().associateBy { it.pos }
  return PipeMap(tiles.map { (pos, tile) ->
    when (tile) {
      is Start -> pos.neighbours()
        .mapNotNull { tiles[it] }
        .filter { neighbourTile -> pos in setOf(neighbourTile.from, neighbourTile.to) }
        .map { it.pos }
        .let { startNeighbours -> pos to Start(pos, startNeighbours.first(), startNeighbours.last()) }
      else     -> pos to tile
    }
  }.toMap())
}