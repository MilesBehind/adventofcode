package de.smartsteuer.frank.adventofcode2024.day15

import de.smartsteuer.frank.adventofcode2024.day15.WarehouseWoes.Pos
import de.smartsteuer.frank.adventofcode2024.day15.WarehouseWoes.parseWarehouse
import de.smartsteuer.frank.adventofcode2024.day15.WarehouseWoes.part1
import de.smartsteuer.frank.adventofcode2024.day15.WarehouseWoes.part2
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class WarehouseWoesTest {
   private val input = listOf(
     "##########",
     "#..O..O.O#",
     "#......O.#",
     "#.OO..O.O#",
     "#..O@..O.#",
     "#O#..O...#",
     "#O..O..O.#",
     "#.OO.O.OO#",
     "#....O...#",
     "##########",
     "",                                                          // X
     "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^",
     "vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v",
     "><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<",
     "<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^",
     "^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><",
     "^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^",
     ">^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^",
     "<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>",
     "^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>",
     "v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^",
   )

  private val simpleInput = listOf(
    "########",
    "#..O.O.#",
    "##@.O..#",
    "#...O..#",
    "#.#.O..#",
    "#...O..#",
    "#......#",
    "########",
    "",
    "<^^>>>vv<v>>v<<",
  )

  private val doubleInput = listOf(
    "#######",
    "#...#.#",
    "#.....#",
    "#..OO@#",
    "#..O..#",
    "#.....#",
    "#######",
    "",
    "<vv<<^^<<^^",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(simpleInput) shouldBe  2028
    part1(input)       shouldBe 10092
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 9021
  }

  @Test
  fun `warehouse can be parsed`() {
    input.parseWarehouse().toString() shouldBe listOf(
      "##########",
      "#..O..O.O#",
      "#......O.#",
      "#.OO..O.O#",
      "#..O@..O.#",
      "#O#..O...#",
      "#O..O..O.#",
      "#.OO.O.OO#",
      "#....O...#",
      "##########",
    ).joinToString("\n") + "\n"
  }

  @Test
  fun `boxes until empty space can be found`() {
    simpleInput.parseWarehouse().let { warehouse -> warehouse.findBoxesUntilEmptySpace(warehouse.boxes, Pos(2, 1), Pos(1,  0)) } shouldBe listOf(Pos(3, 1))
    simpleInput.parseWarehouse().let { warehouse -> warehouse.findBoxesUntilEmptySpace(warehouse.boxes, Pos(2, 2), Pos(0, -1)) }.shouldBeEmpty()
    simpleInput.parseWarehouse().let { warehouse -> warehouse.findBoxesUntilEmptySpace(warehouse.boxes, Pos(4, 1), Pos(1,  0)) } shouldBe listOf(Pos(5, 1))
    simpleInput.parseWarehouse().let { warehouse -> warehouse.findBoxesUntilEmptySpace(warehouse.boxes, Pos(4, 1), Pos(0,  1)) } shouldBe listOf(Pos(4, 2), Pos(4, 3), Pos(4, 4), Pos(4, 5))
  }

  @Test
  fun `robot can be moved`() {
    simpleInput.parseWarehouse().moveRobot().toString() shouldBe listOf(
      "########",
      "#....OO#",
      "##.....#",
      "#.....O#",
      "#.#O@..#",
      "#...O..#",
      "#...O..#",
      "########",
    ).joinToString("\n") + "\n"
  }

  @Test
  fun `robot can be moved in doubled warehouse`() {
    doubleInput.parseWarehouse().double().moveRobot().toString() shouldBe listOf(
      "##############",
      "##...[].##..##",
      "##...@.[]...##",
      "##....[]....##",
      "##..........##",
      "##..........##",
      "##############",
    ).joinToString("\n") + "\n"

    input.parseWarehouse().double().moveRobot().toString() shouldBe listOf(
      "####################",
      "##[].......[].[][]##",
      "##[]...........[].##",
      "##[]........[][][]##",
      "##[]......[]....[]##",
      "##..##......[]....##",
      "##..[]............##",
      "##..@......[].[][]##",
      "##......[][]..[]..##",
      "####################",
    ).joinToString("\n") + "\n"
  }

  @Test
  fun `warehouse can be doubled`() {
    input.parseWarehouse().double().toString() shouldBe listOf(
      "####################",
      "##....[]....[]..[]##",
      "##............[]..##",
      "##..[][]....[]..[]##",
      "##....[]@.....[]..##",
      "##[]##....[]......##",
      "##[]....[]....[]..##",
      "##..[][]..[]..[][]##",
      "##........[]......##",
      "####################",
    ).joinToString("\n") + "\n"
  }
}