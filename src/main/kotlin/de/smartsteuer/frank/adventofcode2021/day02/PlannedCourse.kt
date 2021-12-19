package de.smartsteuer.frank.adventofcode2021.day02

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val commands = lines("/day02/commands.txt")
  val (position, depth) = executeCommands(0, 0, commands)
  println("position = $position, depth = $depth, product = ${position * depth}")

  val (position2, depth2, aim) = executeCommands(0, 0, 0, commands)
  println("position = $position2, depth = $depth2, aim = $aim, product = ${position2 * depth2}")
}

internal data class PositionAndDepth(val position: Int, val depth: Int)

internal tailrec fun executeCommands(position: Int, depth: Int, commands: List<String>): PositionAndDepth {
  if (commands.isEmpty()) {
    return PositionAndDepth(position, depth)
  }
  val (command: String, value: Int) = commands.first().split(' ').let { Pair(it[0], it[1].toInt()) }
  return when (command) {
    "forward" -> executeCommands(position + value, depth,         commands.drop(1))
    "down"    -> executeCommands(position,         depth + value, commands.drop(1))
    "up"      -> executeCommands(position,         depth - value, commands.drop(1))
    else      -> error("unknown command: '$command'")
  }
}

internal data class PositionAndDepthAndAim(val position: Int, val depth: Int, val aim: Int)

internal tailrec fun executeCommands(position: Int, depth: Int, aim: Int, commands: List<String>): PositionAndDepthAndAim {
  if (commands.isEmpty()) {
    return PositionAndDepthAndAim(position, depth, aim)
  }
  val (command: String, value: Int) = commands.first().split(' ').let { Pair(it[0], it[1].toInt()) }
  return when (command) {
    "forward" -> executeCommands(position + value, depth + aim * value, aim,         commands.drop(1))
    "down"    -> executeCommands(position,         depth,               aim + value, commands.drop(1))
    "up"      -> executeCommands(position,         depth,               aim - value, commands.drop(1))
    else      -> error("unknown command: '$command'")
  }
}

/*
internal fun executeCommandsOldSchool(position: Int, depth: Int, aim: Int, commands: List<String>): PositionAndDepthAndAim {
  var currentPosition = position
  var currentDepth    = depth
  var currentAim      = aim
  for (i in commands.indices) {
    val command = commands[i]
    val parts   = command.split(' ')
    val commandName = parts[0]
    val value       = parts[1].toInt()
    when (commandName) {
      "forward" -> {
        currentPosition += value
        currentDepth    += currentAim * value
      }
      "down"    -> {
        currentAim += value
      }
      "up"      -> {
        currentAim -= value
      }
    }
  }
  return PositionAndDepthAndAim(currentPosition, currentDepth, currentAim)
}
*/