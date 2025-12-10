package de.smartsteuer.frank.adventofcode2025.day10

import de.smartsteuer.frank.adventofcode2025.AStar
import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.Node
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Machines.execute(lines("/adventofcode2025/day10/machines.txt"))
}

object Machines: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseMachines().sumOf { machine -> machine.findFewestButtonPresses() }

  override fun part2(input: List<String>): Long =
    input.parseMachines().sumOf { machine -> machine.findFewestButtonPressesForRequiredJoltages() }
}

internal data class Button(val targetIndicators: List<Int>) {
  val mask = targetIndicators.toMask()

  fun apply(indicators: Int): Int =
    indicators xor mask

  fun apply(joltages: List<Int>): List<Int> =
    joltages.mapIndexed { index, joltage -> joltage + if (index in targetIndicators) 1 else 0 }

  override fun toString(): String = targetIndicators.joinToString()
}

internal data class Machine(val targetIndicators: List<Boolean>, val buttons: List<Button>, val requiredJoltages: List<Int>) {
  internal val targetMask = targetIndicators.mapIndexedNotNull { index, flag -> if (flag) index else null}.toMask()
  internal val indicatorCache = mutableMapOf<Int, List<Pair<Indicators, Double>>>()
  internal val joltageCache = mutableMapOf<List<Int>, List<Pair<Joltages, Double>>>()

  inner class Indicators(val indicators: Int, override val isGoal: Boolean): Node<Indicators> {
    override val heuristic = 1.0
    override val key = indicators

    override val neighbors: List<Pair<Indicators, Double>> get() = indicatorCache.getOrPut(indicators) {
      buttons.map { button ->
        val newIndicators = button.apply(indicators)
        Indicators(newIndicators, newIndicators == targetMask) to 1.0
      }
    }

    override fun toString(): String = indicators.toString(2)
  }

  inner class Joltages(val joltages: List<Int>, override val isGoal: Boolean): Node<Joltages> {
    override val heuristic = 1.0
    override val key = joltages

    override val neighbors: List<Pair<Joltages, Double>> get() = joltageCache.getOrPut(key) {
      buttons.mapNotNull { button ->
        val newJoltages = button.apply(joltages)
        if (newJoltages.zip(requiredJoltages).any { (new, target) -> new > target }) null
        else Joltages(newJoltages, newJoltages == requiredJoltages) to 1.0
      }//.also { println("neighbors for machine state $this: $it") }
    }

    override fun toString(): String = joltages.joinToString()
  }

  fun findFewestButtonPresses(): Long {
    val startState = Indicators(0, false)
    val result = AStar<Indicators>().search(startState) ?: error("no solution found for machine $this")
    //println(result)
    //println("-".repeat(180))
    return result.steps.toLong()
  }

  fun findFewestButtonPressesForRequiredJoltages(): Long {
    val startState = Joltages(List(requiredJoltages.size) { 0 }, false)
    val result = AStar<Joltages>().search(startState) ?: error("no solution found for machine $this")
    println(result)
    println("-".repeat(180))
    return result.steps.toLong()
  }
}

internal fun List<Int>.toMask(): Int =
  fold(0) { acc, indicator -> acc or (1 shl indicator) }

internal fun List<String>.parseMachines(): List<Machine> =
  """\[([.#]+)]\s+(.*)\s+\{(.*)}""".toRegex().let { pattern ->
    map { line ->
      val (targetIndicators, buttons, joltageRequirements) = pattern.matchEntire(line)?.destructured ?: error("invalid line: $line")
      val targetState = targetIndicators.trim().map { it == '#' }.toList()
      val buttonList = buttons.trim().split(" ").map { Button(it.trim().drop(1).dropLast(1).split(",").map { number -> number.toInt() }) }
      val joltageRequirementsList = joltageRequirements.trim().split(",").map { it.toInt() }
      Machine(targetState, buttonList, joltageRequirementsList)
    }
  }