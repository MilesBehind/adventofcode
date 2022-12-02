package de.smartsteuer.frank.adventofcode2021.day06

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val fish = lines("/adventofcode2021/day06/fish-timers.txt").first()
    .splitToSequence(',')
    .map { it.toInt() }
    .map { LanternFish(it) }
  val fishAfter80Days = simulateGrowth(fish, 80)
  println("number of fish after 80 days: ${fishAfter80Days.toList().size}")

  val fishCountPerTimerValue: Map<TimerValue, Count> = fish.groupBy { it.timerValue }
                                                           .mapValues { it.value.size.toLong() }
  val fishAfter256Days = simulateGrowth(fishCountPerTimerValue, 256)
  println("number of fish after 256 days: ${fishAfter256Days.values.sum()}")  // 1_609_314_870_967  (1.6 * 10^12) optimized packing would require 800 GByte RAM
}

internal typealias TimerValue = Int
internal typealias Count      = Long

@JvmInline
internal value class LanternFish(val timerValue: TimerValue)

internal tailrec fun simulateGrowth(fish: Sequence<LanternFish>, remainingDays: Int): Sequence<LanternFish> = when (remainingDays) {
  0    -> fish
  else -> simulateGrowth(fish.map {
    when (it.timerValue) {
      0    -> listOf(LanternFish(6), LanternFish(8))
      else -> listOf(LanternFish(it.timerValue - 1))
    }
  }.flatten(), remainingDays - 1)
}

internal tailrec fun simulateGrowth(fishes: Map<TimerValue, Count>, remainingDays: Int): Map<TimerValue, Count> = when (remainingDays) {
  0    -> fishes
  else -> simulateGrowth(mapOf(8 to fishes.count(0),
                               7 to fishes.count(8),
                               6 to fishes.count(7) + fishes.count(0),
                               5 to fishes.count(6),
                               4 to fishes.count(5),
                               3 to fishes.count(4),
                               2 to fishes.count(3),
                               1 to fishes.count(2),
                               0 to fishes.count(1)), remainingDays - 1)
}

private fun <K> Map<K, Count>.count(key: K) = this.getOrDefault(key, 0)