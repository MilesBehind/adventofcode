package de.smartsteuer.frank.adventofcode2022.day03

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class RucksacksKtTest {
  @Test
  fun `part 1 is correct`() {
    part1(listOf("vJrwpWtwJgWrhcsFMMfFFhFp",
                 "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
                 "PmmdzqPrVvPwwTWBwg",
                 "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
                 "ttgJtRGJQctTZtZT",
                 "CrZsJsPPZsGzwwsLwLmpwMDw")) shouldBe 157
  }

  @Test
  fun `split halves should return 2 expected halves`() {
    "".splitHalves() shouldBe Pair("", "")
    "12".splitHalves() shouldBe Pair("1", "2")
    "1234".splitHalves() shouldBe Pair("12", "34")
    "12345".splitHalves() shouldBe Pair("12", "345")
  }

  @Test
  fun `score should compute expected score`() {
    score('a') shouldBe 1
    score('z') shouldBe 26
    score('A') shouldBe 27
    score('Z') shouldBe 52
  }
}