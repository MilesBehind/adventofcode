package de.smartsteuer.frank.adventofcode2023.day07

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class CamelCardsTest {
  private val input = listOf(
    "32T3K 765",
    "T55J5 684",
    "KK677 28",
    "KTJJT 220",
    "QQQJA 483"
  )

  @Test
  fun `part 1`() {
    part1(parseHands(input)) shouldBe 6440
  }

  @Test
  fun `part 2`() {
    part2(parseHands(input)) shouldBe 5905
  }

  @Test
  fun `hands can be parsed`() {
    parseHands(input) shouldBe listOf(
      Hand(listOf(Card('3',  3), Card('2',  2), Card('T', 10), Card('3',  3), Card('K', 13)), 765L),
      Hand(listOf(Card('T', 10), Card('5',  5), Card('5',  5), Card('J', 11), Card('5',  5)), 684L),
      Hand(listOf(Card('K', 13), Card('K', 13), Card('6',  6), Card('7',  7), Card('7',  7)),  28L),
      Hand(listOf(Card('K', 13), Card('T', 10), Card('J', 11), Card('J', 11), Card('T', 10)), 220L),
      Hand(listOf(Card('Q', 12), Card('Q', 12), Card('Q', 12), Card('J', 11), Card('A', 14)), 483L)
    )
  }
}