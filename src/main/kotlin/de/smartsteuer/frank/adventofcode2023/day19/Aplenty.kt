package de.smartsteuer.frank.adventofcode2023.day19

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val (workflowEngine, parts) = parseWorkflowsAndParts(lines("/adventofcode2023/day19/workflows-and-parts.txt"))
  measureTime { println("part 1: ${part1(workflowEngine, parts)}") }.also { println("part 1 took $it") }
}

internal fun part1(workflowEngine: WorkflowEngine, parts: List<Part>): Long =
  workflowEngine.execute(parts).sumOf { it.rating().toLong() }

internal data class WorkflowEngine(val workFlows: List<Workflow>) {
  private val workFlowsByName    = workFlows.associateBy { it.name }
  private val startWorkflow      = workFlowsByName.getValue("in")

  fun execute(parts: List<Part>): List<Part> = parts.filter { execute(it) }

  private fun execute(part: Part): Boolean {
    tailrec fun execute(workflow: Workflow): Boolean =
      when (val target = workflow.execute(part)) {
        "A"  -> true
        "R"  -> false
        else -> execute(workFlowsByName.getValue(target))
      }
    return execute(startWorkflow)
  }
}

internal data class Workflow(val name: String, val rules: List<Rule>) {
  fun execute(part: Part): String = rules.first { it.check(part) }.target
}

internal sealed interface Rule {
  val target: String
  fun check(part: Part): Boolean
}

internal data class Less(val propertyName: String, val propertyValue: Int, override val target: String): Rule {
  override fun check(part: Part): Boolean = part.properties.getValue(propertyName) < propertyValue
}

internal data class Greater(val propertyName: String, val propertyValue: Int, override val target: String): Rule {
  override fun check(part: Part): Boolean = part.properties.getValue(propertyName) > propertyValue
}

internal data class MatchAlways(override val target: String): Rule {
  override fun check(part: Part): Boolean = true
}

internal data class Part(val properties: Map<String, Int>) {
  fun rating() = properties.values.sum()
}

internal fun parseWorkflowsAndParts(lines: List<String>): Pair<WorkflowEngine, List<Part>> {
  val emptyLineIndex = lines.indexOfFirst { it.isEmpty() }
  val workflows = lines.take(emptyLineIndex).map { line ->
    val (name, ruleStrings) = """(\w+)\{(.+)}""".toRegex().matchEntire(line)!!.groupValues.drop(1)
    val rules = ruleStrings.split(',').map { ruleString ->
      val match = """(\w+)([<>])(\d+):(\w+)""".toRegex().matchEntire(ruleString)
      if (match != null)  {
        val (propertyName, operator, value, target) = match.groupValues.drop(1)
        when (operator) {
          "<"  -> Less   (propertyName, value.toInt(), target)
          else -> Greater(propertyName, value.toInt(), target)
        }
      } else {
        MatchAlways(ruleString)
      }
    }
    Workflow(name, rules)
  }
  val parts = lines.drop(emptyLineIndex + 1).map { line ->
    val properties = line.drop(1).dropLast(1).split(",").map { propertyString ->
      val (name, value) = propertyString.split("=")
      name to value.toInt()
    }.toMap()
    Part(properties)
  }
  return WorkflowEngine(workflows) to parts
}