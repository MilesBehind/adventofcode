package de.smartsteuer.frank.adventofcode2024.day11

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  PlutonianPebbles.execute(lines("/adventofcode2024/day11/stones.txt"))
}

object PlutonianPebbles : Day {

  override fun part1(input: List<String>): Long =
    input.parseStones().blink(25).size.toLong()

  override fun part2(input: List<String>): Long =
    input.parseStones().blink(75).size.toLong()

  fun List<Long>.blink(times: Int): List<Long> {
    tailrec fun blink(times: Int, stones: MutableList<Long>): List<Long> {
      println(times)
      if (times == 0) return stones
      stones.indices.reversed().forEach { index ->
        val stone = stones[index]
        val stoneDigits = stone.toString()
        if (stone == 0L) {
          stones[index] = 1
        } else if (stoneDigits.length % 2 == 0) {
          stones[index] = stoneDigits.take(stoneDigits.length / 2).toLong()
          stones.add(index + 1, stoneDigits.takeLast(stoneDigits.length / 2).toLong())
        } else {
          stones[index] = stone * 2024
        }
      }
      return blink(times - 1, stones)
    }
    return blink(times, this.toMutableList())
  }

  private fun List<String>.parseStones() =
    this.first().split(" ").map { it.toLong() }
}
