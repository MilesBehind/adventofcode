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
    input.parseMachines().sumOf { machine -> machine.findFewestButtonPressesForIndicators() }

  override fun part2(input: List<String>): Long =
    input.parseMachines().sumOf { machine -> machine.findFewestButtonPressesForRequiredJoltages() }
}

internal data class Button(val targetIndicators: List<Int>, val joltageCount: Int) {
  val mask = targetIndicators.toMask()
  val modifications = (0 until joltageCount).map { joltageIndex -> if (joltageIndex in targetIndicators) 1 else 0 }

  fun apply(indicators: Int): Int =
    indicators xor mask

  fun apply(joltages: List<Int>): List<Int> =
    joltages.mapIndexed { index, joltage -> joltage + modifications[index] }

  override fun toString(): String = "(${targetIndicators.joinToString()})/(${modifications.joinToString()})"
}

internal data class Machine(val targetIndicators: List<Boolean>, val buttons: List<Button>, val requiredJoltages: List<Int>) {
  internal val targetMask = targetIndicators.mapIndexedNotNull { index, flag -> if (flag) index else null}.toMask()

  inner class Indicators(val indicators: Int, override val isGoal: Boolean): Node<Indicators> {
    override val heuristic = 1.0
    override val key = indicators

    override val neighbors: List<Pair<Indicators, Double>> get() =
      buttons.map { button -> Indicators(button.apply(indicators), button.apply(indicators) == targetMask) to 1.0 }
  }

  fun findFewestButtonPressesForIndicators(): Long {
    val startState = Indicators(0, false)
    val result = AStar<Indicators>().search(startState) ?: error("no solution found for machine $this")
    return result.steps.toLong()
  }

  fun findFewestButtonPressesForRequiredJoltages(): Long {
    val buttonsRequiredForJoltageIndexes = requiredJoltages.indices.map { index ->
      buttons.partition { button -> button.modifications[index] > 0 }
    }
    println("button counts for joltages: ${buttonsRequiredForJoltageIndexes.map { it.first.size to it.second.size }}")
    return 0
  }

  /*
  fun findFewestButtonPressesForRequiredJoltages2(): Long {
    tailrec fun findFewestButtonPresses(joltageLists: List<Pair<List<Int>, Int>>, joltageModifications: List<Button>): Long {
      if (joltageModifications.isEmpty()) throw IllegalStateException("could not find solution for $requiredJoltages")
      val joltageModification = joltageModifications.first()
      println("trying modification $joltageModification")
      val newJoltages = joltageLists.flatMap { (joltageList, count) -> computeNewJoltages(joltageList, joltageModification, count) }
      //println("found new joltages $newJoltages")
      val matches = newJoltages.filter { (joltageList, _) -> joltageList == requiredJoltages }
      if (matches.isNotEmpty()) {
        //println("found solutions: ${matches.first().second} button presses for ${matches.map { it.first }}")
        val (joltages, clicks) = matches.minBy { it.second }
        println("found solution: $clicks button presses for $joltages")
        return clicks.toLong()
      }
      return findFewestButtonPresses(newJoltages, joltageModifications.drop(1))
    }
    return findFewestButtonPresses(listOf(List(requiredJoltages.size) { 0 } to 0), buttons)
  }

  private fun computeNewJoltages(joltageList: List<Int>, joltageModification: Button, clickCountSoFar: Int): List<Pair<List<Int>, Int>> {
    //println("  compute joltages for $joltageList with $joltageModification and $clickCountSoFar clicks")
    val maximumButtonClicks: Int = joltageList.withIndex().minOf { (index, joltage) ->
      val effect = joltageModification.modifications[index]
      if (effect == 0) 1000 else requiredJoltages[index] - joltage
    }
    //println("  0..$maximumButtonClicks clicks")
    return (0..maximumButtonClicks).map { clickCount ->
      val newJoltageList = joltageList.mapIndexed { index, joltage -> joltage + clickCount * joltageModification.modifications[index] }
      val newClickCount = clickCountSoFar + clickCount
      newJoltageList to newClickCount
    }//.also { println("  found ${it.size} new joltages\n") }
  }
 */
}

internal fun List<Int>.toMask(): Int =
  fold(0) { acc, indicator -> acc or (1 shl indicator) }

internal fun List<String>.parseMachines(): List<Machine> =
  """\[([.#]+)]\s+(.*)\s+\{(.*)}""".toRegex().let { pattern ->
    map { line ->
      val (targetIndicators, buttons, joltageRequirements) = pattern.matchEntire(line)?.destructured ?: error("invalid line: $line")
      val targetState = targetIndicators.trim().map { it == '#' }.toList()
      val joltageRequirementsList = joltageRequirements.trim().split(",").map { it.toInt() }
      val buttonList = buttons.trim().split(" ").map { Button(it.trim().drop(1).dropLast(1).split(",").map { number -> number.toInt() }, joltageRequirementsList.size) }
      Machine(targetState, buttonList, joltageRequirementsList)
    }
  }