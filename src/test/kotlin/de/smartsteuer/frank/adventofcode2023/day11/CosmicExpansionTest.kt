package de.smartsteuer.frank.adventofcode2023.day11

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class CosmicExpansionTest {
  private val input = listOf(
    "...#......",
    ".......#..",
    "#.........",
    "..........",
    "......#...",
    ".#........",
    ".........#",
    "..........",
    ".......#..",
    "#...#.....",
  )

  @Test
  fun `part 1`() {
    part1(parseImage(input)) shouldBe 374
  }

  @Test
  fun `expanded distance can be computed`() {
    parseImage(input).expand( 10).distances().sum() shouldBe 1030
    parseImage(input).expand(100).distances().sum() shouldBe 8410
  }

  @Test
  fun `image can be parsed`() {
    parseImage(input) shouldBe Image(10, 10, setOf(
      Pos(3, 0), Pos(7, 1), Pos(0, 2), Pos(6, 4), Pos(1, 5), Pos(9, 6), Pos(7, 8), Pos(0, 9), Pos(4, 9)
    ))
  }

  @Test
  fun `empty rows are found`() {
    parseImage(input).findEmptyRows() shouldBe listOf(3, 7)
  }

  @Test
  fun `empty columns are found`() {
    parseImage(input).findEmptyColumns() shouldBe listOf(2, 5, 8)
  }

  @Test
  fun `galaxies can be expanded`() {
    parseImage(input).expand() shouldBe Image(13, 12, setOf(
      Pos(4, 0), Pos(9, 1), Pos(0, 2), Pos(8, 5), Pos(1, 6), Pos(12, 7), Pos(9, 10), Pos(0, 11), Pos(5, 11)
    ))
  }

  @Test
  fun `unique pairs can be found`() {
    listOf(1, 2, 3).pairs() shouldBe listOf(1 to 2, 1 to 3, 2 to 3)
  }
}

/*
....# ..... ...
..... ....# ...
#.... ..... ...
..... ..... ...
..... ..... ...
..... ...#. ...
.#... ..... ...
..... ..... ..#
..... ..... ...
..... ..... ...
..... ....# ...
#.... #.... ...
 */