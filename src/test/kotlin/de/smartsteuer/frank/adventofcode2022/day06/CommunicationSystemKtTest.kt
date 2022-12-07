package de.smartsteuer.frank.adventofcode2022.day06

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class CommunicationSystemKtTest {
  @Test
  fun `part 1 is correct`() {
    part1("mjqjpqmgbljsphdztnvjfqwrcgsmlb")    shouldBe  7
    part1("bvwbjplbgvbhsrlpgdmjqwftvncz")      shouldBe  5
    part1("nppdvjthqldpwncqszvftbrmjlhg")      shouldBe  6
    part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") shouldBe 10
    part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")  shouldBe 11
  }

  @Test
  fun `part 2 is correct`() {
    part2("mjqjpqmgbljsphdztnvjfqwrcgsmlb")    shouldBe 19
    part2("bvwbjplbgvbhsrlpgdmjqwftvncz")      shouldBe 23
    part2("nppdvjthqldpwncqszvftbrmjlhg")      shouldBe 23
    part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") shouldBe 29
    part2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")  shouldBe 26
  }
}