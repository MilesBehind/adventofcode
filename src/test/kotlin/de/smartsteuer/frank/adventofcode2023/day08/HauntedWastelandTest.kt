package de.smartsteuer.frank.adventofcode2023.day08

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class HauntedWastelandTest {
  private val input1 = listOf(
    "RL",
    "",
    "AAA = (BBB, CCC)",
    "BBB = (DDD, EEE)",
    "CCC = (ZZZ, GGG)",
    "DDD = (DDD, DDD)",
    "EEE = (EEE, EEE)",
    "GGG = (GGG, GGG)",
    "ZZZ = (ZZZ, ZZZ)"
  )

  private val input2 = listOf(
    "LLR",
    "",
    "AAA = (BBB, BBB)",
    "BBB = (AAA, ZZZ)",
    "ZZZ = (ZZZ, ZZZ)",
  )

  private val input3 = listOf(
    "LR",
    "",
    "11A = (11B, XXX)",
    "11B = (XXX, 11Z)",
    "11Z = (11B, XXX)",
  )

  private val input4 = listOf(
    "LR",
    "",
    "11A = (11B, XXX)",
    "11B = (XXX, 11Z)",
    "11Z = (11B, XXX)",
    "22A = (22B, XXX)",
    "22B = (22C, 22C)",
    "22C = (22Z, 22Z)",
    "22Z = (22B, 22B)",
    "XXX = (XXX, XXX)",
  )

  @Test
  fun `part 1`() {
    part1(parseMap(input1)) shouldBe 2
    part1(parseMap(input2)) shouldBe 6
  }

  @Test
  fun `part 2`() {
    part2(parseMap(input4)) shouldBe 6
  }

  @Test
  fun `map can be parsed`() {
    parseMap(input2) shouldBe DesertMap("LLR", mapOf(
      "AAA" to ("BBB" to "BBB"),
      "BBB" to ("AAA" to "ZZZ"),
      "ZZZ" to ("ZZZ" to "ZZZ"),
    ))
    parseMap(input3) shouldBe DesertMap("LR", mapOf(
      "11A" to ("11B" to "XXX"),
      "11B" to ("XXX" to "11Z"),
      "11Z" to ("11B" to "XXX"),
    ))
  }

  @Test
  fun lcm() {
    lcm(12, 18) shouldBe 36
  }

  @Test
  fun `lcm with multiple values`() {
    lcm(listOf(12, 18)) shouldBe 36
    lcm(listOf(3, 4, 5)) shouldBe 60
    lcm(listOf(2, 4, 6)) shouldBe 12
  }
}