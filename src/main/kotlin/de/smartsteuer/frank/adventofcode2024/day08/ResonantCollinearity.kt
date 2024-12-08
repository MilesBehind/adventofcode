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
    input.parseMap().findAllAntiNodes(harmonics = true).size.toLong()

  data class Pos(val x: Int, val y: Int) {
    operator fun plus(other: Pos):  Pos = Pos(x + other.x, y + other.y)
    operator fun minus(other: Pos): Pos = Pos(x - other.x, y - other.y)
  }

  data class AntennaMap(val antennas: Map<Pos, Char>, val width: Int, val height: Int) {
    operator fun contains(pos: Pos) = pos.x in 0 until width && pos.y in 0 until height

    fun findAntennasOfSameFrequencies(): List<List<Pos>> =
      antennas.entries.groupBy({ it.value }, { it.key }).values.toList()

    fun antiNodes(antenna1: Pos, antenna2: Pos, harmonics: Boolean): Set<Pos> =
      (antenna1 - antenna2).let { gap ->
        if (harmonics) generateSequence(antenna2) { pos -> (pos + gap).nullIf { it !in this } } +
                       generateSequence(antenna1) { pos -> (pos - gap).nullIf { it !in this } }
        else sequenceOf(antenna1 + gap, antenna2 - gap).filter { it in this }
      }.toSet()

    fun findAllAntiNodes(harmonics: Boolean = false): Set<Pos> =
      findAntennasOfSameFrequencies().flatMap { antennas: List<Pos> ->
        antennas.pickTwo().flatMap { (antenna1, antenna2) ->
          antiNodes(antenna1, antenna2, harmonics)
        }
      }.toSet()
  }

  fun <T> List<T>.pickTwo(): List<Pair<T, T>> =
    this.flatMapIndexed { index, value -> this.indices.drop(index + 1).map { value to this[it] } }

  fun <T> T.nullIf(predicate: (T) -> Boolean) = if (predicate(this)) this else null

  fun List<String>.parseMap(): AntennaMap =
    AntennaMap(flatMapIndexed { y, line ->
      line.mapIndexed { x, c ->
        Pos(x, y) to c
      }
    }.filter { it.second != '.'}.toMap(), first().length, size)
}
