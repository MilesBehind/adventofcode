package de.smartsteuer.frank.adventofcode2021.day24

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val alu = Alu()
  val program = alu.readProgram(lines("/adventofcode2021/day24/program.txt"))
  //val inputs = generateSequence(9999) { it - 1 }
  //  .map { it.toString() }
  //  .filter { '0' !in it }
  //  .map { it.map { digits -> digits.digitToInt() } }
  val inputs = listOf(97_996_999_369_829.toString().map { it.digitToInt() })
  val modelNumber = inputs.find { input: List<Int> ->
    //if (counter++ % 100_000 == 0) println("counter = ${(counter - 1) / 100_000}, number: ${input.joinToString("")}")
    val result = alu(program, Input(input))
    result['z'] == 0
  }
  println("model number = ${modelNumber?.joinToString("")}")
}

typealias Variable    = Char
typealias Instruction = (Input) -> Unit

data class Input(private val input: List<Int>) {
  private var inputIndex: Int = 0

  fun next(): Int = input[inputIndex++]
}

class Alu {
  object State {
    private val state = mutableMapOf('w' to 0, 'x' to 0, 'y' to 0, 'z' to 0)

    operator fun get(variable: Variable): Int =
      state[variable] ?: error("invalid variable name '$variable'")

    operator fun set(variable: Variable, value: Int) {
      state[variable] = value
    }

    override fun toString() = state.toString()
  }

  operator fun invoke(program: List<Instruction>, input: Input): State {
    "wxyz".forEach { State[it] = 0 }
    println("state at start: $State")
    program.forEachIndexed { index, instruction ->
      instruction(input)
      println("state after instruction ${index + 1}: $State")
    }
    return State
  }

  private fun input      (variable: Variable):                 Instruction = { input -> State[variable] = input.next() }

  private fun addVariable(target: Variable, source: Variable): Instruction = { State[target] = State[target] + State[source] }
  private fun addLiteral (target: Variable, source: Int):      Instruction = { State[target] = State[target] + source }

  private fun mulVariable(target: Variable, source: Variable): Instruction = { State[target] = State[target] * State[source] }
  private fun mulLiteral (target: Variable, source: Int):      Instruction = { State[target] = State[target] * source }

  private fun divVariable(target: Variable, source: Variable): Instruction = { State[target] = State[target] / State[source] }
  private fun divLiteral (target: Variable, source: Int):      Instruction = { State[target] = State[target] / source }

  private fun modVariable(target: Variable, source: Variable): Instruction = { State[target] = State[target] % State[source] }
  private fun modLiteral (target: Variable, source: Int):      Instruction = { State[target] = State[target] % source }

  private fun eqlVariable(target: Variable, source: Variable): Instruction = { State[target] = if (State[target] == State[source]) 1 else 0 }
  private fun eqlLiteral (target: Variable, source: Int):      Instruction = { State[target] = if (State[target] == source)        1 else 0 }

  fun readProgram(lines: List<String>): List<Instruction> = lines.map { line ->
    val parts = """(\w{3})\s(\w)\s?(.*)""".toRegex().matchEntire(line)?.groupValues?.drop(1) ?: error("could not parse instruction '$line'")
    when (parts[0]) {
      "inp" -> input(parts[1].first())
      "add" -> if (parts[2].first() in "wxyz") addVariable(parts[1].first(), parts[2].first()) else addLiteral(parts[1].first(), parts[2].toInt())
      "mul" -> if (parts[2].first() in "wxyz") mulVariable(parts[1].first(), parts[2].first()) else mulLiteral(parts[1].first(), parts[2].toInt())
      "div" -> if (parts[2].first() in "wxyz") divVariable(parts[1].first(), parts[2].first()) else divLiteral(parts[1].first(), parts[2].toInt())
      "mod" -> if (parts[2].first() in "wxyz") modVariable(parts[1].first(), parts[2].first()) else modLiteral(parts[1].first(), parts[2].toInt())
      "eql" -> if (parts[2].first() in "wxyz") eqlVariable(parts[1].first(), parts[2].first()) else eqlLiteral(parts[1].first(), parts[2].toInt())
      else  -> error("unknown instruction: '${parts[0]}'")
    }
  }
}



