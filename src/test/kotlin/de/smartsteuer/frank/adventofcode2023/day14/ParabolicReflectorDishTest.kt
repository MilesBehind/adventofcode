package de.smartsteuer.frank.adventofcode2023.day14

import io.kotest.matchers.*
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

class ParabolicReflectorDishTest {
  private val input = listOf(
    "O....#....",
    "O.OO#....#",
    ".....##...",
    "OO.#O....O",
    ".O.....O#.",
    "O.#..O.#.#",
    "..O..#O..O",
    ".......O..",
    "#....###..",
    "#OO..#....",
  )
  private val rocksAfter1Cycle = listOf(
    ".....#....",
    "....#...O#",
    "...OO##...",
    ".OO#......",
    ".....OOO#.",
    ".O#...O#.#",
    "....O#....",
    "......OOOO",
    "#...O###..",
    "#..OO#....",
  )
  private val rocksAfter2Cycles = listOf(
    ".....#....",
    "....#...O#",
    ".....##...",
    "..O#......",
    ".....OOO#.",
    ".O#...O#.#",
    "....O#...O",
    ".......OOO",
    "#..OO###..",
    "#.OOO#...O",
  )
  private val rocksAfter3Cycles = listOf(
    ".....#....",
    "....#...O#",
    ".....##...",
    "..O#......",
    ".....OOO#.",
    ".O#...O#.#",
    "....O#...O",
    ".......OOO",
    "#...O###.O",
    "#.OOO#...O",
  )

  @Test
  fun `part 1`() {
    part1(parsePlatform(input)) shouldBe 136
  }

  @Test
  fun `part 2`() {
    part2(parsePlatform(input)) shouldBe 64
  }

  @Test
  fun `platform can be tilted`() {
    val roundRocks = parsePlatform(input).tilt().roundRocks
    roundRocks shouldContainExactly setOf(
      Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(3, 0), Pos(7, 0),
      Pos(0, 1), Pos(1, 1),
      Pos(0, 2), Pos(1, 2), Pos(4, 2), Pos(9, 2),
      Pos(0, 3), Pos(5, 3), Pos(6, 3),
      Pos(2, 6), Pos(7, 6), Pos(9, 6),
      Pos(2, 7),
    )
  }

  @Test
  fun `platform can be rotated`() {
    val rotatedPlatform = Platform(4, 3,            setOf(Pos(0, 0), Pos(1, 1), Pos(3, 2), Pos(2, 1)), emptySet()).rotateClockwise()
    rotatedPlatform.roundRocks shouldContainExactly setOf(Pos(2, 0), Pos(1, 1), Pos(0, 3), Pos(1, 2))
  }

  @Test
  fun `platform cycle`() {
    val platform                     = parsePlatform(input)
    val expectedPlatformAfter1Cycle  = parsePlatform(rocksAfter1Cycle)
    val expectedPlatformAfter2Cycles = parsePlatform(rocksAfter2Cycles)
    val expectedPlatformAfter3Cycles = parsePlatform(rocksAfter3Cycles)
    val after1Cycle  = platform.cycle()
    val after2Cycles = after1Cycle.cycle()
    val after3Cycles = after2Cycles.cycle()
    after1Cycle  shouldBe expectedPlatformAfter1Cycle
    after2Cycles shouldBe expectedPlatformAfter2Cycles
    after3Cycles shouldBe expectedPlatformAfter3Cycles
  }

  @Test
  fun `platform can be parsed`() {
    parsePlatform(input) shouldBe Platform(10, 10, setOf(
      Pos(0, 0),
      Pos(0, 1), Pos(2, 1), Pos(3, 1),
      Pos(0, 3), Pos(1, 3), Pos(4, 3), Pos(9, 3),
      Pos(1, 4), Pos(7, 4),
      Pos(0, 5), Pos(5, 5),
      Pos(2, 6), Pos(6, 6), Pos(9, 6),
      Pos(7, 7),
      Pos(1, 9), Pos(2, 9)
    ), setOf(
      Pos(5, 0),
      Pos(4, 1), Pos(9, 1),
      Pos(5, 2), Pos(6, 2),
      Pos(3, 3),
      Pos(8, 4),
      Pos(2, 5), Pos(7, 5), Pos(9, 5),
      Pos(5, 6),
      Pos(0, 8), Pos(5, 8), Pos(6, 8), Pos(7, 8),
      Pos(0, 9), Pos(5, 9),
    ))
  }
}