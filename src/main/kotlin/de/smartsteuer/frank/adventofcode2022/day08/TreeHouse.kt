package de.smartsteuer.frank.adventofcode2022.day08

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val input = lines("/adventofcode2022/day08/trees.txt")
  println("day 08, part 1: ${part1(input)}")
  println("day 08, part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
  val width   = input.first().length
  val height  = input.size
  val trees   = parseTrees(input)
  tailrec fun computeVisibilities(coordinates: Iterator<Coordinate>, result: Set<Coordinate>): Set<Coordinate> {
    if (!coordinates.hasNext()) return result
    val coordinate = coordinates.next()
    val newResult =
      if ((coordinate.left().all         { trees.height(it) < trees.height(coordinate) }) ||
          (coordinate.right(width).all   { trees.height(it) < trees.height(coordinate) }) ||
          (coordinate.top().all          { trees.height(it) < trees.height(coordinate) }) ||
          (coordinate.bottom(height).all { trees.height(it) < trees.height(coordinate) })) result + coordinate
      else result
    return computeVisibilities(coordinates, newResult)
  }
  val box = Coordinate.box(1..(width - 2), 1..(height - 2))
  return computeVisibilities(box.iterator(), emptySet()).size + 2 * width + 2 * height - 4
}

fun part2(input: List<String>): Int {
  val width   = input.first().length
  val height  = input.size
  val trees   = parseTrees(input)
  tailrec fun computeViewDistances(coordinates: Iterator<Coordinate>, result: List<Int>): List<Int> {
    if (!coordinates.hasNext()) return result
    val coordinate = coordinates.next()
    val distanceLeft   = coordinate.left  (      ).takeWhile { trees.height(it) < trees.height(coordinate) }.count()
    val distanceRight  = coordinate.right (width ).takeWhile { trees.height(it) < trees.height(coordinate) }.count()
    val distanceTop    = coordinate.top   (      ).takeWhile { trees.height(it) < trees.height(coordinate) }.count()
    val distanceBottom = coordinate.bottom(height).takeWhile { trees.height(it) < trees.height(coordinate) }.count()
    val viewDistance = (distanceLeft   + if (coordinate.x - distanceLeft > 0)            1 else 0) *
                       (distanceRight  + if (coordinate.x + distanceRight < width - 1)   1 else 0) *
                       (distanceTop    + if (coordinate.y - distanceTop > 0)             1 else 0) *
                       (distanceBottom + if (coordinate.y + distanceBottom < height - 1) 1 else 0)
    return computeViewDistances(coordinates, result + viewDistance)
  }
  val box = Coordinate.box(1..(width - 2), 1..(height - 2))
  return computeViewDistances(box.iterator(), emptyList()).max()
}

data class Coordinate(val x: Int, val y: Int) {
  fun left()              = if (x > 0)          ((x - 1) downTo     0).asSequence().map { Coordinate(it, y) } else emptySequence()
  fun right(width: Int)   = if (x < width - 1)  ((x + 1) until  width).asSequence().map { Coordinate(it, y) } else emptySequence()
  fun top()               = if (y > 0)          ((y - 1) downTo     0).asSequence().map { Coordinate(x, it) } else emptySequence()
  fun bottom(height: Int) = if (y < height - 1) ((y + 1) until height).asSequence().map { Coordinate(x, it) } else emptySequence()
  companion object {
    fun box(horizontalRange: IntRange, verticalRange: IntRange): Sequence<Coordinate> =
      horizontalRange.asSequence().flatMap { x ->
        verticalRange.asSequence().map { y ->
          Coordinate(x, y)
        }
      }
  }
}

typealias TreeGrid = Map<Coordinate, Int>

fun TreeGrid.height(coordinate: Coordinate): Int = get(coordinate) ?: 0

fun parseTrees(input: List<String>): TreeGrid =
  input.flatMapIndexed { y, line ->
    line.mapIndexed { x, height -> Coordinate(x, y) to height.digitToInt() }
  }.toMap()
