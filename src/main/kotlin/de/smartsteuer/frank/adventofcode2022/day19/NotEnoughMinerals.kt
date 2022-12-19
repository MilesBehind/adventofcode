package de.smartsteuer.frank.adventofcode2022.day19

import de.smartsteuer.frank.adventofcode2022.day19.Day19.Robot.*
import de.smartsteuer.frank.adventofcode2022.day19.Day19.part1
import de.smartsteuer.frank.adventofcode2022.day19.Day19.part2
import de.smartsteuer.frank.adventofcode2022.extractNumbers
import de.smartsteuer.frank.adventofcode2022.linesSequence
import java.text.DecimalFormat
import kotlin.system.measureTimeMillis

private const val MINUTES_PART_1 = 24
private const val MINUTES_PART_2 = 32

fun main() {
  measureTimeMillis {
    println("day 19, part 1: ${part1(linesSequence("/adventofcode2022/day19/robot-blueprints.txt"))}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 19, part 2: ${part2(linesSequence("/adventofcode2022/day19/robot-blueprints.txt"))}")
  }.also { println("took $it ms") }
}

@Suppress("SimplifiableCallChain")
object Day19 {
  fun part1(input: Sequence<String>): Int {
    val blueprints = parseBlueprints(input)
    return blueprints.sumOf { blueprint -> blueprint.computeQualityLevel(MINUTES_PART_1) }
  }

  fun part2(input: Sequence<String>): Int {
    val blueprints = parseBlueprints(input.take(3))
    val geodes = blueprints.map { blueprint -> blueprint.computeMaximumGeodes(MINUTES_PART_2) }
    return geodes.fold(1) { product, geode -> product * geode }
  }

  sealed interface Robot {
    val costsOre:      Int get() = 0
    val costsClay:     Int get() = 0
    val costsObsidian: Int get() = 0

    fun canBeProduced(inventory: Inventory): Boolean
    fun produce(inventory: Inventory): Inventory
    fun collect(inventory: Inventory): Inventory

    object NoRobot: Robot {
      override fun canBeProduced(inventory: Inventory) = true
      override fun produce(inventory: Inventory) = inventory
      override fun collect(inventory: Inventory) = inventory
      override fun toString() = "NoRobot"
    }

    data class OreRobot(override val costsOre: Int): Robot {
      override fun canBeProduced(inventory: Inventory) = inventory.ore >= costsOre
      override fun produce(inventory: Inventory) = inventory.copy(ore = inventory.ore - costsOre, oreRobots = inventory.oreRobots + 1)
      override fun collect(inventory: Inventory) = inventory.copy(ore = inventory.ore + 1)
    }

    data class ClayRobot(override val costsOre: Int): Robot  {
      override fun canBeProduced(inventory: Inventory) = inventory.ore >= costsOre
      override fun produce(inventory: Inventory) = inventory.copy(ore = inventory.ore - costsOre, clayRobots = inventory.clayRobots + 1)
      override fun collect(inventory: Inventory) = inventory.copy(clay = inventory.clay + 1)
    }

    data class ObsidianRobot(override val costsOre: Int, override val costsClay: Int): Robot  {
      override fun canBeProduced(inventory: Inventory) = inventory.ore >= costsOre && inventory.clay >= costsClay
      override fun produce(inventory: Inventory) = inventory.copy(ore = inventory.ore - costsOre, clay = inventory.clay - costsClay, obsidianRobots = inventory.obsidianRobots + 1)
      override fun collect(inventory: Inventory) = inventory.copy(obsidian = inventory.obsidian + 1)
    }

    data class GeodeRobot(override val costsOre: Int, override val costsObsidian: Int): Robot  {
      override fun canBeProduced(inventory: Inventory) = inventory.ore >= costsOre && inventory.obsidian >= costsObsidian
      override fun produce(inventory: Inventory) = inventory.copy(ore = inventory.ore - costsOre, obsidian = inventory.obsidian - costsObsidian, geodeRobots = inventory.geodeRobots + 1)
      override fun collect(inventory: Inventory) = inventory.copy(geode = inventory.geode + 1)
    }
  }

  data class Inventory(val ore:       Int, val clay:       Int, val obsidian:       Int, val geode:       Int,
                       val oreRobots: Int, val clayRobots: Int, val obsidianRobots: Int, val geodeRobots: Int): Comparable<Inventory> {

    fun resourceCount(robot: Robot) = when (robot) {
      is NoRobot       -> 0
      is OreRobot      -> ore
      is ClayRobot     -> clay
      is ObsidianRobot -> obsidian
      is GeodeRobot    -> geode
    }

    fun collectResources(minutes: Int = 1) = copy(
      ore      = ore      + minutes * oreRobots,
      clay     = clay     + minutes * clayRobots,
      obsidian = obsidian + minutes * obsidianRobots,
      geode    = geode    + minutes * geodeRobots
    )

    override fun compareTo(other: Inventory): Int {
      val geodeResult = geode.compareTo(other.geode)
      if (geodeResult != 0) return geodeResult
      val obsidianResult = obsidian.compareTo(other.obsidian)
      if (obsidianResult != 0) return obsidianResult
      val clayResult = clay.compareTo(other.clay)
      if (clayResult != 0) return clayResult
      return ore.compareTo(other.ore)
    }
  }

  data class Blueprint(val number: Int, val oreRobot: OreRobot, val clayRobot: ClayRobot, val obsidianRobot: ObsidianRobot, val geodeRobot: GeodeRobot) {

    private val robots = listOf(NoRobot, oreRobot, clayRobot, obsidianRobot, geodeRobot)

    private val maximumNeededResources = listOf(10_000,  // nothing is always on our shopping list
                                                robots.maxOf { it.costsOre } * 2,
                                                robots.maxOf { it.costsClay } * 2,
                                                robots.maxOf { it.costsObsidian } * 2,
                                                10_000)  // geode is always on our shopping list

    private val startInventory = Inventory(ore       = 0, clay       = 0, obsidian       = 0, geode       = 0,
                                           oreRobots = 1, clayRobots = 0, obsidianRobots = 0, geodeRobots = 0)

    fun computeMaximumGeodes(minutes: Int): Int {
      val inventory: Inventory = computeInventory(minutes, startInventory)
      println("after $minutes minutes and ${DecimalFormat().format(calls)} calls: inventory = $inventory")
      return inventory.geode
    }

    private var calls = 0

    private data class State(val minutesLeft: Int, val inventory: Inventory)

    private val stateCache = mutableMapOf<State, Inventory>()

    private fun computeInventory(minutesLeft: Int, inventory: Inventory): Inventory {
      return stateCache.getOrPut(State(minutesLeft, inventory)) {
        calls++
        //if (calls++ % 1_000_000 == 0) println("after ${calls / 1_000_000} million calls")
        if (minutesLeft == 0) return@getOrPut inventory
        val inventoryAfterCollectingResources = inventory.collectResources(1)
        if (geodeRobot.canBeProduced(inventory)) return@getOrPut computeInventory(minutesLeft - 1, geodeRobot.produce(inventoryAfterCollectingResources))
        return@getOrPut robots
          .filter { robot -> robot.canBeProduced(inventory) }
          .filter { robot -> inventoryAfterCollectingResources.resourceCount(robot) < maximumNeededResources[robots.indexOf(robot)] }
          .map { robot -> computeInventory(minutesLeft - 1, robot.produce(inventoryAfterCollectingResources)) }
          .max()
      }
    }

    fun computeQualityLevel(minutes: Int) = computeMaximumGeodes(minutes) * number
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
