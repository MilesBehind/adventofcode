package de.smartsteuer.frank.adventofcode2024.day12

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  GardenGroups.execute(lines("/adventofcode2024/day12/garden-map.txt"))
}

object GardenGroups : Day {

  override fun part1(input: List<String>): Long =
    findRegions(input).sumOf { region -> region.area() * region.perimeter() }.toLong()

  override fun part2(input: List<String>): Long =
    findRegions(input).sumOf { region -> region.area() * region.sides() }.toLong()

  data class Pos(val x: Int, val y: Int) {
    fun neighbours() = listOf(Pos(x - 1, y), Pos(x + 1, y), Pos(x, y + 1), Pos(x, y - 1))
  }

  data class Region(val plant: Char, val area: MutableSet<Pos>) {
    operator fun contains(pos: Pos) = pos in area

    fun area(): Int = area.size

    fun perimeter(): Int =
      area.size * 4 - area.fold(0) { sharedEdges, pos -> sharedEdges + pos.neighbours().count { it in area } }

    fun sides(): Int {
      val positionsToEdgeFlags: Map<Pos, List<Boolean>> = area.associateWith { pos -> pos.neighbours().map { it !in area } } // left, right, top, bottom
      val sides = positionsToEdgeFlags.mapValues { (pos, edgeFlags) ->
        val neighbours = pos.neighbours()
        val leftEdge   = edgeFlags[0] && positionsToEdgeFlags[neighbours[3]]?.get(0) != true
        val rightEdge  = edgeFlags[1] && positionsToEdgeFlags[neighbours[2]]?.get(1) != true
        val topEdge    = edgeFlags[2] && positionsToEdgeFlags[neighbours[0]]?.get(2) != true
        val bottomEdge = edgeFlags[3] && positionsToEdgeFlags[neighbours[1]]?.get(3) != true
        listOf(leftEdge, rightEdge, topEdge, bottomEdge)
      }
      return sides.values.flatten().filter { it }.size
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
