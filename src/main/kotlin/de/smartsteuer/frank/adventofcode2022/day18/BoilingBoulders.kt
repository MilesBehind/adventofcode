package de.smartsteuer.frank.adventofcode2022.day18

import de.smartsteuer.frank.adventofcode2022.day18.Day18.part1
import de.smartsteuer.frank.adventofcode2022.day18.Day18.part2
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.system.measureTimeMillis

fun main() {
  val input = lines("/adventofcode2022/day18/lava-droplets.txt")
  measureTimeMillis {
    println("day 18, part 1: ${part1(input)}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 18, part 2: ${part2(input)}")
  }.also { println("took $it ms") }
}

object Day18 {
  fun part1(input: List<String>): Int {
    val droplets = parseDroplets(input).toSet()
    return droplets.fold(droplets.size * 6) { faces, droplet ->
      faces - droplet.neighbours().count { neighbour -> neighbour in droplets }
    }
  }

  fun part2(input: List<String>): Long {
    return 0
  }

  data class Cube(val x: Int, val y: Int, val z: Int) {
    fun neighbours() = listOf(
      Cube(x - 1, y,     z),     Cube(x + 1, y,     z),
      Cube(x,     y - 1, z),     Cube(x,     y + 1, z),
      Cube(x,     y,     z - 1), Cube(x,     y,     z + 1),
    )
  }

  fun parseDroplets(input: List<String>): List<Cube> =
    input.map { line ->
      val (x, y, z) = line.split(",").map { it.toInt() }
      Cube(x, y, z)
    }
}
