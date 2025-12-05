package de.smartsteuer.frank.adventofcode2025.day05

import de.smartsteuer.frank.adventofcode2025.day05.Cafeteria.part1
import de.smartsteuer.frank.adventofcode2025.day05.Cafeteria.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CafeteriaTest {
  private val input = listOf(
    "3-5",
    "10-14",
    "16-20",
    "12-18",
    "",
    "1",
    "5",
    "8",
    "11",
    "17",
    "32",
  )
  private val input2 = listOf(
    "3-5",
    "10-14",
    "16-20",
    "12-18",
    "11-11",
    "",
    "1",
    "5",
    "8",
    "11",
    "17",
    "32",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 3
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 14
    part2(input2) shouldBe 14
  }

  @Test
  fun `ingredients can be parsed`() {
    input.parseIngredients() shouldBe Ingredients(
      listOf(3L..5L, 10L..14L, 16L..20L, 12L..18L),
      listOf(1L, 5L, 8L, 11L, 17L, 32L)
    )
  }
}