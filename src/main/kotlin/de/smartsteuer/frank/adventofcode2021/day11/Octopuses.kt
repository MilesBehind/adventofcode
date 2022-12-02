@file:Suppress("unused", "UNUSED_VARIABLE") // TODO

package de.smartsteuer.frank.adventofcode2021.day11

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val energyLevels = lines("/adventofcode2021/day11/energy-levels.txt")
  val octopuses = Octopuses(energyLevels)
}


data class Octopuses constructor(private val octopuses: Map<Coordinate, Octopus>,
                                 private val width: Int, private val height: Int, val flashCount: Int) {
  constructor(energyLevels: List<String>):
          this(energyLevels.mapIndexed { y, line ->
            line.mapIndexed { x, char ->
              Coordinate(x, y) to Octopus(char.digitToInt())
            }
          }.flatten().associate { it }, energyLevels.first().length, energyLevels.size, 0)

  private val virtualBorderOctopus = Octopus(-1)

  data class Coordinate(val x: Int, val y: Int) {
    fun neighbours() = listOf(
      Coordinate(x, y - 1), Coordinate(x + 1, y - 1), Coordinate(x + 1, y), Coordinate(x + 1, y + 1),
      Coordinate(x, y + 1), Coordinate(x - 1, y + 1), Coordinate(x - 1, y), Coordinate(x - 1, y - 1)
    )
  }

  private fun octopusAt(coordinate: Coordinate, octopuses: Map<Coordinate, Octopus>) = octopuses[coordinate] ?: virtualBorderOctopus

  @JvmInline
  value class Octopus(val energyLevel: Int) {
    fun withAdditionalEnergy(energy: Int) = Octopus(if (energyLevel + energy > 9) 0 else energyLevel + energy)
    override fun toString() = energyLevel.toString()
  }

  private fun neighbours(coordinate: Coordinate, octopuses: Map<Coordinate, Octopus>) = coordinate.neighbours().map { octopusAt(it, octopuses) }

  @Suppress("USELESS_CAST") // kotlin plugin bug?
  fun step(): Octopuses {
    val octopusesAfterPhase1 = octopuses.mapValues { (_, octopus) -> octopus.withAdditionalEnergy(1) }
    return Octopuses(octopuses, width, height, flashCount)
  }

  override fun toString() = buildString {
    append("\nOctopuses(")
    (0 until height).forEach { y ->
      (0 until width).forEach { x ->
        append(octopuses[Coordinate(x, y)]?.energyLevel)
      }
      if (y < width - 1) append("\n          ")
    }
    append(")")
  }
}