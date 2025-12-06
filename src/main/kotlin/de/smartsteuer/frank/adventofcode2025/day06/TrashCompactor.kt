package de.smartsteuer.frank.adventofcode2025.day06

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  TrashCompactor.execute(lines("/adventofcode2025/day06/exercises.txt"))
}

object TrashCompactor: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseMathExercisesPart1().sumOf { it.solve() }

  override fun part2(input: List<String>): Long =
    input.parseMathExercisesPart2().sumOf { it.solve() }
}

typealias Operator = (Long, Long) -> Long
val add = { a: Long, b: Long -> a + b }
val mul = { a: Long, b: Long -> a * b }

data class MathExercise(val operands: List<Long>, val operator: Operator) {
  fun solve(): Long = operands.reduce(operator)
}

internal fun String.parseOperator(): Operator =
  when (this) {
    "+" -> add
    "*" -> mul
    else -> throw IllegalArgumentException("Unknown operator: $this")
  }

internal fun List<String>.parseMathExercisesPart1(): List<MathExercise> {
  val operators: List<Operator> = last().trim().split("""\s+""".toRegex()).map { operand -> operand.parseOperator() }
  val operands = dropLast(1).map { line -> line.trim().split("""\s+""".toRegex()).map { it.toLong() } }
  return operators.mapIndexed { column, operator ->
    MathExercise(operands.map { it[column] }, operator)
  }
}

internal fun List<String>.parseMathExercisesPart2(): List<MathExercise> {
  val separatorColumns = listOf(-1) + first().indices.filter { column -> this.all { it[column] == ' '} } + first().length
  val operatorLine = last()
  val operandLines = dropLast(1)
  return separatorColumns.windowed(2).map { (from, to) ->
    val operator = operatorLine.substring((from + 1) until (from + 2)).parseOperator()
    val operands = ((from + 1) until to).map { column -> operandLines.map { it[column] }.joinToString("").trim().toLong() }
    MathExercise(operands, operator)
  }
}
