package de.smartsteuer.frank.adventofcode2021.day02

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val commands = lines("/day02/commands.txt").toCommands()
  val (position, depth) = executeCommands(0, 0, commands)
  println("position = $position, depth = $depth, product = ${position * depth}")

  val (position2, depth2, aim) = executeCommands(0, 0, 0, commands)
  println("position = $position2, depth = $depth2, aim = $aim, product = ${position2 * depth2}")
}

internal fun List<String>.toCommands() = map { line -> line.split(' ') }.map { Command(it[0], it[1].toInt()) }

internal data class Command(val command: String, val value: Int)
internal data class SubmarineState(val position: Int, val depth: Int, val aim: Int = 0)

internal tailrec fun executeCommands(position: Int, depth: Int, commands: List<Command>): SubmarineState {
  if (commands.isEmpty()) {
    return SubmarineState(position, depth)
  }
  val (command: String, value: Int) = commands.first()
  return when (command) {
    "forward" -> executeCommands(position + value, depth,         commands.drop(1))
    "down"    -> executeCommands(position,         depth + value, commands.drop(1))
    "up"      -> executeCommands(position,         depth - value, commands.drop(1))
    else      -> error("unknown command: '$command'")
  }
}

internal tailrec fun executeCommands(position: Int, depth: Int, aim: Int, commands: List<Command>): SubmarineState {
  if (commands.isEmpty()) {
    return SubmarineState(position, depth, aim)
  }
  val (command: String, value: Int) = commands.first()
  return when (command) {
    "forward" -> executeCommands(position + value, depth + aim * value, aim,         commands.drop(1))
    "down"    -> executeCommands(position,         depth,               aim + value, commands.drop(1))
    "up"      -> executeCommands(position,         depth,               aim - value, commands.drop(1))
    else      -> error("unknown command: '$command'")
  }
}
