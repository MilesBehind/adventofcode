package de.smartsteuer.frank.adventofcode2021.day15

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
    val riskLevelOfPathWithLowestRisk = cave.lowestRiskPathRisk()
    riskLevelOfPathWithLowestRisk shouldBe 40
  }

  @Test
  internal fun `shortest path for repeated cave is correct`() {
    val cave = Cave(input, repeat = true)
    val riskLevelOfPathWithLowestRisk = cave.lowestRiskPathRisk()
    riskLevelOfPathWithLowestRisk shouldBe 315
  }
}