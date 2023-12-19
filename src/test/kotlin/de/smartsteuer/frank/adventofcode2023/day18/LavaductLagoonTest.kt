package de.smartsteuer.frank.adventofcode2023.day18

import de.smartsteuer.frank.adventofcode2023.day18.Direction.*
import de.smartsteuer.frank.adventofcode2023.lines
import io.kotest.matchers.*
import org.junit.jupiter.api.Test

class LavaductLagoonTest {
  private val input = listOf(
    "R 6 (#70c710)",
    "D 5 (#0dc571)",
    "L 2 (#5713f0)",
    "D 2 (#d2c081)",
    "R 2 (#59c680)",
    "D 2 (#411b91)",
    "L 5 (#8ceee2)",
    "U 2 (#caa173)",
    "L 1 (#1b58a2)",
    "U 2 (#caa171)",
    "R 2 (#7807d2)",
    "U 3 (#a77fa3)",
    "L 2 (#015232)",
    "U 2 (#7a21e3)",
  )

  @Test
  fun `part 1`() {
    part1(parseDigPlan(input)) shouldBe 62
  }

  @Test
  fun `part 1 full`() {
    val outline   = parseDigPlan(lines("/adventofcode2023/day18/dig-plan.txt")).drawOutline().normalize().visualize()
    val intervals = parseDigPlan(lines("/adventofcode2023/day18/dig-plan.txt")).fill().mapValues { (_, value) -> value.sortedBy { it.first } }.entries.sortedBy { it.key }
    val fill      = parseDigPlan(lines("/adventofcode2023/day18/dig-plan.txt")).fillToSet().normalize().visualize()
    outline.indices.forEach { y ->
      println(outline[y])
      println("${intervals[y].key}: ${intervals[y].value} => ${intervals[y].value.union()}")
      println(fill[y])
    }
  }

  @Test
  fun `part 2`() {
    part2(parseDigPlan(input)) shouldBe 952_408_144_115
  }

  @Test
  fun `dig-plan can be parsed`() {
    val digPlan = parseDigPlan(input)
    digPlan.operations shouldBe listOf(
      DigOperation(RIGHT, 6, 0x70c710),
      DigOperation(DOWN,  5, 0x0dc571),
      DigOperation(LEFT,  2, 0x5713f0),
      DigOperation(DOWN,  2, 0xd2c081),
      DigOperation(RIGHT, 2, 0x59c680),
      DigOperation(DOWN,  2, 0x411b91),
      DigOperation(LEFT,  5, 0x8ceee2),
      DigOperation(UP,    2, 0xcaa173),
      DigOperation(LEFT,  1, 0x1b58a2),
      DigOperation(UP,    2, 0xcaa171),
      DigOperation(RIGHT, 2, 0x7807d2),
      DigOperation(UP,    3, 0xa77fa3),
      DigOperation(LEFT,  2, 0x015232),
      DigOperation(UP,    2, 0x7a21e3),
    )
  }

  @Test
  fun `colors can be converted`() {
    parseDigPlan(input).convertColors().operations shouldBe listOf(
      DigOperation(RIGHT, 461937, 0x70c710),
      DigOperation(DOWN,   56407, 0x0dc571),
      DigOperation(RIGHT, 356671, 0x5713f0),
      DigOperation(DOWN,  863240, 0xd2c081),
      DigOperation(RIGHT, 367720, 0x59c680),
      DigOperation(DOWN,  266681, 0x411b91),
      DigOperation(LEFT,  577262, 0x8ceee2),
      DigOperation(UP,    829975, 0xcaa173),
      DigOperation(LEFT,  112010, 0x1b58a2),
      DigOperation(DOWN,  829975, 0xcaa171),
      DigOperation(LEFT,  491645, 0x7807d2),
      DigOperation(UP,    686074, 0xa77fa3),
      DigOperation(LEFT,    5411, 0x015232),
      DigOperation(UP,    500254, 0x7a21e3),
    )
  }

  @Test
  fun `dig plan can be filled`() {
    parseDigPlan(input).fillToSet().visualize() shouldBe listOf(
      "#######",
      "#######",
      "#######",
      "..#####",
      "..#####",
      "#######",
      "#####..",
      "#######",
      ".######",
      ".######",
    )
  }

  @Test
  fun `outline can be drawn`() {
    parseDigPlan(input).drawOutline().visualize() shouldBe listOf(
      "#######",
      "#.....#",
      "###...#",
      "..#...#",
      "..#...#",
      "###.###",
      "#...#..",
      "##..###",
      ".#....#",
      ".######",
    )
  }

  @Test
  fun `int ranges can be combined`() {
    listOf(0..6            ).union() shouldBe listOf(0..6)
    listOf(0..6, 0..6      ).union() shouldBe listOf(0..6)
    listOf(0..2, 2..6      ).union() shouldBe listOf(0..6)
    listOf(4..6, 0..2, 0..4).union() shouldBe listOf(0..6)
    listOf(0..1, 4..6, 1..4).union() shouldBe listOf(0..6)
    listOf(0..1, 4..6      ).union() shouldBe listOf(0..1, 4..6)

  }

  private fun Set<Pos>.normalize(): Set<Pos> {
    val minX = minOf { it.x }
    val minY = minOf { it.y }
    return map { Pos(it.x - minX, it.y - minY) }.toSet()
  }

  private fun Set<Pos>.dimensions(): Pair<Int, Int> =
    maxOf { it.x } + 1 to maxOf { it.y } + 1

  private fun Set<Pos>.visualize(): List<String> {
    val (width, height) = dimensions()
    return (0..<height).map { y ->
      (0..<width).map { x ->
        if (Pos(x, y) in this) '#' else '.'
      }.joinToString(separator = "")
    }
  }

  private fun DigPlan.drawOutline(): Set<Pos> {
    tailrec fun drawOutline(pos: Pos, index: Int, result: MutableSet<Pos>): Set<Pos> {
      if (index >= operations.size) return result
      val operation = operations[index]
      val holes = (1..operation.length).map { delta -> pos + operation.direction.delta * delta }
      return drawOutline(holes.last(), index + 1, result.apply { addAll(holes) })
    }
    return drawOutline(Pos(0, 0), 0, mutableSetOf())
  }

  private fun DigPlan.fillToSet(): Set<Pos> {
    val operations           = computePositionedDigOperations()
    val upOperations         = operations.filter { it.direction == UP }
    val downOperations       = operations.filter { it.direction == DOWN }
    val horizontalOperations = operations.filter { it.direction == LEFT || it.direction == RIGHT }
    return buildSet {
      horizontalOperations.forEach { operation ->
        (0..operation.length).forEach { delta -> add(operation.start + operation.direction.delta * delta) }
      }
      upOperations.forEach { upOperation ->
        val x = upOperation.start.x
        (upOperation.start.y downTo upOperation.start.y - upOperation.length).forEach { y ->
          val downOperation = downOperations.filter { it.start.x > x && it.start.y <= y && it.start.y + it.length >= y }.minBy { it.start.x }
          (x..downOperation.start.x).forEach { add(Pos(it, y)) }
        }
      }
    }
  }
}