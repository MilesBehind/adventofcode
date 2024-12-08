package de.smartsteuer.frank.adventofcode2024.day08

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  ResonantCollinearity.execute(lines("/adventofcode2024/day08/antenna-map.txt"))
}

object ResonantCollinearity: Day {

  override fun part1(input: List<String>): Long =
    input.parseMap().findAllAntiNodes().size.toLong()

  override fun part2(input: List<String>): Long =
    input.parseMap().findAllAntiNodesInLine().size.toLong()

  data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos):  Pos = Pos(x + other.x, y + other.y)
    operator fun minus(other: Pos): Pos = Pos(x - other.x, y - other.y)
  }

  data class AntennaMap(val antennas: Map<Pos, Char>, val width: Int, val height: Int) {
    operator fun contains(pos: Pos) = pos.x in 0 until width && pos.y in 0 until height

    fun findAntennasOfSameFrequencies(): List<List<Pos>> =
      antennas.keys.groupBy { pos -> antennas.values.first { antennas[pos] == it } }.values.toList()

    fun antiNodes(antenna1: Pos, antenna2: Pos): Pair<Pos, Pos> = (antenna1 - antenna2).let { gap -> (antenna1 + gap) to (antenna2 - gap) }

    fun antiNodesInLine(antenna1: Pos, antenna2: Pos): Set<Pos> =
      (antenna1 - antenna2).let { gap ->
        generateSequence(antenna2) { pos -> (pos + gap).let { newPos -> if (newPos in this) newPos else null } }.toSet() +
        generateSequence(antenna1) { pos -> (pos - gap).let { newPos -> if (newPos in this) newPos else null } }.toSet()
      }

    fun findAllAntiNodes(): Set<Pos> =
      findAntennasOfSameFrequencies().flatMap { antennas: List<Pos> ->
        antennas.pickTwo().flatMap { (antenna1, antenna2) ->
          antiNodes(antenna1, antenna2).toList().filter { pos -> pos in this }
        }
      }.toSet()

    fun findAllAntiNodesInLine(): Set<Pos> =
      findAntennasOfSameFrequencies().flatMap { antennas: List<Pos> ->
        antennas.pickTwo().flatMap { (antenna1, antenna2) ->
          antiNodesInLine(antenna1, antenna2).toList().filter { pos -> pos in this }
        }
      }.toSet()
  }

  fun <T> List<T>.pickTwo(): List<Pair<T, T>> =
    this.flatMapIndexed { index, value -> this.indices.drop(index + 1).map { value to this[it] } }

  fun List<String>.parseMap(): AntennaMap =
    AntennaMap(flatMapIndexed { y, line ->
      line.mapIndexed { x, c ->
        Pos(x, y) to c
      }
    }.filter { it.second != '.'}.toMap(), first().length, size)
}
