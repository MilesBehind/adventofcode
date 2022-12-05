package de.smartsteuer.frank.adventofcode2022.day05

import de.smartsteuer.frank.adventofcode2022.extractNumbers
import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val input = lines("/adventofcode2022/day05/crates.txt")
  println("day 05, part 1: ${part1(input)}")
  println("day 05, part 2: ${part2(input)}")
}

typealias CrateStack  = List<Char>
typealias Transformer = (CrateStack) -> CrateStack

fun part1(input: List<String>): String =
  moveCrates(input) { it.reversed() }

fun part2(input: List<String>): String =
  moveCrates(input) { it }

fun moveCrates(input: List<String>, transformCrates: Transformer): String =
  parseMovements(input)
    .fold(parseStacks(input)) { stacks, movement -> movement.apply(stacks, transformCrates) }
    .joinToString(separator = "") { it.last().toString() }

data class Movement(val count: Int, val from: Int, val to: Int) {
  fun apply(stacks: List<CrateStack>, transformCrates: Transformer): List<CrateStack> =
    transformCrates(stacks[from - 1].takeLast(count)).let { crates ->
      stacks.mapIndexed { index, stack ->
        when (index + 1) {
          from -> stack.dropLast(count)
          to   -> stack + crates
          else -> stack
        }
      }
    }
}

fun parseStacks(input: List<String>): List<CrateStack> =
  input
    .takeWhile { '[' in it }
    .map { it.filterIndexed { index, _ -> index % 4 == 1 } }
    .reversed()
    .let { lines ->
      val emptyStacks = List<CrateStack>(lines.first().length) { listOf() }
      lines.fold(emptyStacks) { result, line ->
        result.mapIndexed { index, stack ->
          if (index < line.length && line[index] != ' ') stack + line[index] else stack
        }
      }
    }

fun parseMovements(input: List<String>): Sequence<Movement> =
  input
    .dropWhile { !it.startsWith("move") }
    .asSequence()
    .extractNumbers("""move (\d+) from (\d+) to (\d+)""".toRegex())
    .map { (count, from, to) -> Movement(count, from, to) }

