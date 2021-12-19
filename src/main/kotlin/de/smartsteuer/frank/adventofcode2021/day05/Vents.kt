package de.smartsteuer.frank.adventofcode2021.day05

import de.smartsteuer.frank.adventofcode2021.lines
import kotlin.math.sign

fun main() {
  val vents                     = lines("/day05/vents.txt").map { it.toVent() }
  val limitedVents              = vents.filter { it.x1 == it.x2 || it.y1 == it.y2 }
  val limitedGrid               = computeGrid(limitedVents)
  val limitedDangerousPositions = limitedGrid.values.count { it > 1 }
  println("limited dangerous positions = $limitedDangerousPositions")
  val grid                      = computeGrid(vents)
  val dangerousPositions        = grid.values.count { it > 1 }
  println("dangerous positions = $dangerousPositions")
}

private fun computeGrid(horizontalAndVerticalVents: List<Vent>): Map<GridPosition, Int> {
  val grid = mutableMapOf<GridPosition, Int>()
  horizontalAndVerticalVents.forEach { vent ->
    vent.gridPositions().forEach { gridPosition ->
      grid[gridPosition] = grid.getOrDefault(gridPosition, 0) + 1
    }
  }
  return grid
}

private data class GridPosition(val x: Int, val y: Int)

private data class Vent(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
  fun gridPositions(): Sequence<GridPosition> {
    val stepX = (x2 - x1).sign
    val stepY = (y2 - y1).sign
    return generateSequence(GridPosition(x1, y1)) { pos ->
      if (pos.x == x2 && pos.y == y2) null else GridPosition(pos.x + stepX, pos.y + stepY)
    }
  }
}

private fun String.toVent(): Vent {
  val values = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex().find(this)?.groupValues
  requireNotNull(values)
  return Vent(values[1].toInt(), values[2].toInt(), values[3].toInt(), values[4].toInt(), )
}
