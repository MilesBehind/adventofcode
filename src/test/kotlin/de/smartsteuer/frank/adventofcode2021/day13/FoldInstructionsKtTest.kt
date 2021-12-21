package de.smartsteuer.frank.adventofcode2021.day13

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class FoldInstructionsKtTest {
  private val input = listOf("6,10",
                             "0,14",
                             "9,10",
                             "0,3",
                             "10,4",
                             "4,11",
                             "6,0",
                             "6,12",
                             "4,1",
                             "0,13",
                             "10,12",
                             "3,4",
                             "3,0",
                             "8,4",
                             "1,10",
                             "2,14",
                             "8,10",
                             "9,0",
                             "",
                             "fold along y=7",
                             "fold along x=5")

  @Test
  internal fun `first instruction produces expected result`() {
    val (dots, instructions) = parseInput(input)
    val dotsFoldedOnce = applyInstructions(instructions.take(1), dots)
    dotsFoldedOnce.size shouldBe 17
  }

  @Test
  internal fun `instructions produce rendered dots`() {
    val expectedOutput = listOf("#####",
                                "#...#",
                                "#...#",
                                "#...#",
                                "#####")
    val (dots, instructions) = parseInput(input)
    val dotsFolded = applyInstructions(instructions, dots)
    val output = render(dotsFolded)
    output shouldBe expectedOutput
  }
}