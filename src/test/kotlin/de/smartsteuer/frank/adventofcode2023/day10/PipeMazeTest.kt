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
  private val lines3 = listOf(
    "...........",
    ".S-------7.",
    ".|F-----7|.",
    ".||.....||.",
    ".||.....||.",
    ".|L-7.F-J|.",
    ".|..|.|..|.",
    ".L--J.L--J.",
    "...........",
  )
  private val lines4 = listOf(
    ".F----7F7F7F7F-7....",
    ".|F--7||||||||FJ....",
    ".||.FJ||||||||L7....",
    "FJL7L7LJLJ||LJ.L-7..",
    "L--J.L7...LJS7F-7L7.",
    "....F-J..F7FJ|L7L7L7",
    "....L7.F7||L7|.L7L7|",
    ".....|FJLJ|FJ|F7|.LJ",
    "....FJL-7.||.||||...",
    "....L---J.LJ.LJLJ...",
  )
  private val lines5 = listOf(
    "FF7FSF7F7F7F7F7F---7",
    "L|LJ||||||||||||F--J",
    "FL-7LJLJ||||||LJL-77",
    "F--JF--7||LJLJ7F7FJ-",
    "L---JF-JLJ.||-FJLJJ7",
    "|F|F-JF---7F7-L7L|7|",
    "|FFJF7L7F-JF7|JL---7",
    "7-L-JL7||F7|L7F-7F7|",
    "L.L7LFJ|||||FJL7||LJ",
    "L7JLJL-JLJLJL--JLJ.L",
  )

  @Test
  fun `part 1`() {
    part1(parsePipeMap(lines1)) shouldBe 4
    part1(parsePipeMap(lines2)) shouldBe 8
  }

  @Test
  fun `part 2`() {
    part2(parsePipeMap(lines3)) shouldBe 4
    part2(parsePipeMap(lines4)) shouldBe 8
    part2(parsePipeMap(lines5)) shouldBe 10
  }

  @Test
  fun `pipe map can be parsed`() {
    val pipeMap = parsePipeMap(lines1)
    pipeMap.start shouldBe Tile(Pos(1, 1), Pos(2, 1), Pos(1, 2))
  }
}