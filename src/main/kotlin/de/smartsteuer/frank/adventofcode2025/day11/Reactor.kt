package de.smartsteuer.frank.adventofcode2025.day11

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Reactor.execute(lines("/adventofcode2025/day11/devices.txt"))
}

object Reactor: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseDevices().countPaths("you", "out")

  override fun part2(input: List<String>): Long =
    input.parseDevices().let { devices ->
      val fftToDac = devices.countPaths("fft", "dac")
      if (fftToDac > 0) devices.countPaths("svr", "fft") * fftToDac *                         devices.countPaths("dac", "out")
      else              devices.countPaths("svr", "dac") * devices.countPaths("dac", "fft") * devices.countPaths("fft", "out")
    }

  internal fun List<String>.parseDevices() =
    Devices(associate { line ->
      val (from, to) = line.split(": ")
      from to to.split(" ")
    })
}

internal data class Devices(val connections: Map<String, List<String>>) {
  fun countPaths(from: String, to: String, cache: MutableMap<String, Long> = mutableMapOf()): Long =
    if (from == to) 1 else cache.getOrPut(from) {
      connections.getOrDefault(from, emptyList()).sumOf { target ->
        countPaths(target, to, cache)
      }.also { cache[from] = it }
    }
}