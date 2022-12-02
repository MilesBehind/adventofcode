package de.smartsteuer.frank.adventofcode2021.day09

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val heightMap = HeightMap(lines("/adventofcode2021/day09/height-map.txt"))
  val lowPoints = heightMap.findHeightsOfLowPoints()
  println("lowPoints = $lowPoints")
  val sum       = lowPoints.sumOf { it + 1 }
  println("sum of all risks: $sum")
  val basinSizes = heightMap.findBasinSizes()
  println("basinSizes = ${basinSizes.sortedDescending()}")
  println("product of 3 largest basin sizes: ${basinSizes.sortedDescending().take(3).reduce(Int::times)}")
}

class HeightMap(text: List<String>) {
  private val coordinates: List<List<Coordinate>> =
      text.mapIndexed { y, line -> line.mapIndexed { x, char -> Coordinate(y, x, char.digitToInt()) } }

  fun get(y: Int, x: Int) =
    coordinates.getOrNull(y)?.getOrNull(x) ?: Coordinate(0, 0, 10)

  fun findHeightsOfLowPoints(): List<Int> =
    findLowPoints().map { it.height }

  private fun findLowPoints(): List<Coordinate> =
    coordinates.flatten().filter { coordinate -> coordinate.neighbours().all { coordinate.height < it.height } }

  fun findBasinSizes(): List<Int> =
    findBasins(findLowPoints(), emptySet(), emptySet()).map { it.size }

  private fun findBasins(lowPoints: List<Coordinate>,
                         growingBasin: Set<Coordinate>, finishedBasins: Set<Set<Coordinate>>): Set<Set<Coordinate>> {
    if (growingBasin.isEmpty()) {
      // create new basin from un-consumed low point
      val lowPoint = lowPoints.find { coordinate -> finishedBasins.all { basin -> coordinate !in basin } } ?: return finishedBasins
      return findBasins(lowPoints - lowPoint, setOf(lowPoint), finishedBasins)
    }
    // try to grow current basin
    val pointsToAdd = growingBasin.fold(setOf()) { acc: Set<Coordinate>, coordinate: Coordinate ->
      acc + coordinate.neighbours().filter { it.height < 9 && it !in growingBasin }
    }
    if (pointsToAdd.isEmpty()) {
      // basin does not grow anymore, so move growing basin to finished basins
      return findBasins(lowPoints, emptySet(), finishedBasins + setOf(growingBasin))
    }
    // add points to growing basin
    return findBasins(lowPoints, growingBasin + pointsToAdd, finishedBasins)
  }

  inner class Coordinate(private val y: Int, private val x: Int, val height: Int) {
    fun neighbours(): Set<Coordinate> =
      setOf(get(y - 1, x), get(y, x + 1), get(y + 1, x), get(y, x - 1))

    override fun toString(): String =
      "($y, $x -> $height)"
  }
}
