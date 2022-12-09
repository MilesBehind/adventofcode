package de.smartsteuer.frank.adventofcode2022.day09

import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.math.abs
import kotlin.math.sign

fun main() {
  val input = lines("/adventofcode2022/day09/head-movement.txt")
  println("day 09, part 1: ${Day09.part1(input)}")
}

object Day09 {
  fun part1(input: List<String>): Int {
    tailrec fun processMoves(moves: List<Move>, move: Move, head: Coordinate, tail: Coordinate, visited: Set<Coordinate>): Set<Coordinate> {
      println("processMoves(${moves.size} remaining, $move, $head, $tail, ${visited.size})")
      if (move.isEmpty()) {
        if (moves.isEmpty()) return visited
        return processMoves(moves.drop(1), moves.first(), head, tail, visited)
      }
      val (newHead, newMove) = move applyTo (head)
      println("  applied $move to $head => $newHead")
      val newTail = tail follow newHead
      println("  $tail follow $newHead => $newTail")
      return processMoves(moves, newMove, newHead, newTail, visited + newTail)
    }
    val moves = parseHeadMoves(input)
    val head  = Coordinate(0, 0)
    val tail  = Coordinate(0, 0)
    return processMoves(moves.drop(1), moves.first(), head, tail, setOf(tail)).size
  }

  enum class Direction(val deltaX: Int, val deltaY: Int) {
    RIGHT(1, 0), LEFT(-1, 0), UP(0, -1), DOWN(0, 1);

    companion object {
      fun parse(name: String): Direction = when (name) {
        "R"  -> RIGHT
        "L"  -> LEFT
        "U"  -> UP
        "D"  -> DOWN
        else -> throw IllegalArgumentException("there is no direction with name $name")
      }
    }
  }

  data class Move(val direction: Direction, val distance: Int) {
    infix fun applyTo(coordinate: Coordinate): Pair<Coordinate, Move> =
      Coordinate(coordinate.x + direction.deltaX, coordinate.y + direction.deltaY) to Move(direction, distance - 1)
    fun isEmpty() = distance == 0
  }

  data class Coordinate(val x: Int, val y: Int) {
    infix fun follow(other: Coordinate): Coordinate {
      val deltaX = other.x - this.x
      val deltaY = other.y - this.y
      return when {
        abs(deltaX) <= 1 && abs(deltaY) <= 1 -> this
        abs(deltaX)  < 1 && abs(deltaY) <= 2 -> Coordinate(x,               y + deltaY.sign)
        abs(deltaX) <= 2 && abs(deltaY)  < 1 -> Coordinate(x + deltaX.sign, y)
        else                                 -> Coordinate(x + deltaX.sign, y + deltaY.sign)
      }
    }
  }

  private fun parseHeadMoves(input: List<String>): List<Move> =
    input.map { line ->
      line.split(" ").let { (direction, distance) ->
        Move(Direction.parse(direction), distance.toInt())
      }
    }
}