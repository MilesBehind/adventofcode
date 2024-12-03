package de.smartsteuer.frank.adventofcode2024.day03

import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  val instructions: List<Instruction> = lines("/adventofcode2024/day03/computer-memory.txt").parseInstructions()
  println("part 1: ${part1(instructions)}")
  println("part 2: ${part2(instructions)}")
}

internal fun part1(instructions: List<Instruction>): Long =
  instructions.filterIsInstance<MultiplicationInstruction>().sumOf { instruction -> (instruction.op1 * instruction.op2).toLong() }

internal fun part2(instructions: List<Instruction>): Long =
  instructions.fold((true to 0L)) { (on, sum), instruction ->
    when (instruction) {
      is MultiplicationInstruction -> if (on) true to sum + (instruction.op1 * instruction.op2).toLong() else false to sum
      is DontInstruction           -> false to sum
      else                         -> true  to sum
    }
  }.second

sealed interface Instruction
data class MultiplicationInstruction(val op1: Int, val op2: Int): Instruction
data object DoInstruction: Instruction
data object DontInstruction: Instruction

internal fun List<String>.parseInstructions(): List<Instruction> =
  """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""".toRegex().let { regex ->
    this.flatMap { line -> regex.findAll(line)
      .map { match ->
        when (match.groupValues[0].take(3)) {
          "mul" -> MultiplicationInstruction(match.groupValues[1].toInt(), match.groupValues[2].toInt())
          "don" -> DontInstruction
          else  -> DoInstruction
        }
      }
      .toList()
    }
  }
