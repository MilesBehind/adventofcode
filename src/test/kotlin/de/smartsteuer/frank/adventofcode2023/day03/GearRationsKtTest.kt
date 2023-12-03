package de.smartsteuer.frank.adventofcode2023.day03

import de.smartsteuer.frank.adventofcode2023.day03.Coordinate.Companion.c
import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class GearRationsKtTest {
  val input = listOf(
    "467..114..",
    "...*......",
    "..35..633.",
    "......#...",
    "617*......",
    ".....+.58.",
    "..592.....",
    "......755.",
    "...\$.*....",
    ".664.598.."
  )

  @Test
  fun `part 1`() {
    part1(parseGrid(input)) shouldBe 4361
  }

  @Test
  fun `part 2`() {
    part2(parseGrid(input)) shouldBe 467835
  }

  @Test
  fun `grid finds numbers`() {
    val grid = parseGrid(input + "..1....987")
    grid.numbers shouldBe setOf(
      Grid.Number(467, setOf(c(0, 0), c(1, 0), c(2, 0))),
      Grid.Number(114, setOf(c(5, 0), c(6, 0), c(7, 0))),
      Grid.Number( 35, setOf(c(2, 2), c(3, 2))),
      Grid.Number(633, setOf(c(6, 2), c(7, 2), c(8, 2))),
      Grid.Number(617, setOf(c(0, 4), c(1, 4), c(2, 4))),
      Grid.Number( 58, setOf(c(7, 5), c(8, 5))),
      Grid.Number(592, setOf(c(2, 6), c(3, 6), c(4, 6))),
      Grid.Number(755, setOf(c(6, 7), c(7, 7), c(8, 7))),
      Grid.Number(664, setOf(c(1, 9), c(2, 9), c(3, 9))),
      Grid.Number(598, setOf(c(5, 9), c(6, 9), c(7, 9))),

      Grid.Number(  1, setOf(c(2, 10))),
      Grid.Number(987, setOf(c(7, 10), c(8, 10), c(9, 10))),
    )
  }

  @Test
  fun `symbols are recognized`() {
    val grid = parseGrid(input)
    grid.symbols shouldBe setOf(
      Grid.Symbol('*', c(3, 1)),
      Grid.Symbol('#', c(6, 3)),
      Grid.Symbol('*', c(3, 4)),
      Grid.Symbol('+', c(5, 5)),
      Grid.Symbol('$', c(3, 8)),
      Grid.Symbol('*', c(5, 8))
    )
  }
}