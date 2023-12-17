package de.smartsteuer.frank.adventofcode2023.day15

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val sequence = lines("/adventofcode2023/day15/initialization-sequence.txt").first().split(",")
  measureTime { println("part 1: ${part1(sequence)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(sequence)}") }.also { println("part 2 took $it") }
}

internal fun part1(sequence: List<String>): Int = sequence.sumOf { hash(it) }

internal fun part2(sequence: List<String>): Int {
  val boxes = List(256) { mutableMapOf<String, Int>() }
  sequence.forEach { step ->
    when {
      step.endsWith("-") -> step.dropLast(1).let { label -> boxes[hash(label)].remove(label) }
      else               -> step.split("=").let { (label, lens) -> boxes[hash(label)][label] = lens.toInt() }
    }
  }
  return boxes.withIndex()
    .filter { (_, box) -> box.isNotEmpty() }
    .sumOf { (index, box) -> (index + 1) * box.values.withIndex().sumOf { (lensIndex, focalLength) -> (lensIndex + 1) * focalLength } }
}

private fun hash(input: String): Int = input.fold(0) { acc, char -> ((acc + char.code) * 17) % 256 }
