package de.smartsteuer.frank.adventofcode2022.day08

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TreeHouseKtTest {
  private val input = listOf(
    "30373",
    "25512",
    "65332",
    "33549",
    "35390",
  )

  @Test
  fun `part 1 is correct`() {
    Day08.part1(input) shouldBe 21
  }

  @Test
  fun `part 2 is correct`() {
    Day08.part2(input) shouldBe 8
  }

  @Test
  fun `takeUntil takes elements until predicate is met`() {
    emptyList<Int>().takeUntil    { it >= 2 } shouldContainExactly emptyList()
    listOf(1).takeUntil           { it >= 2 } shouldContainExactly listOf(1)
    listOf(1, 2).takeUntil        { it >= 2 } shouldContainExactly listOf(1, 2)
    listOf(1, 2, 3).takeUntil     { it >= 2 } shouldContainExactly listOf(1, 2)
    listOf(1, 2, 3, 4 ).takeUntil { it >= 2 } shouldContainExactly listOf(1, 2)
  }
}