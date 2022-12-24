package de.smartsteuer.frank.adventofcode2022.day23

import de.smartsteuer.frank.adventofcode2022.day23.Day23.Direction.*
import de.smartsteuer.frank.adventofcode2022.day23.Day23.part1
import de.smartsteuer.frank.adventofcode2022.day23.Day23.part2
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.system.measureTimeMillis

fun main() {
  val input = lines("/adventofcode2022/day23/ground.txt")
  measureTimeMillis {
    println("day 23, part 1: ${part1(input)}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 23, part 2: ${part2(input)}")
  }.also { println("took $it ms") }
}

object Day23 {
  fun part1(input: List<String>): Int {
    val ground = parseGround(input)
    val finalGround = (0 until 10).fold(ground) { resultingGround, round -> resultingGround.round(round).first }
    println(finalGround)
    return finalGround.coveredEmptyGroundTiles()
  }

  fun part2(input: List<String>): Int {
    val ground = parseGround(input)
    val (finalGround, rounds) = generateSequence(ground to 0) { (currentGround, round) ->
      val (nextGround, moveCount) = currentGround.round(round)
      if (moveCount > 0) nextGround to (round + 1) else null
    }.last()
    println("round ${rounds + 1} =>\n$finalGround")
    return rounds + 1
  }

  data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate) = Coordinate(x + other.x, y + other.y)

    fun neighbours(direction: Direction): List<Coordinate> = direction.neighbourDeltas.map { this + it }
    fun neighbours():                     List<Coordinate> = neighbourDeltas.map           { this + it }

    companion object {
      private val neighbourDeltas = (North.neighbourDeltas + South.neighbourDeltas + West.neighbourDeltas + East.neighbourDeltas).toSet()
    }
  }

  enum class Direction(val neighbourDeltas: List<Coordinate>, val delta: Coordinate) {
    North((-1..1).map { x -> Coordinate(x, -1) }, Coordinate(0, -1)),
    South((-1..1).map { x -> Coordinate(x, +1) }, Coordinate(0, +1)),
    West ((-1..1).map { y -> Coordinate(-1, y) }, Coordinate(-1, 0)),
    East ((-1..1).map { y -> Coordinate(+1, y) }, Coordinate(+1, 0))
  }

  data class Ground(val elves: Set<Coordinate>) {

    fun coveredEmptyGroundTiles(): Int {
      val xRange = elves.minOf { it.x }..elves.maxOf { it.x }
      val yRange = elves.minOf { it.y }..elves.maxOf { it.y }
      return (xRange.last - xRange.first + 1) * (yRange.last - yRange.first + 1) - elves.size
    }

    fun round(round: Int): Pair<Ground, Int> {
      val directionsInOrder            = directions(round)
      val proposedMovePositions        = firstHalfOfRound(directionsInOrder)
      val (newElfPositions, moveCount) = secondHalfOfRound(proposedMovePositions)
      return Ground(newElfPositions) to moveCount
    }

    private fun firstHalfOfRound(directionsInOrder: List<Direction>): Map<Coordinate, Coordinate> {
      val elvesThatShouldMove = elves.filter { elfPosition -> elfPosition.neighbours().any { it in elves } }
      val proposedMoveDeltas: Map<Coordinate, Coordinate?> = elvesThatShouldMove.associateWith { elfPosition ->
        directionsInOrder.firstOrNull { direction ->
          elfPosition.neighbours(direction).none { it in elves }
        }?.delta
      }
      return proposedMoveDeltas.filterValues { it != null }.mapValues { (position, delta) -> position + (delta ?: Coordinate(0, 0)) }
    }

    private fun secondHalfOfRound(proposedMovePositions: Map<Coordinate, Coordinate>): Pair<Set<Coordinate>, Int> {
      val duplicateMovePositions: Set<Coordinate> = proposedMovePositions.values.groupingBy { it }.eachCount().filter { it.value > 1 }.keys
      val nonCollidingMoves = proposedMovePositions.filterValues { moveTo -> moveTo !in duplicateMovePositions }
      return elves.map { elfPosition -> nonCollidingMoves[elfPosition] ?: elfPosition }.toSet() to nonCollidingMoves.size
    }

    private fun directions(round: Int): List<Direction> =
      Direction.values().indices.map { index -> Direction.values()[(index + round) % Direction.values().size] }

    override fun toString() = buildString {
      val xRange = elves.minOf { it.x }..elves.maxOf { it.x }
      val yRange = elves.minOf { it.y }..elves.maxOf { it.y }
      yRange.forEach { y ->
        appendLine(xRange.map { x -> if (Coordinate(x, y) in elves) '#' else '.' }.joinToString(""))
      }
    }
  }

  fun parseGround(input: List<String>): Ground =
    Ground(input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, char ->
              if (char == '#') Coordinate(x, y) else null
            }
          }.toSet())
}
