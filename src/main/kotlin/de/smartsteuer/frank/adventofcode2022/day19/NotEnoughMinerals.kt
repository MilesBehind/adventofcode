package de.smartsteuer.frank.adventofcode2022.day19

import de.smartsteuer.frank.adventofcode2022.day19.Day19.Resource.*
import de.smartsteuer.frank.adventofcode2022.day19.Day19.Robot.*
import de.smartsteuer.frank.adventofcode2022.day19.Day19.part1
import de.smartsteuer.frank.adventofcode2022.day19.Day19.part2
import de.smartsteuer.frank.adventofcode2022.extractNumbers
import de.smartsteuer.frank.adventofcode2022.linesSequence
import kotlin.system.measureTimeMillis

private const val MINUTES = 24

fun main() {
  measureTimeMillis {
    println("day 19, part 1: ${part1(linesSequence("/adventofcode2022/day19/robot-blueprints.txt"))}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 19, part 2: ${part2(linesSequence("/adventofcode2022/day19/robot-blueprints.txt"))}")
  }.also { println("took $it ms") }
}

object Day19 {
  fun part1(input: Sequence<String>): Int {
    val blueprints = parseBlueprints(input)
    return blueprints.sumOf { blueprint -> blueprint.computeQualityLevel(MINUTES) }
  }

  fun part2(input: Sequence<String>): Int = 0

  enum class Resource { NOTHING, ORE, CLAY, OBSIDIAN, GEODE }

  sealed interface Robot {
    val costsOre:      Int get() = 0
    val costsClay:     Int get() = 0
    val costsObsidian: Int get() = 0
    val produces:      Resource

    val neededResources get() = lazy { mapOf(ORE to costsOre, CLAY to costsClay, OBSIDIAN to costsObsidian, GEODE to 0) }

    fun canBeProduced(inventory: Inventory): Boolean =
      neededResources.value.all { (resource, count) -> inventory.resources.getValue(resource) >= count }

    fun produce(inventory: Inventory): Inventory =
      inventory.copy(resources = inventory.resources.mapValues { (resource, count) -> count - neededResources.value.getOrDefault(resource, 0) },
                     robots    = inventory.robots.mapValues    { (resource, count) -> if (resource == produces) count + 1 else count })

    object NoRobot: Robot { override val produces = NOTHING }
    data class OreRobot     (override val costsOre: Int): Robot { override val produces = ORE }
    data class ClayRobot    (override val costsOre: Int): Robot { override val produces = CLAY }
    data class ObsidianRobot(override val costsOre: Int, override val costsClay:     Int): Robot { override val produces = OBSIDIAN }
    data class GeodeRobot   (override val costsOre: Int, override val costsObsidian: Int): Robot { override val produces = GEODE }
  }


  data class Inventory(val resources: Map<Resource, Int>, val robots: Map<Resource, Int>)

  data class Blueprint(val number: Int, val oreRobot: OreRobot, val clayRobot: ClayRobot, val obsidianRobot: ObsidianRobot, val geodeRobot: GeodeRobot) {

    private val robots = listOf(GEODE to geodeRobot, OBSIDIAN to obsidianRobot, CLAY to clayRobot, ORE to oreRobot)

    private val startInventory = Inventory(mapOf(ORE to 0, CLAY to 0, OBSIDIAN to 0, GEODE to 0),
                                           mapOf(ORE to 1, CLAY to 0, OBSIDIAN to 0, GEODE to 0))

    private fun computeMaximumGeodesAfter(minutes: Int): Int {
      val inventory = computeInventoryAfter(minutes, startInventory)
      println("inventory after $minutes minutes: $inventory")
      return inventory.resources.getOrDefault(GEODE, 0)
    }

    var calls = 0

    private fun computeInventoryAfter(minutes: Int, inventory: Inventory): Inventory {
      if (calls++ > 1_000) {
        println("aborting after 1000 calls")
        return inventory
      }
      if (minutes == 0) return inventory
      println("computeInventoryAfter($minutes, $inventory)")
      val inventoryAfterCollectingResources = inventory.copy(resources = inventory.resources.mapValues { (resource, count) ->
        count + inventory.robots.getOrDefault(resource, 0)
      })
      val producibleRobots = robots.filter { (_, robot) -> robot.canBeProduced(inventory) }.map { it.second }
      println("producibleRobots: $producibleRobots")
      if (producibleRobots.isEmpty()) {
        println("no robots can be produced")
        return computeInventoryAfter(minutes - 1, inventoryAfterCollectingResources)
      }
      return producibleRobots
        .map { robot -> println("produce $robot..."); computeInventoryAfter(minutes - 1, robot.produce(inventoryAfterCollectingResources)) }
        .maxBy { finalInventory -> finalInventory.resources.getOrDefault(GEODE, 0) }
    }

    fun computeQualityLevel(minutes: Int) = computeMaximumGeodesAfter(minutes) * number
  }

  fun parseBlueprints(input: Sequence<String>): List<Blueprint> =
    """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex().let { regex ->
      input.extractNumbers(regex).map { (number, oreRobotOre, clayRobotOre, obsidianRobotOre, obsidianRobotClay, geodeRobotOre, geodeRobotObsidian) ->
        Blueprint(number,
                  OreRobot(oreRobotOre),
                  ClayRobot(clayRobotOre),
                  ObsidianRobot(obsidianRobotOre, obsidianRobotClay),
                  GeodeRobot(geodeRobotOre, geodeRobotObsidian))
      }
    }.toList()
}

operator fun <T> List<T>.component6() = get(5)
operator fun <T> List<T>.component7() = get(6)
