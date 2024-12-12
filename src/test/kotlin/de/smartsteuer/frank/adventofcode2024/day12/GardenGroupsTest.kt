package de.smartsteuer.frank.adventofcode2024.day12

import de.smartsteuer.frank.adventofcode2024.day12.GardenGroups.Pos
import de.smartsteuer.frank.adventofcode2024.day12.GardenGroups.Region
import de.smartsteuer.frank.adventofcode2024.day12.GardenGroups.findRegions
import de.smartsteuer.frank.adventofcode2024.day12.GardenGroups.part1
import de.smartsteuer.frank.adventofcode2024.day12.GardenGroups.part2
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GardenGroupsTest {
   private val input1 = listOf(
     "AAAA",
     "BBCD",
     "BBCC",
     "EEEC",
   )
   private val input2 = listOf(
     "OOOOO",
     "OXOXO",
     "OOOOO",
     "OXOXO",
     "OOOOO",
   )
   private val input3 = listOf(
     "RRRRIICCFF",
     "RRRRIICCCF",
     "VVRRRCCFFF",
     "VVRCCCJFFF",
     "VVVVCJJCFE",
     "VVIVCCJJEE",
     "VVIIICJJEE",
     "MIIIIIJJEE",
     "MIIISIJEEE",
     "MMMISSJEEE",
   )
   private val input4 = listOf(
     "EEEEE",
     "EXXXX",
     "EEEEE",
     "EXXXX",
     "EEEEE",
   )
   private val input5 = listOf(
     "AAAAAA",
     "AAABBA",
     "AAABBA",
     "ABBAAA",
     "ABBAAA",
     "AAAAAA",
   )

  @Test
  fun `part 1 returns expected result`() {
    part1(input1) shouldBe  140
    part1(input2) shouldBe  772
    part1(input3) shouldBe 1930
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input1) shouldBe   80
    part2(input2) shouldBe  436
    part2(input3) shouldBe 1206
    part2(input4) shouldBe  236
    part2(input5) shouldBe  368
  }

  @Test
  fun `regions are found`() {
    val regions1 = findRegions(input1)
    regions1 shouldHaveSize 5
    regions1 shouldContain Region('A', mutableSetOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(3, 0)))
    regions1 shouldContain Region('B', mutableSetOf(Pos(0, 1), Pos(1, 1), Pos(0, 2), Pos(1, 2)))
    regions1 shouldContain Region('C', mutableSetOf(Pos(2, 1), Pos(2, 2), Pos(3, 2), Pos(3, 3)))
    regions1 shouldContain Region('D', mutableSetOf(Pos(3, 1)))
    regions1 shouldContain Region('E', mutableSetOf(Pos(0, 3), Pos(1, 3), Pos(2, 3)))

    val regions2 = findRegions(input2)
    regions2 shouldHaveSize 5
    regions2 shouldContain Region('O', mutableSetOf(Pos(0, 0), Pos(1, 0), Pos(2, 0), Pos(3, 0), Pos(4, 0),
                                                               Pos(0, 1),            Pos(2, 1),            Pos(4, 1),
                                                               Pos(0, 2), Pos(1, 2), Pos(2, 2), Pos(3, 2), Pos(4, 2),
                                                               Pos(0, 3),            Pos(2, 3),            Pos(4, 3),
                                                               Pos(0, 4), Pos(1, 4), Pos(2, 4), Pos(3, 4), Pos(4, 4)))
    regions2 shouldContain Region('X', mutableSetOf(Pos(1, 1)))
    regions2 shouldContain Region('X', mutableSetOf(Pos(3, 1)))
    regions2 shouldContain Region('X', mutableSetOf(Pos(1, 3)))
    regions2 shouldContain Region('X', mutableSetOf(Pos(3, 3)))

    val regions3 = findRegions(input3)
    regions3 shouldHaveSize 11
    regions3.filter { it.plant == 'C' } shouldContain Region('C', mutableSetOf(Pos(6, 0), Pos(7, 0),
                                                                               Pos(6, 1), Pos(7, 1), Pos(8, 1),
                                                                               Pos(5, 2), Pos(6, 2),
                                                                               Pos(3, 3), Pos(4, 3), Pos(5, 3),
                                                                               Pos(4, 4),
                                                                               Pos(4, 5), Pos(5, 5),
                                                                               Pos(5, 6)))
  }

  @Test
  fun `area and perimeter are computed correctly`() {
    findRegions(input1).map { it.plant to (it.area() to it.perimeter()) } shouldContainExactlyInAnyOrder listOf(
      'A' to (4 to 10),
      'B' to (4 to 8),
      'C' to (4 to 10),
      'D' to (1 to 4),
      'E' to (3 to 8),
    )
    findRegions(input2).map { it.plant to (it.area() to it.perimeter()) } shouldContainExactlyInAnyOrder listOf(
      'O' to (21 to 36),
      'X' to ( 1 to 4),
      'X' to ( 1 to 4),
      'X' to ( 1 to 4),
      'X' to ( 1 to 4),
    )
    findRegions(input3).map { it.plant to (it.area() to it.perimeter()) } shouldContainExactlyInAnyOrder listOf(
      'R' to (12  to 18),
      'I' to ( 4  to  8),
      'C' to (14  to 28),
      'F' to (10  to 18),
      'V' to (13  to 20),
      'J' to (11  to 20),
      'C' to ( 1  to  4),
      'E' to (13  to 18),
      'I' to (14  to 22),
      'M' to ( 5  to 12),
      'S' to ( 3  to  8)
    )
  }
}
