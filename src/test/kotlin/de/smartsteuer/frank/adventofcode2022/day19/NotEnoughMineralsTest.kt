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
