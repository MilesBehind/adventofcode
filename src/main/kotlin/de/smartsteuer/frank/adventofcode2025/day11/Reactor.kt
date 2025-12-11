package de.smartsteuer.frank.adventofcode2025.day11

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Reactor.execute(lines("/adventofcode2025/day11/devices.txt"))
}

object Reactor: Day<Long> {
  override fun part1(input: List<String>): Long =
    0 //input.parseDevices().findAllPathsFromYouToOut("you", "out", emptySet())

  override fun part2(input: List<String>): Long =
    input.parseDevices().findAllPathsFromYouToOut("svr", "out", setOf("dac", "fft"))
}

internal data class Device(val name: String)

internal data class Devices(val devices: List<Device>, val connections: Map<Device, List<Device>>) {

  fun findAllCycles(): List<List<Device>> {
    val allCycles = mutableListOf<List<Device>>()
    val visited = mutableSetOf<Device>()
    val recursionStack = mutableSetOf<Device>()
    val currentPath = mutableListOf<Device>()

    fun dfs(device: Device) {
      visited.add(device)
      recursionStack.add(device)
      currentPath.add(device)
      connections[device]?.forEach { neighbor ->
        when (neighbor) {
          in recursionStack -> {
            // Zyklus gefunden
            val cycleStartIndex = currentPath.indexOf(neighbor)
            val cycle = currentPath.subList(cycleStartIndex, currentPath.size) + neighbor
            allCycles.add(cycle)
          }
          // Noch nicht besucht
          !in visited -> dfs(neighbor)
        }
      }
      currentPath.removeAt(currentPath.lastIndex)
      recursionStack.remove(device)
    }
    // Starte DFS von jedem Knoten
    devices.forEach { device ->
      if (device !in visited) {
        dfs(device)
      }
    }
    return allCycles
  }

  fun findAllPathsFromYouToOut(from: String, to: String, vias: Set<String>): Long {
    val start = devices.first { it.name == from }
    val end   = devices.first { it.name == to }

    var count = 0

    tailrec fun findAllPaths(devicesAndVias: MutableList<Pair<Device, Set<Device>>>, visited: MutableSet<String> = mutableSetOf(), result: Long = 0): Long {
      if (devicesAndVias.isEmpty()) return result.also { println("visited: ${visited.size}, $visited") }
      val (device, currentVias) = devicesAndVias.first()
      if (++count % 10_000 == 0) println("after $count rounds: device = $device, devices = ${devicesAndVias.size}, visited: ${visited.size}, result = $result")
      val newResult = result + if (device == end && currentVias.size == vias.size) 1 else 0
      val connectedDevices = connections[device] ?: emptyList()
      visited.addAll(connectedDevices.map { it.name })
      val newDevices = connectedDevices.map { if (device.name in vias) it to (currentVias + device) else it to currentVias }
      devicesAndVias.removeFirst()
      devicesAndVias.addAll(newDevices)
      return findAllPaths(devicesAndVias, visited, newResult)
    }
    return findAllPaths(mutableListOf(start to emptySet()))
  }
}

internal fun List<String>.parseDevices(): Devices {
  val names = map { it.split(": ", " ").map { names -> names.trim() } } + listOf(listOf("out"))
  val devices = names.map { Device(it.first()) }.associateBy { it.name }
  val connections = names.associate { names -> devices.getValue(names.first()) to names.drop(1).map { devices.getValue(it) } }
  return Devices(devices.values.toList(), connections)
}
