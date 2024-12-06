package de.smartsteuer.frank.adventofcode2024.day06

import de.smartsteuer.frank.adventofcode2024.day06.GuardGallivant.Direction
import de.smartsteuer.frank.adventofcode2024.day06.GuardGallivant.Pos
import de.smartsteuer.frank.adventofcode2024.day06.GuardGallivant.parseMap
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GuardStateGallivantTest {
   private val input = listOf(
     "....#.....",
     ".........#",
     "..........",
     "..#.......",
     ".......#..",
     "..........",
     ".#..^.....",
     "........#.",
     "#.........",
     "......#...",
   )

  @Test
  fun `part 1 returns expected result`() {
    GuardGallivant.part1(input) shouldBe 41
  }

  @Test
  fun `part 2 returns expected result`() {
    GuardGallivant.part2(input) shouldBe 6
  }

  @Test
  fun `map can be parsed`() {
    val (map, guard) = input.parseMap()
    map.obstacles shouldHaveSize 100
    map.obstacles.filterValues { it }.keys shouldBe setOf(Pos(4, 0), Pos(9, 1), Pos(2, 3), Pos(7, 4), Pos(1, 6), Pos(8, 7), Pos(0, 8), Pos(6, 9))
    map.width  shouldBe 10
    map.height shouldBe 10
    guard.pos shouldBe Pos(4, 6)
    guard.direction shouldBe Direction(0, -1)
  }
}