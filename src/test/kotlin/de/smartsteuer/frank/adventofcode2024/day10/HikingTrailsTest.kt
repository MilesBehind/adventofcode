package de.smartsteuer.frank.adventofcode2024.day10

import de.smartsteuer.frank.adventofcode2024.day10.HikingTrails.part1
import de.smartsteuer.frank.adventofcode2024.day10.HikingTrails.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class HikingTrailsTest {
   private val input1 = listOf(
     "0123",
     "1234",
     "8765",
     "9876"
   )
   private val input2 = listOf(
     "89010123",
     "78121874",
     "87430965",
     "96549874",
     "45678903",
     "32019012",
     "01329801",
     "10456732",
   )
   private val input3 = listOf(
     "012345",
     "123456",
     "234567",
     "345678",
     "416789",
     "567891",
   )
   private val input4 = listOf(
     "89010123",
     "78121874",
     "87430965",
     "96549874",
     "45678903",
     "32019012",
     "01329801",
     "10456732",
   )

  @Test
  fun `part 1 returns expected result`() {
    part1(input1) shouldBe 1
    part1(input2) shouldBe 36
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input1) shouldBe 16
    part2(input3) shouldBe 227
    part2(input4) shouldBe 81
  }
}