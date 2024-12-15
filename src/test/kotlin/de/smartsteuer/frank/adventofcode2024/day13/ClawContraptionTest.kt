package de.smartsteuer.frank.adventofcode2024.day13

import de.smartsteuer.frank.adventofcode2024.day13.ClawContraption.part1
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ClawContraptionTest {
   private val input = listOf(
     "Button A: X+94, Y+34",
     "Button B: X+22, Y+67",
     "Prize: X=8400, Y=5400",
     "",
     "Button A: X+26, Y+66",
     "Button B: X+67, Y+21",
     "Prize: X=12748, Y=12176",
     "",
     "Button A: X+17, Y+86",
     "Button B: X+84, Y+37",
     "Prize: X=7870, Y=6450",
     "",
     "Button A: X+69, Y+23",
     "Button B: X+27, Y+71",
     "Prize: X=18641, Y=10279",
   )

  private val henrikInput = listOf(
    "Button A: X+55, Y+55",
    "Button B: X+55, Y+55",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+11, Y+11",
    "Button B: X+55, Y+55",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+55, Y+55",
    "Button B: X+11, Y+11",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+11, Y+11",
    "Button B: X+11, Y+11",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+11, Y+11",
    "Button B: X+22, Y+22",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+11, Y+11",
    "Button B: X+33, Y+33",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+11, Y+11",
    "Button B: X+44, Y+44",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+44, Y+44",
    "Button B: X+11, Y+11",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+22, Y+22",
    "Button B: X+33, Y+33",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+33, Y+33",
    "Button B: X+22, Y+22",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+44, Y+44",
    "Button B: X+22, Y+22",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+22, Y+22",
    "Button B: X+44, Y+44",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+11, Y+11",
    "Button B: X+5, Y+5",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+5, Y+5",
    "Button B: X+11, Y+11",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+1, Y+1",
    "Button B: X+5, Y+5",
    "Prize: X=55, Y=55",
    "",
    "Button A: X+5, Y+5",
    "Button B: X+1, Y+1",
    "Prize: X=55, Y=55",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 480
    part1(henrikInput) shouldBe 0
  }
}