package de.smartsteuer.frank.adventofcode2024.day07

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  BridgeRepair.execute(lines("/adventofcode2024/day07/calibration-equations.txt"))
}

object BridgeRepair: Day {

  override fun part1(input: List<String>): Long =
    input.parseEquations().filter { equation ->
      equation.canBeSolved(listOf(Plus, Times))
    }.sumOf { it.result }

  override fun part2(input: List<String>): Long =
    input.parseEquations().filter { equation ->
      equation.canBeSolved(listOf(Plus, Times, Concatenate))
    }.sumOf { it.result }

  data class Equation(val result: Long, val operands: List<Long>) {
    fun canBeSolved(operators: List<Operator>, index: Int = 1, intermediateResult: Long = operands.first()): Boolean =
      index == operands.size && intermediateResult == result || index < operands.size && operators.any { operator ->
        val newIntermediateResult = operator.apply(intermediateResult, operands[index])
        newIntermediateResult <= result && canBeSolved(operators, index + 1, newIntermediateResult)
      }
  }

  sealed interface Operator {
    fun apply(operand1: Long, operand2: Long): Long
  }
  data object Plus : Operator {
    override fun apply(operand1: Long, operand2: Long): Long = operand1 + operand2
  }
  data object Times : Operator {
    override fun apply(operand1: Long, operand2: Long): Long = operand1 * operand2
  }
  data object Concatenate : Operator {
    override fun apply(operand1: Long, operand2: Long): Long = (operand1.toString() + operand2.toString()).toLong()
  }

  fun List<String>.parseEquations(): List<Equation> =
    map { line ->
      val (resultString, operandsString) = line.split(":")
      Equation(resultString.toLong(), operandsString.trim().split(" ").map { it.toLong() })
    }
}
