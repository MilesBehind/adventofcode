package de.smartsteuer.frank.adventofcode2024.day18

import de.smartsteuer.frank.adventofcode2024.day18.RamRun.part1
import de.smartsteuer.frank.adventofcode2024.day18.RamRun.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RamRunTest {
  private val input = listOf(
    "5,4",
    "4,2",
    "4,5",
    "3,0",
    "2,1",
    "6,3",
    "2,4",
    "1,5",
    "0,6",
    "3,3",
    "2,6",
    "5,1",
    "1,2",
    "5,5",
    "2,5",
    "6,5",
    "1,4",
    "0,4",
    "6,4",
    "1,1",
    "6,1",
    "1,0",
    "0,5",
    "1,6",
    "2,0",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe "22"
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe "6,1"
  }
}