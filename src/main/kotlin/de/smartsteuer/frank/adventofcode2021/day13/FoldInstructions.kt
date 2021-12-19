package de.smartsteuer.frank.adventofcode2021.day13

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val lines = lines("/day13/fold-instructions.txt")
  val (dots: Set<Dot>, instructions: List<(Dot) -> Dot>) = parseInput(lines)
  val dotsFoldedOnce = applyInstructions(instructions.take(1), dots)
  println("dots folded once: ${dotsFoldedOnce.size}")

  val dotsFolded = applyInstructions(instructions, dots)
  val text = render(dotsFolded)
  println("code:")
  println(text.joinToString("\n"))
}

internal fun applyInstructions(instructions: List<Instruction>, dots: Set<Dot>) =
  instructions.fold(dots) { foldedDots, instruction -> foldedDots.map(instruction).toSet() }

internal typealias Instruction = (Dot) -> Dot

internal fun parseInput(lines: List<String>): Pair<Set<Dot>, List<Instruction>> {
  val (coordinateLines, instructionLines) = lines
    .filter { it.isNotBlank() }
    .partition { !it.startsWith("fold") }
  val dots = coordinateLines
    .map { it.split(',').map { number -> number.toInt() } }
    .map { Dot(it[0], it[1]) }
    .toSet()
  val instructions = instructionLines.map { line ->
    val (axis, position) = """fold along (.)=(\d+)""".toRegex().matchEntire(line)?.destructured ?: error("invalid instruction: '$line'")
    if (axis == "x") createVerticalFoldInstruction(position.toInt()) else createHorizontalFoldInstruction(position.toInt())
  }
  return dots to instructions
}

internal fun render(dots: Set<Dot>): List<String> {
  val maxX = dots.maxOf { it.x }
  val maxY = dots.maxOf { it.y }
  val paper = MutableList(maxY + 1) { y ->
    MutableList(maxX + 1) { x -> '.' }
  }
  dots.forEach { dot -> paper[dot.y][dot.x] = '#' }
  return paper.map { it.joinToString("") }
}

fun createVerticalFoldInstruction(position: Int): (Dot) -> Dot = { dot -> if (dot.x > position) Dot(2 * position - dot.x, dot.y) else dot }

fun createHorizontalFoldInstruction(position: Int): (Dot) -> Dot = { dot -> if (dot.y > position) Dot(dot.x, 2 * position - dot.y) else dot }

data class Dot(val x: Int, val y: Int)