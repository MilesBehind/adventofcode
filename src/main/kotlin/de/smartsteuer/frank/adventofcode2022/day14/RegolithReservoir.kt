package de.smartsteuer.frank.adventofcode2022.day14

import de.smartsteuer.frank.adventofcode2022.day14.Day14.CellType
import de.smartsteuer.frank.adventofcode2022.day14.Day14.CellType.*
import de.smartsteuer.frank.adventofcode2022.day14.Day14.Coordinate
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.math.abs

fun main() {
  val input = lines("/adventofcode2022/day14/rocks.txt")
  println("day 14, part 1: ${Day14.part1(input)}")
  println("day 14, part 2: ${Day14.part2(input)}")
}

typealias Cells       = Map<Coordinate, CellType>
typealias CellChecker = (Cells, Coordinate) -> Boolean

object Day14 {
  private const val SOURCE_X = 500
  private const val SOURCE_Y =   0

  fun part1(input: List<String>): Int {
    val isFinished  = { cells: Cells, grain: Coordinate -> cells.keys.maxBy { it.y }.let { abyss -> grain.y > abyss.y } }
    val isCollision = { cells: Cells, grain: Coordinate -> grain in cells.keys }
    return parseAndSimulate(input, isFinished, isCollision)
  }

  fun part2(input: List<String>): Int {
    val isFinished  = { _: Cells, grain: Coordinate -> grain.x == SOURCE_X && grain.y == SOURCE_Y }
    val isCollision = { cells: Cells, grain: Coordinate ->
      grain in cells.keys || grain.y >= cells.filter { (_, type) -> type == ROCK }.keys.maxBy { it.y }.y + 2 }
    return parseAndSimulate(input, isFinished, isCollision) + 1
  }

  private fun parseAndSimulate(input: List<String>, isFinished:  CellChecker, isCollision: CellChecker): Int {
    val cave         = Cave.parse(input).also { println(it) }
    val caveWithSand = cave.simulate(Coordinate(SOURCE_X, SOURCE_Y), isFinished, isCollision).also { println(it) }
    return caveWithSand.grainCount()
  }

  data class Coordinate(val x: Int, val y: Int) {
    fun sandTargets(): List<Coordinate> = listOf(Coordinate(x, y + 1), Coordinate(x - 1, y + 1), Coordinate(x + 1, y + 1))
  }

  enum class CellType { ROCK, SAND }

  data class Cave(val cells: Map<Coordinate, CellType>) {
    fun simulate(grainSource: Coordinate, isFinished: CellChecker, isCollision: CellChecker): Cave {
      tailrec fun simulate(cells: Map<Coordinate, CellType>, grain: Coordinate): Cave {
        val newGrain = grain.sandTargets().dropWhile { isCollision(cells, it) }.firstOrNull() ?: grain
        return when {
          isFinished(cells, newGrain) -> Cave(cells)
          newGrain == grain           -> { simulate(cells + (grain to SAND), grainSource) }
          else                        -> simulate(cells, newGrain)
        }
      }
      return simulate(cells, grainSource)
    }

    fun grainCount() = cells.values.count { it == SAND }

    override fun toString(): String {
      val xRange = (cells.keys.minBy { it.x }.x)..(cells.keys.maxBy { it.x }.x)
      val yRange = (cells.keys.minBy { it.y }.y)..(cells.keys.maxBy { it.y }.y)
      return buildString {
        yRange.map { y ->
          xRange.map { x ->
            val coordinate = Coordinate(x, y)
            append(when {
                     cells[coordinate] == ROCK -> '▓'
                     cells[coordinate] == SAND -> 'o'
                     else                      -> '░'
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
        return Cave(rocks.associateWith { ROCK })
      }
    }
  }
}