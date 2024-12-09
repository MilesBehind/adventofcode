package de.smartsteuer.frank.adventofcode2024.day09

import de.smartsteuer.frank.adventofcode2024.day09.DiskFragmenter.part1
import de.smartsteuer.frank.adventofcode2024.day09.DiskFragmenter.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DiskFragmenterTest {
   val input = listOf(
     "2333133121414131402"
   )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 1928L
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 2858
  }
}