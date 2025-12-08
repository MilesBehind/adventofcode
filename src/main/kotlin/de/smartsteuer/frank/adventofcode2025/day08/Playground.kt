package de.smartsteuer.frank.adventofcode2025.day08

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Playground.execute(lines("/adventofcode2025/day08/junction-boxes.txt"))
}

object Playground: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseJunctionBoxes()
      .connectNearestBoxes(1_000)
      .first
      .sortedByDescending { it.size }
      .take(3)
      .map { it.size.toLong() }
      .reduce { acc, size -> acc * size }

  override fun part2(input: List<String>): Long =
    input.parseJunctionBoxes()
      .connectNearestBoxes(1_000_000)
      .second
      .let { (box1, box2) -> box1.x.toLong() * box2.x.toLong() }
}

data class Coordinate(val x: Int, val y: Int, val z: Int) {
  infix fun distance(other: Coordinate): Long {
    val xDist = (x - other.x).toLong()
    val yDist = (y - other.y).toLong()
    val zDist = (z - other.z).toLong()
    return xDist * xDist + yDist * yDist + zDist * zDist
  }
}

internal data class JunctionBoxes(val boxes: Set<Coordinate>) {
  fun connectNearestBoxes(maxConnectionCount: Int): Pair<List<Set<Coordinate>>, Pair<Coordinate, Coordinate>> {
    tailrec fun connectCircuits(boxes: List<BoxesWithDistance>, circuits: MutableList<MutableSet<Coordinate>>, count: Int,
                                lastConnectionPair: Pair<Coordinate, Coordinate>? = null): Pair<MutableList<MutableSet<Coordinate>>, Pair<Coordinate, Coordinate>> {
      if (count >= maxConnectionCount || (circuits.size == 1 && circuits.first().size == this.boxes.size)) return circuits to lastConnectionPair!!
      val (box1, box2, _) = boxes.first()
      val targetCircuits = circuits.filter { circuit -> box1 in circuit || box2 in circuit }
      if (targetCircuits.size == 1) {
        targetCircuits.first() += setOf(box1, box2)
      } else if (targetCircuits.isNotEmpty()) {
        circuits -= targetCircuits.toSet()
        circuits.add(targetCircuits.flatten().toMutableSet().apply { addAll(setOf(box1, box2)) })
      } else {
        circuits += mutableSetOf(box1, box2)
      }
      return connectCircuits(boxes.drop(1), circuits, count + 1, box1 to box2)
    }
    val boxesWithDistances = computeDistances().sortedBy { it.distance }
    return connectCircuits(boxesWithDistances, mutableListOf(), 0)
  }

  fun computeDistances(): List<BoxesWithDistance> =
    boxes.flatMapIndexed { index1, box1 ->
      boxes.drop(index1 + 1).map { box2 ->
        BoxesWithDistance(box1, box2, box1 distance box2)
      }
    }.sortedBy { it.distance }

  data class BoxesWithDistance(val box1: Coordinate, val box2: Coordinate, val distance: Long)
}

internal fun List<String>.parseJunctionBoxes(): JunctionBoxes =
  JunctionBoxes(map { line ->
    val (x, y, z) = line.split(",").map { it.toInt() }
    Coordinate(x, y, z)
  }.toSet())