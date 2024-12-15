package de.smartsteuer.frank.adventofcode2024.day14

import de.smartsteuer.frank.adventofcode2024.day14.RestroomRedoubt.Pos
import de.smartsteuer.frank.adventofcode2024.day14.RestroomRedoubt.parseRobots
import de.smartsteuer.frank.adventofcode2024.day14.RestroomRedoubt.part1
import de.smartsteuer.frank.adventofcode2024.day14.RestroomRedoubt.part2
import de.smartsteuer.frank.adventofcode2024.lines
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RestroomRedoubtTest {
   val input = listOf(
     "p=0,4 v=3,-3",
     "p=6,3 v=-1,-3",
     "p=10,3 v=-1,2",
     "p=2,0 v=2,-1",
     "p=0,0 v=1,3",
     "p=3,0 v=-2,-2",
     "p=7,6 v=-1,-3",
     "p=3,0 v=-1,-2",
     "p=9,3 v=2,3",
     "p=7,3 v=-1,2",
     "p=2,4 v=2,-3",
     "p=9,5 v=-3,-3",
   )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 12
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(lines("/adventofcode2024/day14/robots.txt")) shouldBe 8280
  }

  @Test
  fun `robots can be simulated`() {
    input.parseRobots().simulate(1).robots.map { it.pos } shouldContainExactlyInAnyOrder listOf(Pos(x=3, y=1), Pos(x=5, y=0), Pos(x=9, y=5),
                                                                                                Pos(x=4, y=6), Pos(x=1, y=3), Pos(x=1, y=5),
                                                                                                Pos(x=6, y=3), Pos(x=2, y=5), Pos(x=0, y=6),
                                                                                                Pos(x=6, y=5), Pos(x=4, y=1), Pos(x=6, y=2))
    input.parseRobots().simulate(100).robots.map { it.pos } shouldContainExactlyInAnyOrder listOf(Pos(x=3, y=5), Pos(x=5, y=4), Pos(x=9, y=0),
                                                                                                  Pos(x=4, y=5), Pos(x=1, y=6), Pos(x=1, y=3),
                                                                                                  Pos(x=6, y=0), Pos(x=2, y=3), Pos(x=0, y=2),
                                                                                                  Pos(x=6, y=0), Pos(x=4, y=5), Pos(x=6, y=6))
  }
}