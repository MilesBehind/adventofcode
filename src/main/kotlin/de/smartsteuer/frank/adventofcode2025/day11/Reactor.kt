package de.smartsteuer.frank.adventofcode2025.day11

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Reactor.execute(lines("/adventofcode2025/day11/devices.txt"))
}

object Reactor: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseDevices().findAllPaths("you", "out", emptySet())

  override fun part2(input: List<String>): Long =
    0 //input.parseDevices().findAllPaths("svr", "out", setOf("dac", "fft"))
}

internal data class Device(val name: String) {
  override fun toString(): String = name
}

internal data class Devices(val devices: List<Device>, val connections: Map<Device, List<Device>>) {

  data class VisitedDevice(val visitCount: Int = 0, val visitedVias: Map<String, Int> = emptyMap()) {
    override fun toString(): String = "$visitCount/$visitedVias"
  }

  fun findAllPaths(from: String, to: String, requiredVias: Set<String>): Long {
    val start = devices.first { it.name == from }
    val end   = devices.first { it.name == to }
    val reverseConnections = connections.reverse()

    tailrec fun findAllPaths(devices: Set<Device>, visited: MutableMap<String, VisitedDevice>): Long {
      println("findAllPaths($devices, $visited)")
      if (devices.isEmpty()) {
        val directConnections = connections[start]?.toSet() ?: emptySet()
        val directVisited = visited.filterKeys { name -> name in directConnections.map { it.name } }
        if (requiredVias.isEmpty()) return directVisited.map { (name, visitedDevice) -> visitedDevice.visitedVias.getOrDefault(name, 0) }.sum().toLong()
        return 0
      }
      val nextDevices = mutableSetOf<Device>()
      devices.forEach { device ->
        val visitedDevice = visited.getOrDefault(device.name, VisitedDevice())
        val visitedVias   = visitedDevice.visitedVias
        reverseConnections[device]?.forEach { connected ->
          val oldVisitedDevice = visited.getOrDefault(connected.name, VisitedDevice(0, visitedVias))
          val newVisitCount    = oldVisitedDevice.visitCount + 1
          val newVisitedVias   = oldVisitedDevice.visitedVias.toMutableMap()
          newVisitedVias[connected.name] = newVisitedVias.getOrDefault(connected.name, 0) + 1
          visited[connected.name] = VisitedDevice(newVisitCount, newVisitedVias)
          if (visited[connected.name]?.visitCount == connections[connected]?.size) {
            nextDevices += connected
          }
          println("$device --> $connected:   visit count = ${visited[connected.name]?.visitCount}, visited vias = ${visited[connected.name]?.visitedVias}")
        }
      }
      return findAllPaths(nextDevices, visited)
    }
    return findAllPaths(setOf(end), mutableMapOf(end.name to VisitedDevice(1, mapOf(end.name to 1))))
  }
}

internal fun <K, V> Map<K, List<V>>.reverse(): Map<V, List<K>> =
  flatMap { (key, values) -> values.map { value -> value to key } }
    .groupBy({ it.first }, { it.second })


internal fun List<String>.parseDevices(): Devices {
  val names = map { it.split(": ", " ").map { names -> names.trim() } } + listOf(listOf("out"))
  val devices = names.map { Device(it.first()) }.associateBy { it.name }
  val connections = names.associate { names -> devices.getValue(names.first()) to names.drop(1).map { devices.getValue(it) } }
  return Devices(devices.values.toList(), connections)
}
