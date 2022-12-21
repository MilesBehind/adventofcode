package de.smartsteuer.frank.adventofcode2022.day21

import de.smartsteuer.frank.adventofcode2022.day21.Day21.Expression.BinaryExpression
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

private const val ROOT  = "root"
private const val HUMAN = "humn"

object Day21 {
  fun part1(input: List<String>): Long = Solver(parseExpressions(input)).solveRoot()

  fun part2(input: List<String>): Long = Solver(parseExpressions(input)).solveHuman()

  sealed interface Expression {
    fun evaluate(solver: Solver): Long

    interface BinaryExpression: Expression {
      val left:  Expression
      val right: Expression

      fun replaceExpressions(newLeft: Expression, newRight: Expression): Expression
      fun counterExpression(left: Expression, right: Expression): Expression
      fun isCommutative(): Boolean
    }

    data class Sum(override val left: Expression, override val right: Expression): BinaryExpression {
      override fun evaluate(solver: Solver): Long = left.evaluate(solver) + right.evaluate(solver)
      override fun replaceExpressions(newLeft: Expression, newRight: Expression) = Sum(newLeft, newRight)
      override fun counterExpression(left: Expression, right: Expression) = Subtraction(left, right)
      override fun isCommutative() = true
      override fun toString() = "($left + $right)"
    }

    data class Subtraction(override val left: Expression, override val right: Expression): BinaryExpression {
      override fun evaluate(solver: Solver): Long = left.evaluate(solver) - right.evaluate(solver)
      override fun replaceExpressions(newLeft: Expression, newRight: Expression) = Subtraction(newLeft, newRight)
      override fun counterExpression(left: Expression, right: Expression) = Sum(left, right)
      override fun isCommutative() = false
      override fun toString() = "($left - $right)"
    }

    data class Multiplication(override val left: Expression, override val right: Expression): BinaryExpression {
      override fun evaluate(solver: Solver): Long = left.evaluate(solver) * right.evaluate(solver)
      override fun replaceExpressions(newLeft: Expression, newRight: Expression) = Multiplication(newLeft, newRight)
      override fun counterExpression(left: Expression, right: Expression) = Division(left, right)
      override fun isCommutative() = true
      override fun toString() = "($left * $right)"
    }

    data class Division(override val left: Expression, override val right: Expression): BinaryExpression {
      override fun evaluate(solver: Solver): Long = left.evaluate(solver) / right.evaluate(solver)
      override fun replaceExpressions(newLeft: Expression, newRight: Expression) = Division(newLeft, newRight)
      override fun counterExpression(left: Expression, right: Expression) = Multiplication(left, right)
      override fun isCommutative() = false
      override fun toString() = "($left / $right)"
    }

    data class Literal(val literal: Long): Expression {
      override fun evaluate(solver: Solver): Long = literal
      override fun toString() = literal.toString()
    }

    data class Assigment(val name: String, val expression: Expression): Expression {
      override fun evaluate(solver: Solver): Long = expression.evaluate(solver)
    }

    data class UnresolvedVariable(val name: String): Expression {
      override fun evaluate(solver: Solver): Long = solver.solve(this)
      override fun toString() = name
    }
  }

  data class Solver(val expressions: Map<String, Assigment>) {
    constructor(expressions: List<Assigment>): this(expressions.associateBy { it.name })

    fun solveRoot(): Long = findRootExpression().evaluate(this)

    fun solve(unresolvedVariable: UnresolvedVariable): Long = findExpression(unresolvedVariable.name).evaluate(this)

    fun solveHuman(): Long {
      val rootExpression = findRootExpression().expression as BinaryExpression
      // step 1: resolve unresolved expressions until all but HUMAN are resolved:
      val left  = resolveExpression(rootExpression.left)
      val right = resolveExpression(rootExpression.right)
      // step 2: simplify expressions:
      val leftMustBeResolved = containsUnresolvedHumanExpression(left)
      val sideToResolve = simplify(if (leftMustBeResolved) left  else right)
      val otherSide     = evaluateToLiteral(if (leftMustBeResolved) right else left)
      // step 3: move expressions to other side using counter expressions until HUMAN is left as single expression on one side:
      val resultExpression = solveEquation(sideToResolve, evaluateToLiteral(otherSide))
      // step 4: evaluate result expression (should be a literal already, anyway)
      return resultExpression.evaluate(this)
    }

    private fun resolveExpression(expression: Expression): Expression {
      if (expression is Literal) return expression
      if (expression is UnresolvedVariable) {
        if (expression.name == HUMAN) return expression
        return resolveExpression(findExpression(expression.name).expression)
      }
      if (expression is BinaryExpression) {
        return expression.replaceExpressions(resolveExpression(expression.left), resolveExpression(expression.right))
      }
      else throw IllegalStateException("unexpected expression type: $expression")
    }

    fun simplify(expression: Expression): Expression {
      if (expression is Literal) return expression
      if (expression is UnresolvedVariable) return expression
      if (expression is BinaryExpression) {
        val simplifiedLeft  = simplify(expression.left)
        val simplifiedRight = simplify(expression.right)
        if (simplifiedLeft is Literal && simplifiedRight is Literal) {
          return evaluateToLiteral(expression)
        }
        return expression.replaceExpressions(simplifiedLeft, simplifiedRight)
      }
      else throw IllegalStateException("unexpected expression type: $expression")
    }

    private fun containsUnresolvedHumanExpression(expression: Expression): Boolean {
      if (expression is Literal) return false
      if (expression is UnresolvedVariable && expression.name == HUMAN) return true
      if (expression is BinaryExpression) {
        return containsUnresolvedHumanExpression(expression.left) ||
               containsUnresolvedHumanExpression(expression.right)
      }
      else throw IllegalStateException("unexpected expression type: $expression")
    }

    private fun solveEquation(sideToResolve: Expression, otherSide: Expression): Expression {
      if (sideToResolve is UnresolvedVariable) return otherSide
      return if (sideToResolve is BinaryExpression) {
        if (sideToResolve.left is Literal) {
          if (sideToResolve.isCommutative()) {
            solveEquation(sideToResolve.right, evaluateToLiteral(sideToResolve.counterExpression(otherSide, sideToResolve.left)))
          } else {
            solveEquation(sideToResolve.right, evaluateToLiteral(sideToResolve.counterExpression(sideToResolve.left, Multiplication(Literal(-1), otherSide))))
          }
        } else {
          solveEquation(sideToResolve.left,  evaluateToLiteral(sideToResolve.counterExpression(otherSide, sideToResolve.right)))
        }
      } else throw IllegalStateException("unexpected expression type: $sideToResolve")
    }

    private fun evaluateToLiteral(expression: Expression): Expression = Literal(expression.evaluate(this))

    private fun findRootExpression() = findExpression(ROOT)

    private fun findExpression(name: String) = expressions[name] ?: throw IllegalStateException("there must be an expression with name '$name'!")
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
