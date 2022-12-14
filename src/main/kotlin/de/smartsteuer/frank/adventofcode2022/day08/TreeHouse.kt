package de.smartsteuer.frank.adventofcode2022.day08

import de.smartsteuer.frank.adventofcode2022.lines

typealias TreeGrid = Map<Day08.Coordinate, Int>

object Day08 {
  fun main() {
    val input = lines("/adventofcode2022/day08/trees.txt")
    println("day 08, part 1: ${part1(input)}")
    println("day 08, part 2: ${part2(input)}")
  }

  fun part1(input: List<String>): Int {
    val width        = input.first().length
    val height       = input.size
    val trees        = parseTrees(input)
    val box          = Coordinate.box(1..(width - 2), 1..(height - 2))
    val visibilities = box.toSet().filter { coordinate ->
      coordinate.rays(width, height).any { ray -> ray.all { trees.height(it) < trees.height(coordinate) } }
    }
    return visibilities.size + 2 * width + 2 * height - 4
  }

  fun part2(input: List<String>): Int {
    val width  = input.first().length
    val height = input.size
    val trees  = parseTrees(input)
    val box    = Coordinate.box(1..(width - 2), 1..(height - 2))
    return box.maxOf { coordinate ->
      val rays = coordinate.rays(width, height)
      val distancesForRays = rays.map { ray -> ray.asIterable().takeUntil { trees.height(it) >= trees.height(coordinate) }.count() }
      distancesForRays.fold(1) { acc, distance -> acc * distance }
    }
  }

  data class Coordinate(val x: Int, val y: Int) {
    fun rays(width: Int, height: Int): Sequence<Sequence<Coordinate>> = sequenceOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1).map { (deltaX, deltaY) ->
      generateSequence(Coordinate(x + deltaX, y + deltaY)) { coordinate ->
        Coordinate(coordinate.x + deltaX, coordinate.y + deltaY).let { result -> if (result.x in 0 until width && result.y in 0 until height) result else null }
      }
    }
    companion object {
      fun box(horizontalRange: IntRange, verticalRange: IntRange): Sequence<Coordinate> =
        horizontalRange.asSequence().flatMap { x ->
          verticalRange.asSequence().map { y ->
            Coordinate(x, y)
          }
        }
    }
  }

  private fun TreeGrid.height(coordinate: Coordinate): Int = get(coordinate) ?: 0

  private fun parseTrees(input: List<String>): TreeGrid =
    input.flatMapIndexed { y, line ->
      line.mapIndexed { x, height -> Coordinate(x, y) to height.digitToInt() }
    }.toMap()
}

/**
 * Take elements until the predicate evaluates to `false`.
 * Elements will always be taken before the predicate is evaluated.
 * - `emptyList<Int>().takeUntil    { it >= 2 }` is evaluated to `emptyList()`
 * - `listOf(1).takeUntil           { it >= 2 }` is evaluated to `listOf(1)`
 * - `listOf(1, 2).takeUntil        { it >= 2 }` is evaluated to `listOf(1, 2)`
 * - `listOf(1, 2, 3).takeUntil     { it >= 2 }` is evaluated to `listOf(1, 2)`
 * - `listOf(1, 2, 3, 4 ).takeUntil { it >= 2 }` is evaluated to `listOf(1, 2)`
 * @param predicate this predicate will stop taking elements, if it evaluates to `true`
 * @return elements before predicate was evaluated to `true`
 */
fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
  // uses mutable list because this is much simpler, better to understand and works very much like other standard library functions
  val result = mutableListOf<T>()
  for (element in this) {
    result += element
    if (predicate(element)) break
  }
  return result
}

