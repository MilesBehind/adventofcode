package de.smartsteuer.frank.adventofcode2022.day09

import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.math.abs
import kotlin.math.sign

fun main() {
  val input = lines("/adventofcode2022/day09/head-movement.txt")
  println("day 09, part 1: ${Day09.part1(input)}")
  println("day 09, part 2: ${Day09.part2(input)}")
}

object Day09 {
  fun part1(input: List<String>): Int {
    val moves = parseHeadMoves(input)
    val rope  = List(2) { Coordinate(0, 0) }
    return processMoves(moves.drop(1), moves.first(), rope, setOf(rope.last())).size
  }

  fun part2(input:List<String>): Int {
    val moves = parseHeadMoves(input)
    val rope  = List(10) { Coordinate(0, 0) }
    return processMoves(moves.drop(1), moves.first(), rope, setOf(rope.last())).size
  }

  private tailrec fun processMoves(moves: List<Move>, move: Move, rope: List<Coordinate>, visited: Set<Coordinate>): Set<Coordinate> {
    if (move.isEmpty()) {
      if (moves.isEmpty()) return visited
      return processMoves(moves.drop(1), moves.first(), rope, visited)
    }
    val (newHead, newMove) = move applyTo (rope.first())
    val ropeAfterHeadMove  = listOf(newHead) + rope.drop(1)
    val newRope            = ropeAfterHeadMove.followHead()
    return processMoves(moves, newMove, newRope, visited + newRope.last())
  }

  enum class Direction(val deltaX: Int, val deltaY: Int) {
    RIGHT(1, 0), LEFT(-1, 0), UP(0, -1), DOWN(0, 1);
    companion object {
      fun parse(name: String): Direction = Direction.values().find { it.name.startsWith(name) } ?: throw IllegalArgumentException("invalid name: $name")
    }
  }

  data class Move(val direction: Direction, val distance: Int) {
    infix fun applyTo(coordinate: Coordinate): Pair<Coordinate, Move> =
      Coordinate(coordinate.x + direction.deltaX, coordinate.y + direction.deltaY) to Move(direction, distance - 1)
    fun isEmpty() = distance == 0
  }

  data class Coordinate(val x: Int, val y: Int) {
    infix fun follow(other: Coordinate): Coordinate =
      Coordinate(other.x - this.x, other.y - this.y).let { delta ->
        if (abs(delta.x) <= 1 && abs(delta.y) <= 1) this else Coordinate(x + delta.x.sign, y + delta.y.sign)
      }
    override fun toString() = "($x, $y)"
  }

  private fun List<Coordinate>.followHead(): List<Coordinate> =
    this.drop(1).fold(listOf(first())) { acc, coordinate -> acc + (coordinate follow acc.last()) }

  private fun parseHeadMoves(input: List<String>): List<Move> =
    input.map { line ->
      line.split(" ").let { (direction, distance) ->
        Move(Direction.parse(direction), distance.toInt())
      }
    }
}