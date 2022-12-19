package de.smartsteuer.frank.adventofcode2022.day19

import de.smartsteuer.frank.adventofcode2022.day19.Day19.Blueprint
import de.smartsteuer.frank.adventofcode2022.day19.Day19.Robot.*
import de.smartsteuer.frank.adventofcode2022.day19.Day19.parseBlueprints
import de.smartsteuer.frank.adventofcode2022.day19.Day19.part1
import de.smartsteuer.frank.adventofcode2022.day19.Day19.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class NotEnoughMineralsTest {
  private val input = sequenceOf(
    "Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.",
    "Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 9 * 1 + 12 * 2  // real data: 1659
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 56 * 62  // real data: 6804
  }

  @Test
  fun `blueprints can be parsed`() {
    parseBlueprints(input) shouldBe listOf(
      Blueprint(1, OreRobot(costsOre = 4), ClayRobot(costsOre = 2), ObsidianRobot(costsOre = 3, costsClay = 14), GeodeRobot(costsOre = 2, costsObsidian =  7)),
      Blueprint(2, OreRobot(costsOre = 2), ClayRobot(costsOre = 3), ObsidianRobot(costsOre = 3, costsClay =  8), GeodeRobot(costsOre = 3, costsObsidian = 12))
    )
  }
}

/*

plan 1
           minute
           1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24
    ore    1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24
  1 clay   0  0  0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21
  2 clay   0  0  0  1  2  4  6  8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40
  3 clay   0  0  0  1  2  4  6  9 12 15 18 21 24 27 30 33 36 39 42 45 48 51 54 57

plan 2
           minute
           1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24
    ore    1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24
  1 clay   0  0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22
  2 clay   0  0  1  2  4  6  8 10 12 14 16 18 20 22 24 26 28 30 32 34 36 38 40 42
  3 clay   0  0  1  2  4  6  9 12 15 18 21 24 27 30 33 36 39 42 45 48 51 54 57 60

*/