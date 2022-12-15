package de.smartsteuer.frank.adventofcode2022.day14

import de.smartsteuer.frank.adventofcode2022.day14.Day14.Cave
import de.smartsteuer.frank.adventofcode2022.day14.Day14.Cave.Coordinate
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.math.abs

fun main() {
  val input = lines("/adventofcode2022/day14/rocks.txt")
  println("day 14, part 1: ${Day14.part1(input)}")
  println("day 14, part 2: ${Day14.part2(input)}")
}

typealias CellChecker = (Cave, Coordinate) -> Boolean

object Day14 {
  private const val SOURCE_X = 500
  private const val SOURCE_Y =   0

  fun part1(input: List<String>): Int {
    val isFinished  = { cave: Cave, grain: Coordinate -> grain.y > cave.maxRockY }
    val isCollision = { cave: Cave, grain: Coordinate -> grain in cave.rockCells || grain in cave.sandCells }
    return parseAndSimulate(input, isFinished, isCollision)
  }

  fun part2(input: List<String>): Int {
    val isFinished  = { _: Cave, grain: Coordinate -> grain.x == SOURCE_X && grain.y == SOURCE_Y }
    val isCollision = { cave: Cave, grain: Coordinate -> grain in cave.rockCells || grain in cave.sandCells || grain.y >= cave.maxRockY + 2 }
    return parseAndSimulate(input, isFinished, isCollision) + 1
  }

  private fun parseAndSimulate(input: List<String>, isFinished:  CellChecker, isCollision: CellChecker): Int {
    val cave         = Cave.parse(input)//.also { println(it) }
    val caveWithSand = cave.simulate(Coordinate(SOURCE_X, SOURCE_Y), isFinished, isCollision)//.also { println(it) }
    return caveWithSand.grainCount()
  }


  data class Cave constructor(val rockCells: Set<Coordinate>, val sandCells: Set<Coordinate>, val maxRockY: Int) {

    data class Coordinate(val x: Int, val y: Int) {
      fun sandTargets(): List<Coordinate> = listOf(Coordinate(x, y + 1), Coordinate(x - 1, y + 1), Coordinate(x + 1, y + 1))
    }

    constructor(rockCells: Set<Coordinate>, sandCells: Set<Coordinate> = emptySet()): this(rockCells, sandCells, rockCells.maxBy { it.y }.y)

    fun simulate(grainSource: Coordinate, isFinished: CellChecker, isCollision: CellChecker): Cave {
      tailrec fun simulate(sandCells: Set<Coordinate>, grain: Coordinate): Cave {
        val cave = Cave(rockCells, sandCells, maxRockY)//.also { println(it) }
        val newGrain = grain.sandTargets().dropWhile { isCollision(cave, it) }.firstOrNull() ?: grain
        return when {
          isFinished(this, newGrain) -> Cave(rockCells, sandCells, maxRockY)
          newGrain == grain          -> simulate(sandCells + grain, grainSource)
          else                       -> simulate(sandCells, newGrain)
        }
      }
      return simulate(sandCells, grainSource)
    }

    fun grainCount() = sandCells.size

    override fun toString(): String {
      val allCells = rockCells + sandCells
      val xRange = (allCells.minBy { it.x }.x)..(allCells.maxBy { it.x }.x)
      val yRange = (allCells.minBy { it.y }.y)..(allCells.maxBy { it.y }.y)
      return buildString {
        yRange.map { y ->
          xRange.map { x ->
            val coordinate = Coordinate(x, y)
            append(when (coordinate) {
              in rockCells -> '▓'
              in sandCells -> 'o'
              else         -> '░'
            })
          }
          appendLine()
        }
      }
    }

    companion object {
      fun parse(input: List<String>): Cave {
        val rocks = input.flatMap { line ->
          val stops: List<Coordinate> = line.split("->")
            .map { coordinateString ->
              val (x, y) = coordinateString.split(",").map { it.trim().toInt() }
              Coordinate(x, y)
            }
          stops.zipWithNext{ from, to ->
            if (from.x == to.x) {
              val step = if (to.y > from.y) 1 else -1
              List(abs(from.y - to.y) + 1) { deltaY -> Coordinate(from.x, from.y + deltaY * step) }
            } else {
              val step = if (to.x > from.x) 1 else -1
              List(abs(from.x - to.x) + 1) { deltaX -> Coordinate(from.x + deltaX * step, from.y) }
            }
          }.flatten()
        }
        return Cave(rocks.toSet())
      }
    }
  }
}