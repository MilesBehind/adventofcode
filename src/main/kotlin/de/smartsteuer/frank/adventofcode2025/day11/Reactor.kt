package de.smartsteuer.frank.adventofcode2025.day11

import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Reactor.execute(lines("/adventofcode2025/day11/devices.txt"))
}

object Reactor: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseDevices().countPaths(you, out)

  override fun part2(input: List<String>): Long =
    input.parseDevices().let { devices ->
      val fftToDac = devices.countPaths(fft, dac)
      if (fftToDac > 0) devices.countPaths(svr, fft) * fftToDac *                     devices.countPaths(dac, out)
      else              devices.countPaths(svr, dac) * devices.countPaths(dac, fft) * devices.countPaths(fft, out)
    }

  var you = "you".hash()
  var out = "out".hash()
  var svr = "svr".hash()
  var fft = "fft".hash()
  var dac = "dac".hash()

  internal fun List<String>.parseDevices(): Devices {
    val connections = MutableList<List<Int>>(MAXIMUM_DEVICE_COUNT) { emptyList() }
    forEach { line ->
      val (from, to) = line.split(": ")
      connections[from.hash()] = to.split(" ").map { it.hash() }
    }
    return Devices(connections)
  }

  internal fun String.hash(): Int =
    CHARACTER_COUNT * (CHARACTER_COUNT * (this[0] - 'a') + (this[1] - 'a')) + (this[2] - 'a')
}

private const val CHARACTER_COUNT = 26
private const val MAXIMUM_DEVICE_COUNT = CHARACTER_COUNT * CHARACTER_COUNT * CHARACTER_COUNT

internal data class Devices(val connections: List<List<Int>>) {
  fun countPaths(from: Int, to: Int, cache: MutableList<Long> = MutableList(MAXIMUM_DEVICE_COUNT) { -1 }): Long =
    if (from == to) 1
    else cache[from].let { cached ->
      if (cached >= 0) cached
      else connections[from].sumOf { target ->
        countPaths(target, to, cache)
      }.also { cache[from] = it }
    }
}