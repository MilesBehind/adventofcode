package de.smartsteuer.frank.adventofcode2023.day14

import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.time.measureTime

fun main() {
  val platform = parsePlatform(lines("/adventofcode2023/day14/platform.txt"))
  measureTime { println("part 1: ${part1(platform)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(platform)}") }.also { println("part 2 took $it") }
}

internal fun part1(platform: Platform): Long =
  platform.tilt().computeLoad()

internal fun part2(platform: Platform): Long =
  platform.cycle(1_000_000_000).computeLoad()

internal data class Pos(val x: Int, val y: Int)

internal data class Platform(val width: Int, val height: Int, val roundRocks: Set<Pos>, val squareRocks: Set<Pos>) {
  fun tilt(): Platform {
    tailrec fun tilt(x: Int, y: Int, roundRocks: MutableSet<Pos>): Platform {
      if (y >= height) return copy(roundRocks = roundRocks)
      if (x >= width) return tilt(0, y + 1, roundRocks)
      val pos = Pos(x, y)
      if (pos !in this.roundRocks) return tilt(x + 1, y, roundRocks)
      val newY   = ((y-1) downTo 0).takeWhile { Pos(x, it) !in roundRocks && Pos(x, it) !in squareRocks }.lastOrNull() ?: y
      val newPos = Pos(x, newY)
      return tilt(x + 1, y, roundRocks.apply { add(newPos) })
    }
    return tilt(0, 0, mutableSetOf())
  }

  fun rotateClockwise(): Platform =
    Platform(height, width, roundRocks.map  { (x, y) -> Pos((height - 1) - y, x) }.toSet(),
                            squareRocks.map { (x, y) -> Pos((height - 1) - y, x) }.toSet())

  fun cycle(): Platform {
    return (1..4).fold(this) { acc, _ -> acc.tilt().rotateClockwise() }
  }

  fun cycle(cycleCount: Int): Platform {
    val previousRoundRocks = LinkedHashMap<Set<Pos>, Int>()
    tailrec fun cycleUntilNotChanged(count: Int, checkForCycle: Boolean, platFormBeforeCycle: Platform): Platform {
      if (count >= cycleCount) return platFormBeforeCycle
      val platFormAfterCycle = platFormBeforeCycle.cycle()
      if (checkForCycle) {
        val previousCount = previousRoundRocks[platFormAfterCycle.roundRocks]
        if (previousCount != null) {
          println("found cycle of length ${count - previousCount} between iteration $previousCount and $count")
          val cycleLength = count - previousCount
          val newCount    = previousCount + ((cycleCount - previousCount) / cycleLength) * cycleLength + 1
          return cycleUntilNotChanged(newCount, false, platFormAfterCycle)
        }
        previousRoundRocks[platFormAfterCycle.roundRocks] = count
        if (previousRoundRocks.size > 100) {
          val key = previousRoundRocks.entries.find { it.value == count - 100 }?.key
          previousRoundRocks.remove(key)
        }
      }
      return cycleUntilNotChanged(count + 1, checkForCycle, platFormAfterCycle)
    }
    return cycleUntilNotChanged(0, true, this)
  }

  fun computeLoad(): Long =
    roundRocks.sumOf { (height - it.y).toLong() }
}

internal fun parsePlatform(input: List<String>): Platform {
  val roundRocks  = mutableSetOf<Pos>()
  val squareRocks = mutableSetOf<Pos>()
  input.flatMapIndexed { y: Int, line: String ->
    line.mapIndexed { x, c ->
      when (c) {
        'O' -> roundRocks  += Pos(x, y)
        '#' -> squareRocks += Pos(x, y)
      }
    }
  }
  return Platform(input.first().length, input.size, roundRocks, squareRocks)
}