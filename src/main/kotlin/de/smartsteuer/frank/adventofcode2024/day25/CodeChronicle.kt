package de.smartsteuer.frank.adventofcode2024.day25

import de.smartsteuer.frank.adventofcode2022.day01.split
import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  CodeChronicle.execute(lines("/adventofcode2024/day25/locks-and-keys.txt"))
}

object CodeChronicle: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseKeysAndLocks().findMatchingKeyAndLockCombinations().toLong()

  override fun part2(input: List<String>): Long {
    TODO("Not yet implemented")
  }

  data class Key (val columns: List<Int>) {
    infix fun matches(lock: Lock) =
      columns.zip(lock.columns).all { (keyColumn, lockColumn) -> keyColumn + lockColumn <= 5 }
  }
  data class Lock(val columns: List<Int>)

  data class KeysAndLocks(val keys: List<Key>, val locks: List<Lock>) {
    fun findMatchingKeyAndLockCombinations(): Int =
      keys.sumOf { key -> locks.count { lock -> key matches lock } }
  }

  fun List<String>.parseKeysAndLocks(): KeysAndLocks {
    val (lockSchematics, keySchematics) = this.split { it.isBlank() }.partition { it.first() == "#####" }
    val locks = lockSchematics.map { lockSchematic: List<String> ->
      Lock(lockSchematic.first().indices.map { i -> lockSchematic.indexOfFirst { it[i] == '.' } - 1 })
    }
    val keys = keySchematics.map { keySchematic: List<String> ->
      Key(keySchematic.first().indices.map { i -> 6 - keySchematic.indexOfFirst { it[i] == '#' } })
    }
    return KeysAndLocks(keys, locks)
  }
}