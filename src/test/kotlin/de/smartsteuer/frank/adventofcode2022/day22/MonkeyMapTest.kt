package de.smartsteuer.frank.adventofcode2022.day22

import de.smartsteuer.frank.adventofcode2022.day22.Day22.Action.*
import de.smartsteuer.frank.adventofcode2022.day22.Day22.Coordinate
import de.smartsteuer.frank.adventofcode2022.day22.Day22.State
import de.smartsteuer.frank.adventofcode2022.day22.Day22.Direction
import de.smartsteuer.frank.adventofcode2022.day22.Day22.GroveMap
import de.smartsteuer.frank.adventofcode2022.day22.Day22.parseInput
import de.smartsteuer.frank.adventofcode2022.day22.Day22.part1
import de.smartsteuer.frank.adventofcode2022.day22.Day22.part2
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
class MonkeyMapTest {
  private val input = listOf(
    "        ...#",
    "        .#..",
    "        #...",
    "        ....",
    "...#.......#",
    "........#...",
    "..#....#....",
    "..........#.",
    "        ...#....",
    "        .....#..",
    "        .#......",
    "        ......#.",
    "",
    "10R5L5R10L4R5L5",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 1_000 * 6 + 4 * 8 + 0  // non-example result: ???
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 301  // non-example result: 3451534022348
  }

  @Test
  fun `map and path can be parsed`() {
    val (map, path) = parseInput(input, GroveMap.WrapStrategy.Flat)
    map.toString() shouldBe input.dropLast(2).joinToString(separator = "\n", postfix = "\n")

    path.actions shouldBe listOf(
      Forward(10), TurnRight, Forward(5), TurnLeft, Forward(5), TurnRight, Forward(10), TurnLeft, Forward(4), TurnRight, Forward(5), TurnLeft, Forward(5)
    )
  }

  @Test
  fun `next tile for some state can be found`() {
    val (map, _) = parseInput(input, GroveMap.WrapStrategy.Flat)
    assertSoftly {
      map.nextTile(State( 8 to  0, Direction.RIGHT)) shouldBe Coordinate( 9,  0)
      map.nextTile(State( 8 to  0, Direction.LEFT))  shouldBe Coordinate(11,  0)
      map.nextTile(State(11 to  1, Direction.RIGHT)) shouldBe Coordinate( 8,  1)
      map.nextTile(State(11 to  1, Direction.LEFT))  shouldBe Coordinate(10,  1)

      map.nextTile(State( 5 to  4, Direction.UP))    shouldBe Coordinate( 5,  7)
      map.nextTile(State( 5 to  4, Direction.DOWN))  shouldBe Coordinate( 5,  5)
      map.nextTile(State(15 to 11, Direction.UP))    shouldBe Coordinate(15, 10)
      map.nextTile(State(15 to 11, Direction.DOWN))  shouldBe Coordinate(15,  8)
    }
  }
}
