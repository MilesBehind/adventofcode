package de.smartsteuer.frank.adventofcode2024.day25

import de.smartsteuer.frank.adventofcode2024.day25.CodeChronicle.Key
import de.smartsteuer.frank.adventofcode2024.day25.CodeChronicle.Lock
import de.smartsteuer.frank.adventofcode2024.day25.CodeChronicle.parseKeysAndLocks
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CodeChronicleTest {
  private val input = listOf(
    "#####",
    ".####",
    ".####",
    ".####",
    ".#.#.",
    ".#...",
    ".....",
    "",
    "#####",
    "##.##",
    ".#.##",
    "...##",
    "...#.",
    "...#.",
    ".....",
    "",
    ".....",
    "#....",
    "#....",
    "#...#",
    "#.#.#",
    "#.###",
    "#####",
    "",
    ".....",
    ".....",
    "#.#..",
    "###..",
    "###.#",
    "###.#",
    "#####",
    "",
    ".....",
    ".....",
    ".....",
    "#....",
    "#.#..",
    "#.#.#",
    "#####",
  )

  @Test
  fun `part 1 returns expected result`() {
    CodeChronicle.part1(input) shouldBe 3
  }

  @Test
  fun `matching key and lock combinations are correct`() {
    input.parseKeysAndLocks().findMatchingKeyAndLockCombinations() shouldBe 3
  }

  @Test
  fun `keys and locks can be parsed`() {
    val (keys, locks) = input.parseKeysAndLocks()
    keys shouldBe listOf(
      Key(listOf(5, 0, 2, 1, 3)),
      Key(listOf(4, 3, 4, 0, 2)),
      Key(listOf(3, 0, 2, 0, 1))
    )
    locks shouldBe listOf(
      Lock(listOf(0, 5, 3, 4, 3)),
      Lock(listOf(1, 2, 0, 5, 3))
    )
  }
}