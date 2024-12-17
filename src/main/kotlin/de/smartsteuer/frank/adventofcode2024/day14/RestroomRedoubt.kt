package de.smartsteuer.frank.adventofcode2024.day14

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  RestroomRedoubt.execute(lines("/adventofcode2024/day14/robots.txt"))
}

object RestroomRedoubt : Day<Long> {

  override fun part1(input: List<String>): Long =
    input.parseRobots().simulate(100).quadrants().fold(1) { product, robots -> product * robots.size }

  override fun part2(input: List<String>): Long =
    input.parseRobots().simulateUntilLineIsFound().toLong()

  data class Pos(val x: Long, val y: Long) {
    operator fun plus(other: Pos): Pos = Pos(x + other.x, y + other.y)
    operator fun times(scalar: Long): Pos = Pos(x * scalar, y * scalar)
    operator fun rem(other: Pos): Pos = Pos(x % other.x, y % other.y)
  }

  data class Robot(val pos: Pos, val velocity: Pos)

  data class SafetyArea(val robots: List<Robot>, val size: Pos) {
    fun simulate(seconds: Long): SafetyArea =
      this.copy(robots = robots.map { robot ->
        robot.copy(pos = ((robot.velocity * seconds + robot.pos) % size + size) % size)
      })

    fun quadrants(): List<List<Robot>> =
      robots.filter { robot -> robot.pos.x != size.x / 2 && robot.pos.y != size.y / 2 }
        .partition { robot -> robot.pos.x < size.x / 2 }
        .toList()
        .map { robots -> robots.partition { robot -> robot.pos.y < size.y / 2 }.toList() }
        .flatten()

    fun simulateUntilLineIsFound(): Int {
      (1..20_000).forEach { seconds ->
        val safetyArea = this.simulate(seconds.toLong())
        if (safetyArea.findSeparatedHorizontalLine()) {
          return seconds
        }
      }
      return -1
    }

    private fun findSeparatedHorizontalLine(): Boolean {
      val positions = robots.map { robot -> robot.pos }.toSet()
      val lineToFind = (0 until size.x / 4).map { 'X' }.joinToString("")
      (size.y / 4 until size.y / 2).forEach { y ->
        val line = (0 until size.x).map { x -> if (Pos(x, y) in positions) 'X' else ' ' }.joinToString("")
        if (lineToFind in line) {
          return true.also { println(this) }
        }
      }
      return false
    }

    override fun toString(): String {
      val robotCountByPos = robots.groupBy { it.pos }.mapValues { it.value.size }
      return buildString {
        (0 until size.y ).forEach { y ->
          (0 until size.x).forEach { x ->
            append(robotCountByPos[Pos(x, y)] ?: '.')
          }
          appendLine()
        }
      }
    }
  }

  internal fun List<String>.parseRobots(): SafetyArea =
    """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex().let { regex ->
      this.map { line ->
        (regex.matchEntire(line) ?: throw IllegalArgumentException("invalid line: $line")).groupValues.let { (_, px, py, vx, vy) ->
          Robot(Pos(px.toLong(), py.toLong()), Pos(vx.toLong(), vy.toLong()))
        }
      }.let { robots ->
        SafetyArea(robots, if (robots.size > 20) Pos(101, 103) else Pos(11, 7))//.also { println(it) }
      }
    }
}
