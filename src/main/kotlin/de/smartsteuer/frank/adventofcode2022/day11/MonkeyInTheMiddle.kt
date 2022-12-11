package de.smartsteuer.frank.adventofcode2022.day11

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val input = lines("/adventofcode2022/day11/monkey-rules.txt")
  println("day 11, part 1: ${Day11.part1(input)}")
  println("day 11, part 2: ${Day11.part2(input)}")
}

object Day11 {
  fun part1(input: List<String>): Long {
    val monkeys = parseMonkeyRules(input)
    return computeMonkeyBusiness(monkeys, 20, DivideOperation(3))
  }

  fun part2(input: List<String>): Long {
    val monkeys         = parseMonkeyRules(input)
    val reliefOperation = ModuloOperation(monkeys.map { it.rule.test.divisor }.fold(1) { product, factor -> product * factor })
    return computeMonkeyBusiness(monkeys, 10_000, reliefOperation)
  }

  private fun computeMonkeyBusiness(monkeys: List<Monkey>, rounds: Int, reliefOperation: Operation): Long {
    val result   = executeRounds(monkeys, rounds, reliefOperation)
    val topItems = result.map { it.inspectedItems }.sortedDescending().take(2)
    return topItems.fold(1) { product, factor -> product * factor }
  }

  private tailrec fun executeRounds(monkeys: List<Monkey>, rounds: Int, reliefOperation: Operation, monkeyIndex: Int = 0): List<Monkey> {
    if (rounds == 0) return monkeys //.also { println("  rounds == 0 => finished") }
    if (monkeyIndex >= monkeys.size) {
      return executeRounds(monkeys, rounds - 1, reliefOperation)
    }
    val monkey = monkeys[monkeyIndex]
    if (monkey.items.isEmpty()) {
      return executeRounds(monkeys, rounds, reliefOperation, monkeyIndex + 1)
    }
    val newItem           = reliefOperation(monkey.rule.operation(monkey.items.first()))
    val targetMonkeyIndex = monkey.rule.test.findTargetMonkeyIndex(newItem)
    val newMonkey         = monkey.copy(items = monkey.items.drop(1), inspectedItems = monkey.inspectedItems + 1)
    val newTargetMonkey   = monkeys[targetMonkeyIndex].copy(items = monkeys[targetMonkeyIndex].items + newItem)
    return executeRounds(monkeys.replaced(monkeyIndex, newMonkey).replaced(targetMonkeyIndex, newTargetMonkey),
                         rounds, reliefOperation, monkeyIndex)
  }

  data class Monkey(val items: List<Long>, val rule: MonkeyRule, val inspectedItems: Long = 0)

  data class MonkeyRule(val operation: Operation, val test: Test)

  sealed interface Operation {
    operator fun invoke(item: Long): Long
  }

  data class AddOperation(val increase: Int): Operation {
    override fun invoke(item: Long) = item + increase
  }

  data class MultiplyOperation(val factor: Int): Operation {
    override fun invoke(item: Long) = item * factor
  }

  data class DivideOperation(val divisor: Int): Operation {
    override fun invoke(item: Long) = item / divisor
  }

  object SquareOperation: Operation {
    override fun invoke(item: Long) = item * item
  }

  data class ModuloOperation(val divisor: Long): Operation {
    override fun invoke(item: Long) = item % divisor
  }

  data class Test(val divisor: Int, val successMonkeyIndex: Int, val failureMonkeyIndex: Int) {
    fun findTargetMonkeyIndex(dividend: Long): Int = if (dividend % divisor == 0L) successMonkeyIndex else failureMonkeyIndex
  }

  fun parseMonkeyRules(input: List<String>): List<Monkey> =
    input.chunked(7).map { ruleLines ->
      val items           = ruleLines[1].substringAfterLast(":").split(",").map { it.trim().toLong() }
      val operationString = ruleLines[2].substringAfterLast("=").trim()
      val operation       = when {
        operationString.startsWith("old + ")    -> AddOperation(operationString.intAfter("+"))
        operationString.startsWith("old * old") -> SquareOperation
        operationString.startsWith("old * ")    -> MultiplyOperation(operationString.intAfter("*"))
        else                                    -> throw IllegalArgumentException("unknown operation: '$operationString'")
      }
      val divisor            = ruleLines[3].intAfter("by")
      val successMonkeyIndex = ruleLines[4].intAfter("monkey")
      val failureMonkeyIndex = ruleLines[5].intAfter("monkey")
      val test               = Test(divisor, successMonkeyIndex, failureMonkeyIndex)
      Monkey(items, MonkeyRule(operation, test))
    }

  private fun String.intAfter(delimiter: String) = substringAfterLast(delimiter).trim().toInt()

  fun <T> List<T>.replaced(index: Int, element: T): List<T> = take(index) + element + drop(index + 1)
}