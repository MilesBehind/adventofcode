package de.smartsteuer.frank.adventofcode2024.day02

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ReportsTest {
   private val reports = listOf(
     "7 6 4 2 1",
     "1 2 7 8 9",
     "9 7 6 2 1",
     "1 3 2 4 5",
     "8 6 4 4 1",
     "1 3 6 7 9",
   )

  @Test
  fun `reports can be parsed`() {
    reports.parseReports() shouldBe listOf(
      listOf(7, 6, 4, 2, 1),
      listOf(1, 2, 7, 8, 9),
      listOf(9, 7, 6, 2, 1),
      listOf(1, 3, 2, 4, 5),
      listOf(8, 6, 4, 4, 1),
      listOf(1, 3, 6, 7, 9),
    )
  }

  @Test
  fun `part 1 returns expected result`() {
    part1(reports.parseReports()) shouldBe 2
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(reports.parseReports()) shouldBe 4
  }
}