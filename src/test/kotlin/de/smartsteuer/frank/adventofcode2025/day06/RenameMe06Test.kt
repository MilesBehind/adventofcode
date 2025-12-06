package de.smartsteuer.frank.adventofcode2025.day06

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TrashCompactorTest {
  private val input = listOf(
    "123 328  51 64 ",
    " 45 64  387 23 ",
    "  6 98  215 314",
    "*   +   *   +  ",
  )

  @Test
  fun `part 1 returns expected result`() {
    TrashCompactor.part1(input) shouldBe 4277556
  }

  @Test
  fun `part 2 returns expected result`() {
    TrashCompactor.part2(input) shouldBe 3263827
  }

  @Test
  fun `exercises can be parsed for part 1`() {
    input.parseMathExercisesPart1() shouldBe listOf(
      MathExercise(listOf(123,  45,   6), mul),
      MathExercise(listOf(328,  64,  98), add),
      MathExercise(listOf( 51, 387, 215), mul),
      MathExercise(listOf( 64,  23, 314), add),
    )
  }

  @Test
  fun `exercises can be parsed for part 2`() {
    input.parseMathExercisesPart2() shouldBe listOf(
      MathExercise(listOf(  1,  24, 356), mul),
      MathExercise(listOf(369, 248,   8), add),
      MathExercise(listOf( 32, 581, 175), mul),
      MathExercise(listOf(623, 431,   4), add),
    )
  }
}