package de.smartsteuer.frank.adventofcode2023.day10

import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class PipeMazeTest {
  private val lines1 = listOf(
    "-L|F7",
    "7S-7|",
    "L|7||",
    "-L-J|",
    "L|-JF",
  )
  private val lines2 = listOf(
    "7-F7-",
    ".FJ|7",
    "SJLL7",
    "|F--J",
    "LJ.LJ",
  )

  @Test
  fun `part 1`() {
    part1(parsePipeMap(lines1)) shouldBe 4
    part1(parsePipeMap(lines2)) shouldBe 8
  }
/*
  @Test
  fun `part 2`() {
    part2(parsePipeMap(lines1)) shouldBe 0
    part2(parsePipeMap(lines2)) shouldBe 0
  }

  @Test
  fun `pipe map can be parsed`() {
    val pipeMap = parsePipeMap(lines1)
    pipeMap.tiles[Pos(0, 0)] shouldBe EastWest(Pos(0, 0))
    pipeMap.tiles[Pos(4, 4)] shouldBe SouthEast(Pos(4, 4))
    pipeMap.tiles[Pos(1, 1)] shouldBe Start(Pos(1, 1), Pos(2, 1), Pos(1, 2))
  }

 */
}