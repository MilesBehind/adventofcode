package de.smartsteuer.frank.adventofcode2022.day16

import de.smartsteuer.frank.adventofcode2022.*
import de.smartsteuer.frank.adventofcode2022.day16.Day16.part1
import de.smartsteuer.frank.adventofcode2022.day16.Day16.part2
import java.io.File


fun main() {
  val input = lines("/adventofcode2022/day16/valves.txt")
  println("day 16, part 1: ${part1(input)}")
  println("day 16, part 2: ${part2(input)}")
}

const val MINUTES_AVAILABLE         = 30
const val VALVE_OPEN_NEEDED_MINUTES = 1
const val ELEPHANT_TEACHING_MINUTES = 4

object Day16 {
  fun part1(input: List<String>): Int {
    val valves = Valves.parseValves(input)
    File("valves.tgf").writeText(valves.renderAsTgf())
    return valves.maximumPressure()
  }

  fun part2(input: List<String>): Int = Valves.parseValves(input).maximumPressureWithElephant()

  data class Valves(val valves: Map<String, Valve>, val tunnels: Set<Tunnel>, val start: Valve) {

    // distances between all pairs of nodes, that can reach each other
    private val distances: Map<Pair<Valve, Valve>, Int> =
      GraphAlgorithms.floydMarshall(valves.values.toList(), tunnels.toList()).nodeInfos.mapValues { it.value.distance }

    data class Valve(val name: String, val rate: Int)

    data class Tunnel(val valves: Set<Valve>, val length: Int): GraphAlgorithms.Edge<Valve> {
      constructor(valve1: Valve, valve2: Valve, length: Int): this(setOf(valve1, valve2), length)
      override fun equals(other: Any?) = other is Tunnel && valves == other.valves
      override fun hashCode(): Int = valves.hashCode()
      override fun from() = valves.first()
      override fun to() = valves.drop(1).first()
      override fun distance() = length
    }

    fun maximumPressure(): Int = maximumPressure(MINUTES_AVAILABLE) { emptyList<Valve>() to 0 }

    fun maximumPressureWithElephant(): Int =
      maximumPressure(MINUTES_AVAILABLE - ELEPHANT_TEACHING_MINUTES) { valves ->
        maximumPressure(start, valves, MINUTES_AVAILABLE - ELEPHANT_TEACHING_MINUTES) { emptyList<Valve>() to 0 }
      }

    private fun maximumPressure(minutesAvailable: Int, defaultIfTimeIsOut: (valves: List<Valve>) -> Pair<List<Valve>, Int>): Int {
      val valvesToOpen = valves.values.filter { it.rate > 0 }
      val (openValves, maximumPressure) = maximumPressure(start, valvesToOpen, minutesAvailable, defaultIfTimeIsOut)
      println("pressure for valves ${openValves.map { it.name }} is $maximumPressure")
      return maximumPressure
    }

    private fun maximumPressure(valve:              Valve,
                                valves:             List<Valve>,
                                remainingMinutes:   Int,
                                defaultIfTimeIsOut: (valves: List<Valve>) -> Pair<List<Valve>, Int>): Pair<List<Valve>, Int> {
      return valves
        .eachAndOthers()
        .filter { (nextValve, _) -> distances.getValue(valve to nextValve) < remainingMinutes }
        .map { (nextValve, remainingValves) ->
          val distance = distances.getValue(valve to nextValve)
          val newRemainingMinutes = remainingMinutes - distance - VALVE_OPEN_NEEDED_MINUTES
          val (open, pressure) = maximumPressure(nextValve, remainingValves, newRemainingMinutes, defaultIfTimeIsOut)
          (listOf(nextValve) + open) to (nextValve.rate * newRemainingMinutes + pressure)
        }
        .maxByOrNull { (_, pressure) -> pressure } ?: defaultIfTimeIsOut(valves)
    }

    fun renderAsTgf(): String = buildString {
      val valvesToId = valves.values.mapIndexed { index, valve -> valve to index }.toMap()
      valvesToId.forEach { (valve, id) -> appendLine("$id ${valve.name} ${valve.rate}") }
      appendLine("#")
      tunnels.forEach { tunnel ->
        tunnel.valves.forEach { append("${valvesToId[it]} ") }
        appendLine(tunnel.length)
      }
    }

    companion object {
      val x = listOf(1, 2, 3)
      data class ValveData(val name: String, val rate: Int, val targetValves: List<String>)
      fun parseValves(input: List<String>): Valves {
        @Suppress("RegExpUnnecessaryNonCapturingGroup")
        val temporaryValves: List<ValveData> = """Valve (\w+) has flow rate=(\d+); tunnel(?:s?) lead(?:s?) to valve(?:s?) (.+)""".toRegex().let { regex ->
          input.extractStrings(regex).map { (sourceValve, rate, targetValves) ->
            ValveData(sourceValve, rate.toInt(), targetValves.split(",").map { it.trim() })
          }
        }
        val valves  = temporaryValves.map { valve -> Valve(valve.name, valve.rate) }.associateBy { it.name }
        val tunnels = temporaryValves.flatMap { valve ->
          valve.targetValves.map { targetName -> Tunnel(valves.getValue(valve.name), valves.getValue(targetName), 1) }
        }.toSet()
        return Valves(valves, tunnels, valves.getValue("AA"))
      }
    }
  }
}
