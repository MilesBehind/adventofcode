package de.smartsteuer.frank.adventofcode2023.day01

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class TrebuchetKtTest {
  @Test
  fun `part 1`() {
    part1(listOf("1abc2",
                 "pqr3stu8vwx",
                 "a1b2c3d4e5f",
                 "treb7uchet")) shouldBe 142
  }

  @Test
  fun `part 2`() {
    part2(listOf("two1nine",
                 "eightwothree",
                 "abcone2threexyz",
                 "xtwone3four",
                 "4nineeightseven2",
                 "zoneight234",
                 "7pqrstsixteen")) shouldBe 281
  }
}