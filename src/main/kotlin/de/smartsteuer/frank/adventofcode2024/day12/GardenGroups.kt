package de.smartsteuer.frank.adventofcode2024.day12

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  GardenGroups.execute(lines("/adventofcode2024/day12/garden-map.txt"))
}

object GardenGroups : Day<Long> {

  override fun part1(input: List<String>): Long =
    findRegions(input).sumOf { region -> region.area() * region.perimeter() }.toLong()

  override fun part2(input: List<String>): Long =
    findRegions(input).sumOf { region -> region.area() * region.sides() }.toLong()

  data class Pos(val x: Int, val y: Int) {
    companion object {
      val neighboursDelta     = listOf(Pos(-1, 0), Pos(1, 0), Pos(0, 1), Pos(0, -1))
      val edgeNeighboursDelta = neighboursDelta.map { it.turnLeft() }
    }
    private fun turnLeft() = Pos(y, -x)
    fun neighbours()     = neighboursDelta.map     { Pos(x + it.x, y + it.y) }
    fun edgeNeighbours() = edgeNeighboursDelta.map { Pos(x + it.x, y + it.y) }
  }

  data class Region(val plant: Char, val area: MutableSet<Pos>) {
    operator fun contains(pos: Pos) = pos in area

    fun area(): Int = area.size

    fun perimeter(): Int =
      area.size * 4 - area.fold(0) { sharedEdges, pos -> sharedEdges + pos.neighbours().count { it in area } }

    fun sides(): Int {
      val positionsToEdgeFlags: Map<Pos, List<Boolean>> = area.associateWith { pos -> pos.neighbours().map { it !in area } }
      val sides = positionsToEdgeFlags.entries.map { (pos, edgeFlags) ->
        val edgeNeighbours = pos.edgeNeighbours()
        edgeFlags.withIndex().count { (index, edgeFlag) ->
          edgeFlag && positionsToEdgeFlags[edgeNeighbours[index]]?.get(index) != true
        }
      }
      return sides.sum()
    }

    fun hasContactTo(regionBelow: Region): Boolean =
      regionBelow.plant == plant && regionBelow.area.any { Pos(it.x, it.y - 1) in area }

    fun merge(regions: List<Region>) =
      this.also { regions.forEach { area.addAll(it.area) } }
  }

  fun findRegions(map: List<String>): List<Region> =
    map.drop(1).foldIndexed(findRegions(map.first(), 0).toMutableList()) { y, regions, line ->
      regions.also {
        findRegions(line, y + 1).forEach { region ->
          val regionsAbove = regions.filter { regionAbove -> regionAbove.hasContactTo(region) }
          if (regionsAbove.isNotEmpty()) {
            regions -= regionsAbove.toSet()
            regions += region.merge(regionsAbove)
          } else {
            regions += region
          }
        }
      }
    }

  private fun findRegions(line: String, y: Int): List<Region> =
    line.drop(1).foldIndexed(mutableListOf(Region(line.first(), mutableSetOf(Pos(0, y))))) { x, regions, plant ->
      regions.also {
        if (plant == regions.last().plant) regions.last().area += Pos(x + 1, y) else regions.apply { add(Region(plant, mutableSetOf(Pos(x + 1, y)))) }
      }
    }
}
