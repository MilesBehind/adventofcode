package de.smartsteuer.frank.adventofcode2021.day04

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test


internal class BoardTest {
  @Test
  internal fun `board has bingo after 5 values in a row are marked`() {
    val board = Board(listOf(
      listOf(86, 46, 47, 61, 57),
      listOf(44, 74, 17,  5, 87),
      listOf(78,  8, 54, 55, 97),
      listOf(11, 90,  7, 75, 70),
      listOf(81, 50, 84, 10, 60),
    ))
    board.isBingo().shouldBeFalse()
    val board2 = board.mark(44)
    board2.isBingo().shouldBeFalse()
    val board3 = board2.mark(74)
    board3.isBingo().shouldBeFalse()
    val board4 = board3.mark(17)
    board4.isBingo().shouldBeFalse()
    val board5 = board4.mark(5)
    board5.isBingo().shouldBeFalse()
    val board6 = board5.mark(97)
    board6.isBingo().shouldBeFalse()
    val board7 = board6.mark(87)
    board7.isBingo().shouldBeTrue()
  }

  @Test
  internal fun `board has bingo after 5 values in a column are marked`() {
    val board = Board(listOf(
      listOf(86, 46, 47, 61, 57),
      listOf(44, 74, 17,  5, 87),
      listOf(78,  8, 54, 55, 97),
      listOf(11, 90,  7, 75, 70),
      listOf(81, 50, 84, 10, 60),
    ))
    board.isBingo().shouldBeFalse()
    val board2 = board.mark(47)
    board2.isBingo().shouldBeFalse()
    val board3 = board2.mark(17)
    board3.isBingo().shouldBeFalse()
    val board4 = board3.mark(78)
    board4.isBingo().shouldBeFalse()
    val board5 = board4.mark(7)
    board5.isBingo().shouldBeFalse()
    val board6 = board5.mark(84)
    board6.isBingo().shouldBeFalse()
    val board7 = board6.mark(54)
    board7.isBingo().shouldBeTrue()
  }
}