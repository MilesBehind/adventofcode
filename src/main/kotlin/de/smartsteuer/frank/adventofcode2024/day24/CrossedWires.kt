package de.smartsteuer.frank.adventofcode2024.day24

import de.smartsteuer.frank.adventofcode2022.day08.takeUntil
import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  CrossedWires.execute(lines("/adventofcode2024/day24/input-and-gates.txt"))
}

object CrossedWires: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseDevice().computeResult()

  override fun part2(input: List<String>): Long {
    TODO("Not yet implemented")
  }

  sealed interface Expression {
    val operand1: String
    val operand2: String
    fun execute(device: Device): Int
  }

  data class And(override val operand1: String, override val operand2: String): Expression {
    override fun execute(device: Device): Int =
      device.compute(operand1) and device.compute(operand2)
  }

  data class Or(override val operand1: String, override val operand2: String): Expression {
    override fun execute(device: Device): Int =
      device.compute(operand1) or device.compute(operand2)
  }

  data class Xor(override val operand1: String, override val operand2: String): Expression {
    override fun execute(device: Device): Int =
      device.compute(operand1) xor device.compute(operand2)
  }

  data class Device(val inputs: Map<String, Int>, val expressions: Map<String, Expression>) {
    fun compute(operand: String): Int =
      inputs[operand] ?: expressions.getValue(operand).execute(this)

    internal fun findVariableNames(start: Char): List<String> =
      (inputs.keys + expressions.keys).filter { it.first() == start }

    private fun combineBits(variableNames: List<String>): Long =
      variableNames
        .sortedDescending()
        .map { compute(it) }
        .fold(0L) { acc, bit -> acc shl 1 or bit.toLong() }

    fun computeExpectedResult(): Long =
      combineBits(findVariableNames('x')) + combineBits(findVariableNames('y'))

    fun computeBits(number: Long, variableStart: Char): Map<String, Int> {
      tailrec fun computeBits(remaining: Long, index: Int, result: MutableMap<String, Int>): Map<String, Int> {
        if (remaining == 0L) return result
        return computeBits(remaining shr 1, index + 1, result.apply { put("$variableStart${"%02d".format(index)}", (remaining and 1).toInt()) })
      }
      return computeBits(number, 0, mutableMapOf())
    }

    fun computeFlippedBitsAndNonFlippedBits(): Pair<Map<String, Int>, Map<String, Int>> {
      val expectedZBits: Map<String, Int> = computeBits(computeExpectedResult(), 'z')
      val computedZBits: Map<String, Int> = findVariableNames('z').associateWith { compute(it) }
      val flippedBits = expectedZBits.entries.fold(mutableMapOf<String, Int>()) { result, (name, value) ->
        result.also {
          if (value != computedZBits[name]) {
            result[name] = computedZBits.getValue(name)
          }
        }
      }
      val nonFlippedBits = expectedZBits - flippedBits.keys
      return Pair(flippedBits, nonFlippedBits)
    }

    private fun findExpressions(output: String): List<Expression> {
      val expression = expressions[output] ?: return emptyList()
      return (findExpressions(expression.operand1) + findExpressions(expression.operand2) + expression)
    }

    fun findExpressionsForFlippedBits(flippedBits: Map<String, Int>): Set<Expression> {
      val expressionsForFlippedBits: Map<String, List<Expression>> = flippedBits.keys.associateWith { output -> findExpressions(output) }
      val expressionSets: List<Set<Expression>> = expressionsForFlippedBits.values.map { it.toSet() }
      return expressionSets.fold(mutableSetOf()) { combined, expressions ->
        combined.apply { addAll(expressions) }
      }
    }

    fun computeResult(): Long =
      combineBits(findVariableNames('z'))
  }

  fun List<String>.parseDevice(): Device {
    val inputs = this.takeUntil { it.isBlank() }.dropLast(1).associate { line ->
      val (name, value) = line.split(": ")
      name to value.toInt()
    }
    val expressions = this.dropWhile { it.isNotBlank() }.drop(1).associate { line ->
      val (op1, expression, op2, result) = line.split(" -> ", " ")
      result to when (expression) {
        "AND" -> And(op1, op2)
        "OR"  -> Or(op1, op2)
        "XOR" -> Xor(op1, op2)
        else  -> throw IllegalArgumentException("Unknown expression: $expression")
      }
    }
    return Device(inputs, expressions)
  }
}