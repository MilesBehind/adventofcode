package de.smartsteuer.frank.adventofcode2025.day01

import de.smartsteuer.frank.adventofcode2025.day01.Direction.LEFT
import de.smartsteuer.frank.adventofcode2025.day01.Direction.RIGHT
import de.smartsteuer.frank.adventofcode2025.day01.SecretEntrance.part1
import de.smartsteuer.frank.adventofcode2025.day01.SecretEntrance.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SecretEntranceTest {
  private val input = listOf(
    "L68",
    "L30",
    "R48",
    "L5",
    "R60",
    "L55",
    "L1",
    "L99",
    "R14",
    "L82",
  )

  @Test
  fun `rotations can be parsed`() {
    input.parseRotations() shouldBe listOf(
      Rotation(LEFT,  68),
      Rotation(LEFT,  30),
      Rotation(RIGHT, 48),
      Rotation(LEFT,   5),
      Rotation(RIGHT, 60),
      Rotation(LEFT,  55),
      Rotation(LEFT,   1),
      Rotation(LEFT,  99),
      Rotation(RIGHT, 14),
      Rotation(LEFT,  82)
    )
  }

  @Test
  fun `positions can be added`() {
    Position(0)  + Rotation(RIGHT, 10) shouldBe Position(10)
    Position(99) + Rotation(RIGHT, 10) shouldBe Position(9)
    Position(0)  + Rotation(LEFT,  10) shouldBe Position(90)
    Position(99) + Rotation(LEFT,  10) shouldBe Position(89)
  }

  @Test
  fun `part1 returns expected result`() {
    part1(input) shouldBe 3
  }

  @Test
  fun `part2 returns expected result`() {
    part2(input) shouldBe 6
  }
}