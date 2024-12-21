package de.smartsteuer.frank.adventofcode2024.day21

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

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

      fun findPaths(start: Pos, end: Pos, visited: MutableSet<Pos> = mutableSetOf(), currentPath: MutableList<Pos> = mutableListOf()): List<List<Pos>> {
        if (start !in positions || start in visited) return emptyList()
        visited.add(start)
        currentPath.add(start)
        if (start == end) {
          val path = currentPath.toList()
          visited.remove(start)
          currentPath.removeLast()
          return listOf(path)
        }
        val directions = Pos.neighbours
        val paths = mutableListOf<List<Pos>>()
        for (direction in directions) {
          val nextPos = start + direction
          paths.addAll(findPaths(nextPos, end, visited, currentPath))
        }
        visited.remove(start)
        currentPath.removeLast()
        val minLength = paths.minOfOrNull { it.size } ?: Int.MAX_VALUE
        return paths.filter { it.size == minLength }      }

      val positionsToPaths: Map<Pair<Pos, Pos>, Set<List<Pos>>> = keyMap.values.flatMap { from ->
        keyMap.values.map { to ->
          (from to to) to findPaths(from, to).toSet()
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
    private val robot1 = Robot('A', NumberKeypad)
    private val robot2 = Robot('A', DirectionalKeypad)
    private val robot3 = Robot('A', DirectionalKeypad)

    fun computeMovements(): Map<String, Long> {
      tailrec fun computeMovements(robotsAndKeySequence: RobotsAndKeySequence, codes: List<String>, result: Map<String, Long>): Map<String, Long> {
        if (codes.isEmpty()) return result
        val code = codes.first()
        val nextRobotsAndKeySequences = computeMovements(robotsAndKeySequence, code)
        val nextResult = result + (code to nextRobotsAndKeySequences.finalKeySequence.length.toLong())
        return computeMovements(nextRobotsAndKeySequences, codes.drop(1), nextResult)
      }
      return computeMovements(RobotsAndKeySequence(robot1, robot2, robot3, ""),codes, emptyMap())
    }

    data class RobotsAndKeySequence(val robot1: Robot, val robot2: Robot, val robot3: Robot, val finalKeySequence: String)

    private fun computeMovements(robotsAndKeySequence: RobotsAndKeySequence, code: String): RobotsAndKeySequence {
      println("code: $code")
      val (robot1, robot2, robot3, _) = robotsAndKeySequence
      val keySequences1: Set<String>  = robot1.computeKeys(code)
      println("keys for robot 1: ${keySequences1.size}")
      val keySequences2: Set<String>  = keySequences1.flatMap { sequence -> robot2.computeKeys(sequence) }.toSet()
      val shortestLength2 = keySequences2.minOf { it.length }
      val shortestKeySequences2 = keySequences2.filter { it.length == shortestLength2 }
      println("keys for robot 2: ${shortestKeySequences2.size}")
      val keySequences3: Set<String>  = shortestKeySequences2.flatMap { sequence -> robot3.computeKeys(sequence) }.toSet()
      val shortestLength3 = keySequences3.minOf { it.length }
      val shortestKeySequences3 = keySequences3.filter { it.length == shortestLength3 }
      println("keys for robot 3: ${shortestKeySequences3.size}")
      val shortestKeySequence = shortestKeySequences3.minBy { it.length }
      return RobotsAndKeySequence(robot1.copy(pos = shortestKeySequence.last()),
                                  robot2.copy(pos = shortestKeySequence.last()),
                                  robot3.copy(pos = shortestKeySequence.last()),
                                  shortestKeySequence)
    }
  }

  internal fun List<String>.parseCodes(): Codes =
    Codes(this)
}
