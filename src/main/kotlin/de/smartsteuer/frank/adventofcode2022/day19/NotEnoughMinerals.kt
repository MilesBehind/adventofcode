package de.smartsteuer.frank.adventofcode2022.day19

import de.smartsteuer.frank.adventofcode2022.day19.Day19.Resource.*
import de.smartsteuer.frank.adventofcode2022.day19.Day19.Robot.*
import de.smartsteuer.frank.adventofcode2022.day19.Day19.part1
import de.smartsteuer.frank.adventofcode2022.day19.Day19.part2
import de.smartsteuer.frank.adventofcode2022.extractNumbers
import de.smartsteuer.frank.adventofcode2022.linesSequence
import de.smartsteuer.frank.adventofcode2022.replaced
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

    val neededResources get() = lazy { listOf(0, costsOre, costsClay, costsObsidian, 0) }

    fun canBeProduced(inventory: Inventory): Boolean =
      neededResources.value.mapIndexed { resource, count -> inventory.resources[resource] >= count }.all { it }

    fun produce(inventory: Inventory): Inventory =
      inventory.copy(resources = inventory.resources.mapIndexed { resource, count -> count - neededResources.value[resource] },
                     robots    = inventory.robots.replaced(produces.ordinal, inventory.robots[produces.ordinal] + 1))

    object NoRobot: Robot { override val produces = NOTHING; override fun toString() = "NoRobot" }
    data class OreRobot     (override val costsOre: Int): Robot { override val produces = ORE }
    data class ClayRobot    (override val costsOre: Int): Robot { override val produces = CLAY }
    data class ObsidianRobot(override val costsOre: Int, override val costsClay:     Int): Robot { override val produces = OBSIDIAN }
    data class GeodeRobot   (override val costsOre: Int, override val costsObsidian: Int): Robot { override val produces = GEODE }
  }

  data class Inventory(val resources: List<Int>, val robots: List<Int>)  // uses resource.ordinal as index



  data class Blueprint(val number: Int, val oreRobot: OreRobot, val clayRobot: ClayRobot, val obsidianRobot: ObsidianRobot, val geodeRobot: GeodeRobot) {

    private val robots = listOf(NoRobot, oreRobot, clayRobot, obsidianRobot, geodeRobot)

    private val maximumNeededResources = listOf(10_000,  // nothing is always on our shopping list
                                                robots.maxOf { it.costsOre },
                                                robots.maxOf { it.costsClay },
                                                robots.maxOf { it.costsObsidian } * 2,
                                                10_000)  // geode is always on our shopping list

    private val startInventory = Inventory(listOf(0, 0, 0, 0, 0),  // NOTHING, ORE, CLAY, OBSIDIAN, GEODE
                                           listOf(0, 1, 0, 0, 0))  // NOTHING, ORE, CLAY, OBSIDIAN, GEODE

    private fun computeMaximumGeodesAfter(minutes: Int): Int {
      val inventory = computeInventoryAfter(minutes, startInventory)
      println("inventory after $minutes minutes: $inventory")
      return inventory.resources[GEODE.ordinal]
    }

    private fun computeInventoryAfter(minutes: Int, inventory: Inventory): Inventory {
      //println("computeInventoryAfter(${24 - minutes + 1}, $inventory)")
      if (minutes == 0) {
        println("time is up, inventory: $inventory")
        return inventory
      }
      val inventoryAfterCollectingResources = inventory.copy(resources = inventory.resources.mapIndexed { resource, count ->
        count + inventory.robots[resource]
      })
      val productionCandidates = robots
        .filter { robot -> robot.canBeProduced(inventory) }
        .filterIndexed { resource, _ -> inventoryAfterCollectingResources.resources[resource] < maximumNeededResources[resource] }
      //println("producibleRobots: $producibleRobots")
      return productionCandidates
        .map { robot ->
          //println("produce $robot...")
          computeInventoryAfter(minutes - 1, robot.produce(inventoryAfterCollectingResources))
        }
        .maxBy { finalInventory -> finalInventory.resources[GEODE.ordinal] }
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
