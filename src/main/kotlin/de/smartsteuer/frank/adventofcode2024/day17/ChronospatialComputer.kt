package de.smartsteuer.frank.adventofcode2024.day17

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  ChronospatialComputer.execute(lines("/adventofcode2024/day17/program.txt"))
}

object ChronospatialComputer: Day<String> {
  override fun part1(input: List<String>): String =
    input.parseProgram().run().joinToString(",")

  // start with 0
  // for all positions n in program from right to left:
  //   try running program with current value for register A, incrementing by 1 until last n numbers match
  //   register A = register A shl 3
  // this needs 465 program runs to find solution
  override fun part2(input: List<String>): String {
    val computer = input.parseProgram()
    return (1..computer.program.size).fold(0L) { registerA, n ->
      val endOfSequenceSoFar = computer.program.takeLast(n).map { it.toLong() }
      generateSequence(registerA shl 3) { it + 1 }.first { testRegisterA ->
        computer.copy(registerA = testRegisterA).run().takeLast(n) == endOfSequenceSoFar
      }
    }.toString()
  }

  data class Computer(var registerA: Long, var registerB: Long, var registerC: Long, val program: List<Int>) {
    private val output = mutableListOf<Long>()
    private var instructionPointer: Int = 0

    enum class Operand(val value: (computer: Computer) -> Long) {
      LITERAL_0 ( { _        -> 0 } ),
      LITERAL_1 ( { _        -> 1 } ),
      LITERAL_2 ( { _        -> 2 } ),
      LITERAL_3 ( { _        -> 3 } ),
      REGISTER_A( { computer -> computer.registerA } ),
      REGISTER_B( { computer -> computer.registerB } ),
      REGISTER_C( { computer -> computer.registerC } ),
      LITERAL_7 ( { _        -> 7 } ),
    }

    enum class Instruction(val execute: (computer: Computer, operand: Operand) -> Unit) {
      ADV ( { computer, operand -> computer.registerA = computer.registerA shr operand.value(computer).toInt() } ),
      BXL ( { computer, operand -> computer.registerB = computer.registerB xor operand.value(computer) } ),
      BST ( { computer, operand -> computer.registerB = operand.value(computer) and 7 } ),
      JNZ ( { computer, operand -> if (computer.registerA != 0L) computer.instructionPointer = operand.value(computer).toInt() } ),
      BXC ( { computer, _       -> computer.registerB = computer.registerB xor computer.registerC } ),
      OUT ( { computer, operand -> computer.output += operand.value(computer) and 7 } ),
      BDV ( { computer, operand -> computer.registerB = computer.registerA shr operand.value(computer).toInt() } ),
      CDV ( { computer, operand -> computer.registerC = computer.registerA shr operand.value(computer).toInt() } ),
    }

    fun run(): List<Long> {
      while (instructionPointer < program.size) {
        val instruction = Instruction.entries[program[instructionPointer++]]
        val operand     = Operand.entries[program[instructionPointer++]]
        instruction.execute(this, operand)
      }
      return output
    }
  }

  fun List<String>.parseProgram() = Computer(
    registerA = this[0].drop("Register A: ".length).toLong(),
    registerB = this[1].drop("Register B: ".length).toLong(),
    registerC = this[2].drop("Register C: ".length).toLong(),
    program   = this[4].drop("Program: ".length).split(',').map { it.toInt() }
  )
}