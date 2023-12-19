package de.smartsteuer.frank.adventofcode2023.day18

import de.smartsteuer.frank.adventofcode2022.day04.size
import de.smartsteuer.frank.adventofcode2023.day18.Direction.*
import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val digPlan = parseDigPlan(lines("/adventofcode2023/day18/dig-plan.txt"))
  measureTime { println("part 1: ${part1(digPlan)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(digPlan)}") }.also { println("part 2 took $it") }
}

internal fun part1(digPlan: DigPlan): Long =
  digPlan.computeArea()

internal fun part2(digPlan: DigPlan): Long =
  digPlan.convertColors().computeArea()

internal data class Pos(val x: Int, val y: Int) {
  operator fun plus(pos: Pos) = Pos(x + pos.x, y + pos.y)
  operator fun times(multiplier: Int) = Pos(x * multiplier, y * multiplier)
}

internal data class DigPlan(val operations: List<DigOperation>) {
  fun computeArea(): Long =
    fill().values.sumOf { ranges -> ranges.union().sumOf { range -> range.size }.toLong() }

  fun convertColors(): DigPlan =
    DigPlan(operations.map { it.convertColor() } )

  fun fill(): Map<Int, List<IntRange>> {
    val operations      = computePositionedDigOperations()
    val upOperations    = operations.filter { it.direction == UP }
    val downOperations  = operations.filter { it.direction == DOWN }
    val leftOperations  = operations.filter { it.direction == LEFT }
    val rightOperations = operations.filter { it.direction == RIGHT }
    return buildMap<Int, MutableList<IntRange>> {
      leftOperations.forEach { operation ->
        getOrPut(operation.start.y) { mutableListOf() }.add((operation.start.x - operation.length)..operation.start.x)
      }
      rightOperations.forEach { operation ->
        getOrPut(operation.start.y) { mutableListOf() }.add(operation.start.x..(operation.start.x + operation.length))
      }
      upOperations.forEach { upOperation ->
        val x = upOperation.start.x
        (upOperation.start.y downTo (upOperation.start.y - upOperation.length)).forEach { y ->
          val downOperation = downOperations.filter { it.start.x > x && it.start.y <= y && it.start.y + it.length >= y }.minBy { it.start.x }
          getOrPut(y) { mutableListOf() }.add(x..downOperation.start.x)
        }
      }
    }
  }

  fun computePositionedDigOperations(): List<PositionedDigOperation> =
    operations.fold(Pos(0, 0) to emptyList<PositionedDigOperation>()) { (pos, acc), operation ->
      (pos + operation.direction.delta * operation.length) to (acc + PositionedDigOperation(operation.direction, pos, operation.length, operation.color))
    }.second
}

internal enum class Direction(val delta: Pos) {
  UP   (Pos( 0, -1)),
  RIGHT(Pos( 1,  0)),
  DOWN (Pos( 0,  1)),
  LEFT (Pos(-1,  0))
}

internal data class DigOperation(val direction: Direction, val length: Int, val color: Int) {
  fun convertColor(): DigOperation =
    DigOperation(listOf(RIGHT, DOWN, LEFT, UP)[color and 0xF], color shr 4, color)
}

internal data class PositionedDigOperation(val direction: Direction, val start: Pos, val length: Int, val color: Int)

internal fun List<IntRange>.union(): List<IntRange> {
  return sortedBy { it.first }.let { sorted ->
    sorted.drop(1).fold(listOf(sorted.first())) { acc, range ->
      if (range.first > acc.last().last) acc + listOf(range) else acc.dropLast(1) + listOf((acc.last().first..range.last))
    }
  }
}

internal fun parseDigPlan(lines: List<String>): DigPlan =
  DigPlan(lines.map { line ->
    val (directionCode, length, color) = line.split(" ")
    val direction = when (directionCode.first()) {
      'U'  -> UP
      'R'  -> RIGHT
      'D'  -> DOWN
      'L'  -> LEFT
      else -> throw IllegalArgumentException("invalid direction: '$directionCode'")
    }
    DigOperation(direction, length.toInt(), color.drop(2).dropLast(1).toInt(16))
  })
