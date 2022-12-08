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
  val visible = mutableSetOf<Coordinate>()

  for (y in 1..(height - 2)) {
    for (x in 1..(width - 2)) {
      val coordinate = Coordinate(x, y)
      if ((coordinate.left().all         { trees.height(it) < trees.height(coordinate) }) ||
          (coordinate.right(width).all   { trees.height(it) < trees.height(coordinate) }) ||
          (coordinate.top().all          { trees.height(it) < trees.height(coordinate) }) ||
          (coordinate.bottom(height).all { trees.height(it) < trees.height(coordinate) })) {
        visible += coordinate
      }
    }
  }
  return visible.size + 2 * width + 2 * height - 4
}

fun part2(input: List<String>): Int {
  val width   = input.first().length
  val height  = input.size
  val trees   = parseTrees(input)
  return (1..(height - 2)).flatMap { y ->
    (1..(width - 2)).map { x ->
      val coordinate     = Coordinate(x, y)
      val distanceLeft   = coordinate.left  (      ).takeWhile { trees.height(it) < trees.height(coordinate) }.size
      val distanceRight  = coordinate.right (width ).takeWhile { trees.height(it) < trees.height(coordinate) }.size
      val distanceTop    = coordinate.top   (      ).takeWhile { trees.height(it) < trees.height(coordinate) }.size
      val distanceBottom = coordinate.bottom(height).takeWhile { trees.height(it) < trees.height(coordinate) }.size
      (distanceLeft   + if (x - distanceLeft > 0)            1 else 0) *
      (distanceRight  + if (x + distanceRight < width - 1)   1 else 0) *
      (distanceTop    + if (y - distanceTop > 0)             1 else 0) *
      (distanceBottom + if (y + distanceBottom < height - 1) 1 else 0)
    }
  }.max()
}

data class Coordinate(val x: Int, val y: Int) {
  fun left()              = if (x > 0)          ((x - 1) downTo 0).map     { Coordinate(it, y) } else emptyList()
  fun right(width: Int)   = if (x < width - 1)  ((x + 1) until width).map  { Coordinate(it, y) } else emptyList()
  fun top()               = if (y > 0)          ((y - 1) downTo 0).map     { Coordinate(x, it) } else emptyList()
  fun bottom(height: Int) = if (y < height - 1) ((y + 1) until height).map { Coordinate(x, it) } else emptyList()
}

typealias TreeGrid = Map<Coordinate, Int>

fun TreeGrid.height(coordinate: Coordinate): Int = get(coordinate) ?: 0

fun parseTrees(input: List<String>): TreeGrid =
  input.flatMapIndexed { y, line ->
    line.mapIndexed { x, height -> Coordinate(x, y) to height.digitToInt() }
  }.toMap()
