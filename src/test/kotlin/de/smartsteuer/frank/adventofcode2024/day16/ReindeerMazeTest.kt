package de.smartsteuer.frank.adventofcode2024.day16

import de.smartsteuer.frank.adventofcode2024.day16.ReindeerMaze.part1
import de.smartsteuer.frank.adventofcode2024.day16.ReindeerMaze.part2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ReindeerMazeTest {
   private val input1 = listOf(
     "###############",
     "#.......#....E#",
     "#.#.###.#.###.#",
     "#.....#.#...#.#",
     "#.###.#####.#.#",
     "#.#.#.......#.#",
     "#.#.#####.###.#",
     "#...........#.#",
     "###.#.#####.#.#",
     "#...#.....#.#.#",
     "#.#.#.###.#.#.#",
     "#.....#...#.#.#",
     "#.###.#.#.#.#.#",
     "#S..#.....#...#",
     "###############",
   )

  private val input2 = listOf(
    "#################",
    "#...#...#...#..E#",
    "#.#.#.#.#.#.#.#.#",
    "#.#.#.#...#...#.#",
    "#.#.#.#.###.#.#.#",
    "#...#.#.#.....#.#",
    "#.#.#.#.#.#####.#",
    "#.#...#.#.#.....#",
    "#.#.#####.#.###.#",
    "#.#.#.......#...#",
    "#.#.###.#####.###",
    "#.#.#...#.....#.#",
    "#.#.#.#####.###.#",
    "#.#.#.........#.#",
    "#.#.#.#########.#",
    "#S#.............#",
    "#################",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input1) shouldBe  7036
    part1(input2) shouldBe 11048
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input1) shouldBe  0
  }
}