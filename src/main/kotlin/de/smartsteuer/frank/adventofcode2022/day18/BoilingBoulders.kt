package de.smartsteuer.frank.adventofcode2022.day18

import de.smartsteuer.frank.adventofcode2022.day18.Day18.part1
import de.smartsteuer.frank.adventofcode2022.day18.Day18.part2
import de.smartsteuer.frank.adventofcode2022.lines
import kotlin.system.measureTimeMillis

fun main() {
  val input = lines("/adventofcode2022/day18/lava-droplets.txt")

  val volume = Day18.Volume.parse(input)
  volume.renderXSlices()
  volume.renderYSlices()
  volume.renderZSlices()

  measureTimeMillis {
    println("day 18, part 1: ${part1(input)}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 18, part 2: ${part2(input)}")
  }.also { println("took $it ms") }
}

object Day18 {
  fun part1(input: List<String>): Int = Volume.parse(input).surface()

  fun part2(input: List<String>): Int = Volume.parse(input).exteriorSurface()

  data class Cube(val x: Int, val y: Int, val z: Int) {
    fun neighbours() = normals.map { normal -> Cube(x + normal.x, y + normal.y, z + normal.z) }

    data class Delta(val x: Int, val y: Int, val z: Int)

    companion object {
      val normals = listOf(
        Delta(-1,  0,  0),
        Delta( 1,  0,  0),
        Delta( 0, -1,  0),
        Delta( 0,  1,  0),
        Delta( 0,  0, -1),
        Delta( 0,  0,  1),
      )
    }
  }

  data class Volume(val cubes: Set<Cube>) {
    private val xRange = (cubes.minOf { it.x } - 1)..(cubes.maxOf { it.x } + 1)
    private val yRange = (cubes.minOf { it.y } - 1)..(cubes.maxOf { it.y } + 1)
    private val zRange = (cubes.minOf { it.z } - 1)..(cubes.maxOf { it.z } + 1)

    // surface of a cube is number of neighbours that do not exist
    fun surface(): Int = cubes.flatMap { cube -> cube.neighbours() }.count { it !in cubes }

    fun exteriorSurface(): Int =
      findInteriorCells().let { interiorCells -> cubes.flatMap { cube -> cube.neighbours() }.count { it !in cubes && it !in interiorCells } }

    private fun findInteriorCells() = xRange.flatMap { x -> yRange.flatMap { y -> zRange.map { z -> Cube(x, y, z) } } }.toSet() - findExteriorCells() - cubes

    // search for all outside cells by starting at (0, 0, 0) and processing neighbours that are not out of bounds and not an existing cube
    private fun findExteriorCells(): Set<Cube> {
      val outsideCells = mutableSetOf <Cube>()
      val stack = MutableList(1) { Cube(xRange.first, yRange.first, zRange.first) }
      while (stack.isNotEmpty()) {
        val cell = stack.removeLast()
        if (cell !in outsideCells && cell !in cubes && (cell.x in xRange && cell.y in yRange && cell.z in zRange)) {
          outsideCells += cell
          stack += cell.neighbours()
        }
      }
      return outsideCells
    }

    // this is far slower than the mutable iterative variant
    private tailrec fun findExteriorCellsTailRec(cellsToProcess: List<Cube> = List(1) { Cube(xRange.first, yRange.first, zRange.first) },
                                                 outsideCells: Set<Cube> = emptySet()): Set<Cube> {
      if (cellsToProcess.isEmpty()) return outsideCells
      val cell = cellsToProcess.last()
      if (cell !in outsideCells && cell !in cubes && (cell.x in xRange && cell.y in yRange && cell.z in zRange)) {
        return findExteriorCellsTailRec(cellsToProcess.dropLast(1) + cell.neighbours(), outsideCells + cell)
      }
      return findExteriorCellsTailRec(cellsToProcess.dropLast(1), outsideCells)
    }

    companion object {
      fun parse(input: List<String>): Volume =
        Volume(input.map { line ->
          val (x, y, z) = line.split(",").map { it.toInt() }
          Cube(x, y, z)
        }.toSet())
    }

    //------------------------------------------------------------------------
    //----------- the remaining code is just for visualization fun -----------
    //------------------------------------------------------------------------

    fun renderXSlices() {
      val outside = findExteriorCells()
      val inside  = findInteriorCells()
      val separator = "  "
      val slices = xRange.map { x -> renderXSlice(outside, inside, x) }
      slices.forEachIndexed { x, slice ->
        val title = "x = $x".padEnd(slice.first().length)
        print(title)
        print(separator)
      }
      println()
      yRange.forEach { y ->
        slices.forEach { slice ->
          print(slice[y - yRange.first])
          print(separator)
        }
        println()
      }
    }

    private fun renderXSlice(outside: Set<Cube>, inside: Set<Cube>, x: Int): List<String> =
      yRange.map { y ->
        xRange.map { z ->
          when {
            Cube(x, y, z) in outside -> '░'
            Cube(x, y, z) in inside  -> '▓'
            Cube(x, y, z) in cubes   -> '█'
            else                     -> '•'
          }
        }.joinToString(separator = "")
      }

    fun renderYSlices() {
      val outside = findExteriorCells()
      val inside  = findInteriorCells()
      val separator = "  "
      val slices = yRange.map { y -> renderYSlice(outside, inside, y) }
      slices.forEachIndexed { y, slice ->
        val title = "y = $y".padEnd(slice.first().length)
        print(title)
        print(separator)
      }
      println()
      yRange.forEach { x ->
        slices.forEach { slice ->
          print(slice[x - xRange.first])
          print(separator)
        }
        println()
      }
    }

    private fun renderYSlice(outside: Set<Cube>, inside: Set<Cube>, y: Int): List<String> =
      yRange.map { x ->
        xRange.map { z ->
          when {
            Cube(x, y, z) in outside -> '░'
            Cube(x, y, z) in inside  -> '▓'
            Cube(x, y, z) in cubes   -> '█'
            else                     -> '•'
          }
        }.joinToString(separator = "")
      }

    fun renderZSlices() {
      val outside = findExteriorCells()
      val inside  = findInteriorCells()
      val separator = "  "
      val slices = zRange.map { z -> renderZSlice(outside, inside, z) }
      slices.forEachIndexed { z, slice ->
        val title = "z = $z".padEnd(slice.first().length)
        print(title)
        print(separator)
      }
      println()
      yRange.forEach { y ->
        slices.forEach { slice ->
          print(slice[y - yRange.first])
          print(separator)
        }
        println()
      }
    }

    private fun renderZSlice(outside: Set<Cube>, inside: Set<Cube>, z: Int): List<String> =
      yRange.map { y ->
        xRange.map { x ->
          when {
            Cube(x, y, z) in outside -> '░'
            Cube(x, y, z) in inside  -> '▓'
            Cube(x, y, z) in cubes   -> '█'
            else                     -> '•'
          }
        }.joinToString(separator = "")
      }
  }
}
