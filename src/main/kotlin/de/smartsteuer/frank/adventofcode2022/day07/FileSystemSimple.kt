package de.smartsteuer.frank.adventofcode2022.day07

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val fileSystemOutput = lines("/adventofcode2022/day07/file-system-output.txt")
  println("day 07, part 1: ${part1(fileSystemOutput)}")
  println("day 07, part 2: ${part2(fileSystemOutput)}")
  println("day 07, part 1: ${part1Simple(fileSystemOutput)}")
  println("day 07, part 2: ${part2Simple(fileSystemOutput)}")
}

fun part1Simple(fileSystemOutput: List<String>): Int {
  val root = parseOutput(fileSystemOutput.asSequence())
  return root
    .findAll()
    .map { it.size() }
    .filter { it < 100_000 }
    .sum()
}

fun part2Simple(fileSystemOutput: List<String>): Int {
  val root = parseOutput(fileSystemOutput.asSequence())
  val totalSpace  = 70_000_000
  val neededSpace = 30_000_000
  val usedSpace   = root.size()
  val freeSpace   = totalSpace - usedSpace
  val spaceToFree = neededSpace - freeSpace
  return root
    .findAll()
    .asSequence()
    .map { it.size() }
    .filter { it >= spaceToFree }
    .sorted()
    .first()
}

data class Dir(val name: String, var size: Int = 0, val childDirectories: MutableList<Dir> = mutableListOf()) {
  fun size(): Int = size + childDirectories.sumOf { it.size() }
  fun find(name: String): Dir = if (this.name == name) this else childDirectories.first { it.name == name }
  fun findAll(): List<Dir> = listOf(this) + childDirectories.flatMap { it.findAll() }
}

fun parseOutput(lines: Sequence<String>): Dir {
  val root  = Dir("/")
  val stack = mutableListOf(root)
  lines.drop(1).forEach { line ->
    when {
      line.startsWith("$ ls")    -> {}
      line.startsWith("$ cd ..") -> stack.removeLast()
      line.startsWith("$ cd")    -> line.split(" ")[2].let { name -> stack += stack.last().find(name) }
      line.startsWith("dir")     -> line.split(" ")[1].let { name -> stack.last().childDirectories += Dir(name) }
      else                       -> line.split(" ")[0].toInt().let { size -> stack.last().size += size }
    }
  }
  return root
}