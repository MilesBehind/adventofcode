package de.smartsteuer.frank.adventofcode2022.day21

import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.Sum
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.Subtraction
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.Multiplication
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.Division
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.Literal
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.UnresolvedVariable
import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.Assigment
import de.smartsteuer.frank.adventofcode2022.day21.Day21.part1
import de.smartsteuer.frank.adventofcode2022.day21.Day21.part2
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.system.measureTimeMillis

fun main() {
  val input = lines("/adventofcode2022/day21/monkey-math-expressions.txt")
  measureTimeMillis {
    println("day 21, part 1: ${part1(input)}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 21, part 2: ${part2(input)}")
  }.also { println("took $it ms") }
}

object Day21 {
  fun part1(input: List<String>): Long {
    val solver = Solver(parseExpressions(input))
    return solver.solve()
  }

  fun part2(input: List<String>): Long {
    return 0
  }

  sealed interface Expression {
    fun evaluate(solver: Solver): Long

    data class Sum(val summand1: Expression, val summand2: Expression): Expression {
      override fun evaluate(solver: Solver): Long = summand1.evaluate(solver) + summand2.evaluate(solver)
    }

    data class Subtraction(val minuend: Expression, val subtrahend: Expression): Expression {
      override fun evaluate(solver: Solver): Long = minuend.evaluate(solver) - subtrahend.evaluate(solver)
    }

    data class Multiplication(val factor1: Expression, val factor2: Expression): Expression {
      override fun evaluate(solver: Solver): Long = factor1.evaluate(solver) * factor2.evaluate(solver)
    }

    data class Division(val dividend: Expression, val divisor: Expression): Expression {
      override fun evaluate(solver: Solver): Long = dividend.evaluate(solver) / divisor.evaluate(solver)
    }

    data class Literal(val literal: Long): Expression {
      override fun evaluate(solver: Solver): Long = literal
    }

    data class Assigment(val name: String, val expression: Expression): Expression {
      override fun evaluate(solver: Solver): Long = expression.evaluate(solver)
    }

    data class UnresolvedVariable(val name: String): Expression {
      override fun evaluate(solver: Solver): Long = solver.solve(this)
    }
  }

  data class Solver(val expressions: Map<String, Assigment>) {
    constructor(expressions: List<Assigment>): this(expressions.associateBy { it.name })

    fun solve(): Long = (expressions["root"] ?: throw IllegalStateException("there must be a root expression!")).evaluate(this)

    fun solve(unresolvedVariable: UnresolvedVariable): Long {
      val expression = expressions[unresolvedVariable.name] ?: throw IllegalArgumentException("unknown name '${unresolvedVariable.name}'")
      return expression.evaluate(this)
    }
  }

  fun parseExpressions(input: List<String>): List<Assigment> =
    input.map { line ->
      val (name, expressionString) = line.split(":").map { it.trim() }
      val expression = when {
        '+' in expressionString -> operands(expressionString).let { (left, right) -> Sum           (left, right) }
        '-' in expressionString -> operands(expressionString).let { (left, right) -> Subtraction   (left, right) }
        '*' in expressionString -> operands(expressionString).let { (left, right) -> Multiplication(left, right) }
        '/' in expressionString -> operands(expressionString).let { (left, right) -> Division      (left, right) }
        else                    -> Literal(expressionString.trim().toLong())
      }
      Assigment(name, expression)
    }

  private fun operands(expressionString: String): List<UnresolvedVariable> =
    expressionString.split(' ').map { it.trim() }.let { (left, _, right) -> listOf(UnresolvedVariable(left), UnresolvedVariable(right)) }
}
