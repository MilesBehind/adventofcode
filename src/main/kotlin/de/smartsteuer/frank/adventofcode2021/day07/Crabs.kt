package de.smartsteuer.frank.adventofcode2021.day07

import de.smartsteuer.frank.adventofcode2021.lines
import kotlin.math.absoluteValue

fun main() {
  val positions = lines("/day07/positions.txt").first().splitToSequence(',').map { it.toInt() }.toList()
  val positionWithMinimumFuelConsumption = computePositionWithMinimumFuelConsumption(positions)
  println("position with minimum fuel consumption = $positionWithMinimumFuelConsumption")

  val positionWithMinimumFuelConsumption2 = computePositionWithMinimumFuelConsumption(positions) { it * (it + 1) / 2 }
  println("position with minimum fuel consumption 2 = $positionWithMinimumFuelConsumption2")
}

fun computePositionWithMinimumFuelConsumption(positions: List<Int>, fuelConsumption: (Int) -> Int = { it }): Map.Entry<Int, Int> {
  val min = positions.minOf { it }
  val max = positions.maxOf { it }
  val posToFuel = (min..max).associateWith { candidate ->
    positions.sumOf { position -> fuelConsumption((position - candidate).absoluteValue) }
  }
  println("posToFuel = ${posToFuel.entries.sortedBy { it.value }}")
  val result = posToFuel.minByOrNull { it.value }
  requireNotNull(result)
  return result
}
