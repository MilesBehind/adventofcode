package de.smartsteuer.frank.adventofcode2021.day15

import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

internal class CaveTest {
  private val input = listOf("1163751742",
                             "1381373672",
                             "2136511328",
                             "3694931569",
                             "7463417111",
                             "1319128137",
                             "1359912421",
                             "3125421639",
                             "1293138521",
                             "2311944581")

  @Test
  internal fun `shortest path is correct`() {
    val cave = Cave(input)
    val path = cave.Path().shortestPath()
    path.map { "${it.x}/${it.y}" } shouldContainExactly listOf(
      "0/0", "0/1", "0/2", "1/2", "2/2", "3/2", "4/2", "5/2", "6/2", "6/3", "7/3", "7/4", "8/4", "8/5", "8/6", "8/7", "8/8", "9/8", "9/9"
    )
    val riskLevelOfShortestPath = cave.riskLevelOfShortestPath()
    riskLevelOfShortestPath shouldBe 40
  }

  @Test
  internal fun `shortest path for repeated cave is correct`() {
    val cave = Cave(input, 5, 5)
    //val path = cave.Path().shortestPath()
    //path.map { "${it.x}/${it.y}" } shouldContainExactly listOf(
    //  "0/0", "0/1", "0/2", "1/2", "2/2", "3/2", "4/2", "5/2", "6/2", "6/3", "7/3", "7/4", "8/4", "8/5", "8/6", "8/7", "8/8", "9/8", "9/9"
    //)
    val riskLevelOfShortestPath = cave.riskLevelOfShortestPath()
    riskLevelOfShortestPath shouldBe 315
  }

  @Test
  internal fun `wrap wraps values to expected results`() {
    (1..20).map { it.wrap(1..9) } shouldBe listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2)
  }
}