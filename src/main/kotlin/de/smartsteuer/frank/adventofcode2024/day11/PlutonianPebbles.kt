package de.smartsteuer.frank.adventofcode2024.day11

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  PlutonianPebbles.execute(lines("/adventofcode2024/day11/stones.txt"))
}

object PlutonianPebbles : Day {

  override fun part1(input: List<String>): Long =
    input.parseStones().blink(25)

  override fun part2(input: List<String>): Long =
    input.parseStones().blink(75)

  private fun List<Long>.blink(times: Int): Long =
    this.sumOf { stone -> blink(times, stone) }

  private fun blink(times: Int, stone: Long, cache: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()): Long {
    if (times == 0) return 1
    return cache.getOrPut(stone to times) {
      val stoneDigits = stone.toString()
      if (stone == 0L) {
        blink(times - 1, 1L, cache)
      } else if (stoneDigits.length % 2 == 0) {
        val stone1 = stoneDigits.take(stoneDigits.length / 2).toLong()
        val stone2 = stoneDigits.takeLast(stoneDigits.length / 2).toLong()
        blink(times - 1, stone1, cache) + blink(times - 1, stone2, cache)
      } else {
        blink(times - 1, stone * 2024L, cache)
      }
    }
  }

  private fun List<String>.parseStones() =
    this.first().split(" ").map { it.toLong() }
}
