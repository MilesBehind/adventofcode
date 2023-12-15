package de.smartsteuer.frank.adventofcode2023.day15

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val sequence = parseInitializationSequence(lines("/adventofcode2023/day15/initialization-sequence.txt"))
  measureTime { println("part 1: ${part1(sequence)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(sequence)}") }.also { println("part 2 took $it") }
}

internal fun part1(sequence: InitializationSequence): Int =
  sequence.steps.sumOf { it.hash() }

internal fun part2(sequence: InitializationSequence): Int =
  sequence.apply { applySequence() }.computeFocusingPower()

internal data class InitializationSequence(val steps: List<String>) {
  private val boxes = List(256) { Box() }

  fun applySequence(): List<Box> =
    parseSteps().fold(boxes) { _, step ->
      step.apply(boxes[step.hash])
      boxes
    }

  fun computeFocusingPower(): Int =
    boxes
      .withIndex()
      .filter { (_, box) -> box.lenses.isNotEmpty() }
      .sumOf { (index, box) -> box.focusingPower(index) }

  fun parseSteps(): List<Step> = steps.map { Step.parse(it) }
}

internal data class Box(val lenses: MutableMap<String, Int> = mutableMapOf()) {
  fun focusingPower(index: Int) = (index + 1) * lenses.values.withIndex().sumOf { (lensIndex, focalLength) -> (lensIndex + 1) * focalLength }
}

internal sealed interface Step {
  val hash: Int
  fun apply(box: Box)

  companion object {
    fun parse(step: String): Step = when {
      step.endsWith("-") -> step.dropLast(1).let { label -> Remove(label, label.hash()) }
      else               -> step.split("=").let { (label, lens) -> Put(label, lens.toInt(), label.hash()) }
    }
  }
}

internal data class Put(val label: String, val lens: Int, override val hash: Int): Step {
  override fun apply(box: Box) { box.lenses[label] = lens }
}

internal data class Remove(val label: String, override val hash: Int): Step {
  override fun apply(box: Box) { box.lenses.remove(label) }
}

internal fun String.hash(): Int =
  fold(0) { acc, char -> ((acc + char.code) * 17) % 256 }

internal fun parseInitializationSequence(lines: List<String>): InitializationSequence {
  return InitializationSequence(lines.first().split(","))
}