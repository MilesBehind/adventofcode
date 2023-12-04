package de.smartsteuer.frank.adventofcode2023.day04

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class ScratchCardsTest {
  private val input = listOf(
    "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
    "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
    "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
    "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
    "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
    "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11",
  )

  @Test
  fun `part 1`() {
    part1(parseCards(input)) shouldBe 13
  }

  @Test
  fun `part 2`() {
    part2(parseCards(input)) shouldBe 30
  }

  @Test
  fun `scratch cards can be parsed`() {
    parseCards(input) shouldBe listOf(
      ScratchCard(1, setOf(41, 48, 83, 86, 17), setOf(83, 86,  6, 31, 17,  9, 48, 53)),
      ScratchCard(2, setOf(13, 32, 20, 16, 61), setOf(61, 30, 68, 82, 17, 32, 24, 19)),
      ScratchCard(3, setOf( 1, 21, 53, 59, 44), setOf(69, 82, 63, 72, 16, 21, 14,  1)),
      ScratchCard(4, setOf(41, 92, 73, 84, 69), setOf(59, 84, 76, 51, 58,  5, 54, 83)),
      ScratchCard(5, setOf(87, 83, 26, 28, 32), setOf(88, 30, 70, 12, 93, 22, 82, 36)),
      ScratchCard(6, setOf(31, 18, 13, 56, 72), setOf(74, 77, 10, 23, 35, 67, 36, 11)),
    )
  }
}