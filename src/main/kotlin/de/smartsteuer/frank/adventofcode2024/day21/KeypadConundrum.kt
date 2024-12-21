package de.smartsteuer.frank.adventofcode2024.day21

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines
import kotlin.io.path.fileVisitor

fun main() {
  KeypadConundrum.execute(lines("/adventofcode2024/day21/codes.txt"))
}

object KeypadConundrum: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseCodes().computeMovements().map { (code, keySequenceLength) -> code.dropLast(1).dropWhile { it == '0' }.toLong() * keySequenceLength }.sum()

  override fun part2(input: List<String>): Long {
    TODO("Not yet implemented")
  }

  data class Pos(val x: Int, val y: Int) {
    companion object {
      val neighbours = listOf(Pos(-1, 0), Pos(0, -1), Pos(1, 0), Pos(0, 1))
    }
    operator fun plus(other: Pos) = Pos(x + other.x, y + other.y)
    fun neighbours() = listOf(Pos(x - 1, y), Pos(x, y - 1), Pos(x + 1, y), Pos(x, y + 1))
    fun movement(other: Pos) = Movement(other.x - x, other.y - y)
  }

  data class Movement(val dx: Int, val dy: Int)

  sealed class KeyPad(private val keyMap: Map<Char, Pos>) {
    private val forbiddenPosition = keyMap.getValue(' ')
    private val keysToControlKeySequences: Map<Pair<Char, Char>, Set<String>>
    private val positionsToKeys: Map<Pos, Char>

    init {
      val movementsToKeys = mapOf(Movement(-1,  0) to '<',
                                  Movement( 1,  0) to '>',
                                  Movement( 0, -1) to '^',
                                  Movement( 0,  1) to 'v')
      fun movementsToKeys(movements: List<Movement>): String =
        (movements.mapNotNull { movementsToKeys[it] } + 'A').joinToString("")

      val positions = keyMap.values.toSet() - forbiddenPosition

      positionsToKeys = keyMap.entries.associateBy({ it.value }) { it.key }

      fun findShortestPaths(start: Pos, end: Pos, visited: Set<Pos> = emptySet(), path: List<Pos> = emptyList()): List<List<Pos>> {
        if (start !in positions || start in visited) return emptyList()
        val newPath = path + start
        if (start == end) return listOf(newPath)
        val newVisited = visited + start
        val directions = Pos.neighbours
        val paths = directions.flatMap { findShortestPaths(start + it, end, newVisited, newPath) }
        val minLength = paths.minOfOrNull { it.size } ?: Int.MAX_VALUE
        return paths.filter { it.size == minLength }
      }

      val positionsToPaths: Map<Pair<Pos, Pos>, Set<List<Pos>>> = keyMap.values.flatMap { from ->
        keyMap.values.map { to ->
          (from to to) to findShortestPaths(from, to).toSet()
        }
      }.toMap()

      keysToControlKeySequences = positionsToPaths.map { entry ->
        val fromAndTo = positionsToKeys.getValue(entry.key.first) to positionsToKeys.getValue(entry.key.second)
        val sequences = entry.value.mapTo(mutableSetOf()) { sequence ->
          movementsToKeys(sequence.zipWithNext().map { (from, to) -> from.movement(to) })
        }
        fromAndTo to sequences
      }.toMap()
    }

    fun findKeySequences(from: Char, to: Char): Set<String> = keysToControlKeySequences[from to to] ?: emptySet()

    companion object {
      fun List<String>.toKeyMap(): Map<Char, Pos> =
        this.flatMapIndexed { y, line -> line.mapIndexed { x, char -> char to Pos(x, y) } }.toMap()
    }
  }

  data object NumberKeypad: KeyPad(listOf("789", "456", "123", " 0A").toKeyMap())

  data object DirectionalKeypad: KeyPad(listOf(" ^A", "<v>").toKeyMap())

  data class Robot(private val pos: Char, private val keyPad: KeyPad) {
    fun computeKeys(keys: String): Set<String> {
      tailrec fun computeKeys(keys: String, keySequences: List<Pair<Char, String>>): Set<String> {
        //println("compute keys($keys, $keySequences)")
        if (keys.isEmpty()) return keySequences.map { it.second }.toSet()
        val key = keys.first()
        val sequencesForNextKey: List<Pair<Char, String>> = keySequences.map { (pos, keySequence) ->
          keyPad.findKeySequences(pos, key).map { keySequence + it }
        }.flatten().map { key to it }
        return computeKeys(keys.drop(1), sequencesForNextKey)
      }
      return computeKeys(keys, listOf(pos to ""))
    }
  }

  data class Codes(val codes: List<String>) {

    fun computeMovements(): Map<String, Long> {
      tailrec fun computeMovements(codes: List<String>, result: Map<String, Long>): Map<String, Long> {
        if (codes.isEmpty()) return result
        val code = codes.first()
        val keySequence = computeMovements(code)
        val nextResult = result + (code to keySequence.length.toLong())
        return computeMovements(codes.drop(1), nextResult)
      }
      return computeMovements(codes, emptyMap())
    }

    private fun computeMovements(code: String): String {
      println("code: $code")
      tailrec fun computeShortestSequence(remainingRobots: Int, keySequences: Set<String>): String {
        if (remainingRobots == 0) return keySequences.minBy { it.length }



        val newKeySequences: Set<String> = keySequences.flatMap { sequence -> Robot('A', DirectionalKeypad).computeKeys(sequence) }.toSet()
        val shortestLength: Int = newKeySequences.minOf { it.length }
        val shortestKeySequences: Set<String> = newKeySequences.filter { it.length == shortestLength }.toSet()
        println("remaining: $remainingRobots, shortestLength: $shortestLength, count: ${shortestKeySequences.size}")
        return computeShortestSequence(remainingRobots - 1, shortestKeySequences)
      }
      val keySequencesAfterFirstRobot: Set<String>  = Robot('A', NumberKeypad).computeKeys(code)
      return computeShortestSequence(2, keySequencesAfterFirstRobot)
    }
  }

  internal fun List<String>.parseCodes(): Codes =
    Codes(this)
}
