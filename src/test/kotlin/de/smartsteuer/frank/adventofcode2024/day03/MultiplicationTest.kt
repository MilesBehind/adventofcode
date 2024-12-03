package de.smartsteuer.frank.adventofcode2024.day03

import de.smartsteuer.frank.adventofcode2024.lines
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MultiplicationTest {
  val input = listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")

  @Test
  fun `part1 computed expected result`() {
    part1(input.parseInstructions()) shouldBe 161L
  }

  @Test
  fun `part2 computed expected result`() {
    part2(input.parseInstructions()) shouldBe 48L
  }

  @Test
  fun `memory can be parsed`() {
    input.parseInstructions() shouldContainExactly listOf(
      MultiplicationInstruction( 2, 4),
      DontInstruction,
      MultiplicationInstruction( 5, 5),
      MultiplicationInstruction(11, 8),
      DoInstruction,
      MultiplicationInstruction( 8, 5),
    )
  }

  @Test
  fun `big memory can be parsed`() {
    val instructions = lines("/adventofcode2024/day03/computer-memory.txt").parseInstructions()
    instructions shouldHaveSize 816
  }
}