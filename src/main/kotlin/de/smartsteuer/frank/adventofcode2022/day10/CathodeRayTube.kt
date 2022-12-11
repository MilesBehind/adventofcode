package de.smartsteuer.frank.adventofcode2022.day10

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val input = lines("/adventofcode2022/day10/cpu-instructions.txt")
  println("day 10, part 1: ${Day10.part1(input)}")
  println("day 10, part 2: ${Day10.part2(input)}")
}

object Day10 {
  fun part1(input: List<String>): Int {
    val instructions = parseInput(input)
    val timeToX      = executeInstructions(instructions)
    return listOf(20, 60, 100, 140, 180, 220).sumOf { time -> time * timeToX[time - 1] }
  }

  @Suppress("SpellCheckingInspection")
  fun part2(input: List<String>): String {
    val instructions = parseInput(input)
    val timeToX      = executeInstructions(instructions)
    val image        = createCrtImage(timeToX)
    println(image)
    return "PHLHJGZA"  // found after reading CRT image output
  }

  fun createCrtImage(timeToX: List<Int>): String {
    val pixels = timeToX.mapIndexed { time, x -> (if (time % 40 - x in (-1..1)) '#' else '.') }
    return pixels.take(6 * 40).chunked(40).joinToString(separator = "\n") { String(it.toCharArray()) }
  }

  fun executeInstructions(instructions: List<Instruction>): List<Int> =
    instructions.fold(listOf(1)) { result, instruction -> result + instruction.execute(result.last()) }

  sealed interface Instruction {
    fun execute(x: Int): Int
  }

  object Noop: Instruction {
    override fun execute(x: Int) = x
  }

  data class AddX(val increase: Int): Instruction {
    override fun execute(x: Int) = x + increase
  }

  fun parseInput(input: List<String>): List<Instruction> =
    input.flatMap { line ->
      when (line) {
        "noop" -> listOf(Noop)
        else   -> listOf(Noop, AddX(line.split(" ")[1].toInt()))
      }
    }
}