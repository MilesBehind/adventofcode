package de.smartsteuer.frank.adventofcode2023.day22

import de.smartsteuer.frank.adventofcode2022.day19.component6
import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val bricks = parseBricks(lines("/adventofcode2023/day22/bricks.txt"))
  measureTime { println("part 1: ${part1(bricks)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(bricks)}") }.also { println("part 2 took $it") }
}

internal fun part1(bricks: List<Brick>): Int =
  findSafelyDisintegratableBricks(settle(bricks).bricks).size

internal fun part2(bricks: List<Brick>): Int =
  countFallingBricks(settle(bricks).bricks).values.sum()

internal data class BricksAndMovements(val bricks: List<Brick>, val movements: Int)

internal fun settle(bricks: List<Brick>): BricksAndMovements {
  tailrec fun settle(bricksToSettle: List<Brick>, occupied: MutableSet<Pos>, movements: Int, result: List<Brick>): BricksAndMovements {
    if (bricksToSettle.isEmpty()) return BricksAndMovements(result, movements)
    val brick = bricksToSettle.first()
    val movedBrick = ((-1 downTo -brick.minZ + 1)).map { deltaZ -> brick.moveZBy(deltaZ) }.takeWhile { movedBrick ->
      movedBrick.minArea().none { pos -> pos in occupied }
    }.lastOrNull()
    if (movedBrick != null) {
      occupied.addAll(movedBrick.volume())
      return settle(bricksToSettle.drop(1), occupied, movements + 1, result + movedBrick)
    } else {
      occupied.addAll(brick.volume())
      return settle(bricksToSettle.drop(1), occupied, movements, result + brick)
    }
  }
  return settle(bricks.sortedBy { it.minZ }, mutableSetOf(), 0, emptyList())
}

internal fun findBricksThatCauseFallingBricks(bricks: List<Brick>): Set<Brick> {
  fun Brick.findSupporters(): List<Brick> =
    moveZBy(-1).minArea().let { searchSpace ->
      bricks.filter { candidate ->
        candidate.maxArea().any { it in searchSpace }
      }
    }

  val bricksToSupporters = bricks.associateWith { it.findSupporters() }
  return bricksToSupporters.values.filter { it.size == 1 }.mapNotNull { it.firstOrNull() }.toSet()
}

internal fun findSafelyDisintegratableBricks(bricks: List<Brick>): List<Brick> {
  val singleSupporters = findBricksThatCauseFallingBricks(bricks)
  return (bricks.toSet() - singleSupporters).toList()
}

internal fun countFallingBricks(bricks: List<Brick>): Map<Brick, Int> {
  fun countFallingBricks(disintegratedBrick: Brick): Int {
    return settle(bricks - disintegratedBrick).movements
  }
  val singleSupporters = findBricksThatCauseFallingBricks(bricks)
  return singleSupporters.associateWith { brick -> countFallingBricks(brick) }
}


internal data class Pos(val x: Int, val y: Int, val z: Int) {
  fun moveByZ(deltaZ: Int) = copy(z = z + deltaZ)
}

internal data class Brick(val from: Pos, val to: Pos) {
  private  val minX = if (from.x < to.x) from.x else to.x
  private  val maxX = if (from.x > to.x) from.x else to.x
  private  val minY = if (from.y < to.y) from.y else to.y
  private  val maxY = if (from.y > to.y) from.y else to.y
  internal val minZ = if (from.z < to.z) from.z else to.z
  internal val maxZ = if (from.z > to.z) from.z else to.z
  fun minArea(): List<Pos> = (minX..maxX).flatMap { x -> (minY..maxY).map { y -> Pos(x, y, minZ) } }
  fun maxArea(): List<Pos> = (minX..maxX).flatMap { x -> (minY..maxY).map { y -> Pos(x, y, maxZ) } }
  fun volume():  List<Pos> = (minX..maxX).flatMap { x -> (minY..maxY).flatMap { y -> (minZ..maxZ).map { z -> Pos(x, y, z) } } }
  fun moveZBy(deltaZ: Int) = Brick(from.moveByZ(deltaZ), to.moveByZ(deltaZ))
}

internal fun Set<Pos>.visualize() {
  if (isEmpty()) return
  val minX = this.minOf { it.x }
  val maxX = this.maxOf { it.x }
  val minY = this.minOf { it.y }
  val maxY = this.maxOf { it.y }
  val minZ = this.minOf { it.z }
  val maxZ = this.maxOf { it.z }
  (minY..maxY).forEach { y ->
    (minZ..maxZ).forEach { z ->
      print("${z.toString().padStart(5, ' ')}  ")
      (minX..maxX).forEach { x ->
        print(if (Pos(x, y, z) in this) '#' else '.')
      }
    }
    println("")
  }
}

internal fun parseBricks(lines: List<String>): List<Brick> =
  lines.map { line ->
    val (x1, y1, z1, x2, y2, z2) = line.split(",", "~").map { it.toInt() }
    Brick(Pos(x1, y1, z1), Pos(x2, y2, z2))
  }

operator fun <T> List<T>.component6() = get(5)
