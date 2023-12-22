package de.smartsteuer.frank.adventofcode2023.day22//import static org.junit.jupiter.api.Assertions.*;

import io.kotest.matchers.*
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Test

class SandSlabsTest {
  private val lines = listOf(
    "1,0,1~1,2,1",
    "0,0,2~2,0,2",
    "0,2,3~2,2,3",
    "0,0,4~0,2,4",
    "2,0,5~2,2,5",
    "0,1,6~2,1,6",
    "1,1,8~1,1,9",
  )

  @Test
  fun `part 1`() {
    part1(parseBricks(lines)) shouldBe 5
  }

  @Test
  fun `part 2`() {
    part2(parseBricks(lines)) shouldBe 7 // 6 + 0 + 0 + 0 + 0 + 1 + 0
  }

  @Test
  fun `bricks can be settled`() {
    val (bricks, movements) = settle(parseBricks(lines))
    bricks shouldContainExactlyInAnyOrder listOf(
      Brick(Pos(1, 0, 1), Pos(1, 2, 1)),
      Brick(Pos(0, 0, 2), Pos(2, 0, 2)),
      Brick(Pos(0, 2, 2), Pos(2, 2, 2)),
      Brick(Pos(0, 0, 3), Pos(0, 2, 3)),
      Brick(Pos(2, 0, 3), Pos(2, 2, 3)),
      Brick(Pos(0, 1, 4), Pos(2, 1, 4)),
      Brick(Pos(1, 1, 5), Pos(1, 1, 6)),
    )
    movements shouldBe 5
  }

  @Test
  fun `bricks can be parsed`() {
    parseBricks(lines) shouldBe listOf(
      Brick(Pos(1, 0, 1), Pos(1, 2, 1)),
      Brick(Pos(0, 0, 2), Pos(2, 0, 2)),
      Brick(Pos(0, 2, 3), Pos(2, 2, 3)),
      Brick(Pos(0, 0, 4), Pos(0, 2, 4)),
      Brick(Pos(2, 0, 5), Pos(2, 2, 5)),
      Brick(Pos(0, 1, 6), Pos(2, 1, 6)),
      Brick(Pos(1, 1, 8), Pos(1, 1, 9)),
    )
  }
}