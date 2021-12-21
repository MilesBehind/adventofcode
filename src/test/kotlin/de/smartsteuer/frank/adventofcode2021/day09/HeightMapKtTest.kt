package de.smartsteuer.frank.adventofcode2021.day09

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Test

internal class HeightMapKtTest {

  private val heightMap1 = HeightMap(listOf("2199943210",
                                            "3987894921",
                                            "9856789892",
                                            "8767896789",
                                            "9899965678"))

  private val heightMap2 = HeightMap(listOf("2199943210",
                                            "3987894921",
                                            "9856789892",
                                            "8767886789",  // replaced 9 with 8, so basin should contain 2 low points
                                            "9899965678"))

  @Test
  internal fun `correct low points are found for a height map 1`() {
    heightMap1.findHeightsOfLowPoints() shouldContainExactlyInAnyOrder listOf(1, 0, 5, 5)
  }

  @Test
  internal fun `correct basin sizes are found for a height map 1`() {
    heightMap1.findBasinSizes() shouldContainExactlyInAnyOrder listOf(3, 9, 14, 9)
  }

  // ••999•••••
  // •9•••9•9••
  // 9•••••989•
  // •••••••••9
  // 9•999•••••
  @Test
  internal fun `correct basin sizes are found for a height map 2`() {
    heightMap2.findBasinSizes() shouldContainExactlyInAnyOrder listOf(3, 9, 24)
  }

  // 2199943210
  // 39•••94921
  // 9•••••9892
  // •••••86789
  // 9•99965678
}