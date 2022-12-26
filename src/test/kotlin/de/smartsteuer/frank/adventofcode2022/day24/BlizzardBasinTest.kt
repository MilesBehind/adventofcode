package de.smartsteuer.frank.adventofcode2022.day24

import de.smartsteuer.frank.adventofcode2022.day24.Day24.parseValley
import de.smartsteuer.frank.adventofcode2022.day24.Day24.part1
import de.smartsteuer.frank.adventofcode2022.day24.Day24.part2
import de.smartsteuer.frank.adventofcode2022.day24.Day24.Coordinate
import de.smartsteuer.frank.adventofcode2022.day24.Day24.CoordinateXYT
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class BlizzardBasinTest {
  private val input = listOf(
    "#.######",
    "#>>.<^<#",
    "#.<..<<#",
    "#>v.><>#",
    "#<^v^^>#",
    "######.#",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 18  // non-example result: 373
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 54  // non-example result: ???
  }

  @Test
  fun `valley can be parsed`() {
    parseValley(input).toString() shouldBe """
      #.######
      #>>.<^<#
      #.<..<<#
      #>v.><>#
      #<^v^^>#
      ######.#
      
    """.trimIndent()
  }

  @Test
  fun `3d coordinate can be computed`() {
    val valley = parseValley(input)
    valley.blizzardPosition(Coordinate(1, 1), Day24.Direction.East,   0) shouldBe CoordinateXYT(1, 1,  0)
    valley.blizzardPosition(Coordinate(1, 1), Day24.Direction.East,   5) shouldBe CoordinateXYT(6, 1,  5)
    valley.blizzardPosition(Coordinate(1, 1), Day24.Direction.East,   6) shouldBe CoordinateXYT(1, 1,  6)
    valley.blizzardPosition(Coordinate(1, 1), Day24.Direction.East,  11) shouldBe CoordinateXYT(6, 1, 11)

    valley.blizzardPosition(Coordinate(2, 2), Day24.Direction.South,  0) shouldBe CoordinateXYT(2, 2,  0)
    valley.blizzardPosition(Coordinate(2, 2), Day24.Direction.South,  2) shouldBe CoordinateXYT(2, 4,  2)
    valley.blizzardPosition(Coordinate(2, 2), Day24.Direction.South,  3) shouldBe CoordinateXYT(2, 1,  3)

    valley.blizzardPosition(Coordinate(1, 4), Day24.Direction.West,   0) shouldBe CoordinateXYT(1, 4,  0)
    valley.blizzardPosition(Coordinate(1, 4), Day24.Direction.West,   1) shouldBe CoordinateXYT(6, 4,  1)
    valley.blizzardPosition(Coordinate(1, 4), Day24.Direction.West,  13) shouldBe CoordinateXYT(6, 4, 13)

    valley.blizzardPosition(Coordinate(3, 3), Day24.Direction.North,  0) shouldBe CoordinateXYT(3, 3,  0)
    valley.blizzardPosition(Coordinate(3, 3), Day24.Direction.North,  2) shouldBe CoordinateXYT(3, 1,  2)
    valley.blizzardPosition(Coordinate(3, 3), Day24.Direction.North,  3) shouldBe CoordinateXYT(3, 4,  3)
    valley.blizzardPosition(Coordinate(3, 3), Day24.Direction.North, 27) shouldBe CoordinateXYT(3, 4, 27)
  }

  @Test
  fun `valley can be converted to 3D-valley`() {
    val valley = parseValley(input)
    val valley3D = valley.toValley3D()
    println("time =  0:\n${valley3D.toValley( 0)}")
    println("time =  9:\n${valley3D.toValley( 9)}")
    println("time = 18:\n${valley3D.toValley(18)}")
  }
}
