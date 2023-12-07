package de.smartsteuer.frank.adventofcode2023.day07

import de.smartsteuer.frank.adventofcode2023.lines
import de.smartsteuer.frank.adventofcode2023.day07.Score.*
import kotlin.time.measureTime

fun main() {
  val hands = parseHands(lines("/adventofcode2023/day07/hands.txt"))
  measureTime { println("part 1: ${part1(hands)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(hands)}") }.also { println("part 2 took $it") }
}

internal fun part1(hands: List<Hand>): Long =
  hands.sorted().mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()

internal fun part2(hands: List<Hand>): Long =
  hands.map { it.copy(useJokers = true) }.sorted().mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()

enum class Score {
  HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND
}

internal data class Hand(val cards: List<Card>, val bid: Long, val useJokers: Boolean = false): Comparable<Hand> {
  private val score = if (useJokers) jokerScore() else score()

  override fun compareTo(other: Hand): Int = when {
    this.score > other.score -> 1
    this.score < other.score -> -1
    else -> {
      val differentCardIndex = cards.indices.first { this.cards[it].value != other.cards[it].value }
      this.cards[differentCardIndex].value(useJokers) - other.cards[differentCardIndex].value(useJokers)
    }
  }

  private fun score(): Score =
    cards.groupBy { it.value }.values.toList().let { cardGroups: List<List<Card>> ->
      val same4 = cardGroups.any { it.size == 4 }
      val same3 = cardGroups.any { it.size == 3 }
      when {
        cardGroups.size == 1          -> FIVE_OF_A_KIND
        cardGroups.size == 2 && same4 -> FOUR_OF_A_KIND
        cardGroups.size == 2          -> FULL_HOUSE
        cardGroups.size == 3 && same3 -> THREE_OF_A_KIND
        cardGroups.size == 3          -> TWO_PAIR
        cardGroups.size == 4          -> PAIR
        else                          -> HIGH_CARD
      }
    }

  private fun jokerScore(): Score =
    cards.groupBy { it.value }.values.toList().let { cardGroups: List<List<Card>> ->
      val jokerCount = cardGroups.find { it.first().label == 'J' }?.size ?: 0
      val same4      = cardGroups.any { it.size == 4 }
      val same3      = cardGroups.any { it.size == 3 }
      when {
        cardGroups.size == 1                            -> FIVE_OF_A_KIND
        cardGroups.size == 2 && same4 && jokerCount > 0 -> FIVE_OF_A_KIND  // four of a kind + 1 or 4 jokers => five of a kind
        cardGroups.size == 2 && same4                   -> FOUR_OF_A_KIND
        cardGroups.size == 2 && jokerCount > 0          -> FIVE_OF_A_KIND  // full house + joker => five of a kind
        cardGroups.size == 2                            -> FULL_HOUSE
        cardGroups.size == 3 && same3 && jokerCount > 0 -> FOUR_OF_A_KIND  // three of a kind + 1 or 3 jokers => 4 of a kind
        cardGroups.size == 3 && same3                   -> THREE_OF_A_KIND
        cardGroups.size == 3 && jokerCount == 2         -> FOUR_OF_A_KIND  // two pair + 2 jokers => 4 of a kind
        cardGroups.size == 3 && jokerCount == 1         -> FULL_HOUSE      // two pair + 1 joker => full house
        cardGroups.size == 3                            -> TWO_PAIR
        cardGroups.size == 4 && jokerCount > 0          -> PAIR            // pair + 1 or 2 jokers => three of a kind
        cardGroups.size == 4                            -> PAIR
        jokerCount == 1                                 -> HIGH_CARD       // high card + joker => pair
        else                                            -> HIGH_CARD
      }
    }
}

internal data class Card(val label: Char, val value: Int) {
  fun value(useJokers: Boolean) = if (label == 'J' && useJokers) 1 else value
}

internal fun parseHands(lines: List<String>, useJokers: Boolean = false): List<Hand> =
  lines.map { line ->
    val (cardLabels, bid) = line.split(" ")
    val cards = cardLabels.map { label ->
      val value = mapOf('A' to 14, 'K' to 13, 'Q' to 12, 'J' to 11, 'T' to 10)[label] ?: label.digitToInt()
      Card(label, value)
    }
    Hand(cards, bid.toLong(), useJokers)
  }