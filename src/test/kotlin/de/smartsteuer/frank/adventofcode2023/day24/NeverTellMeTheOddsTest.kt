package de.smartsteuer.frank.adventofcode2023.day24

import io.kotest.matchers.*
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.jupiter.api.Test

class NeverTellMeTheOddsTest {
  private val lines = listOf(
    "19, 13, 30 @ -2,  1, -2",
    "18, 19, 22 @ -1, -1, -2",
    "20, 25, 34 @ -2, -2, -4",
    "12, 31, 28 @ -1, -2, -1",
    "20, 19, 15 @  1, -5, -3",
  )

  @Test
  fun `part 1`() {
    part1(parseHailstones(lines)) shouldBe 2
  }

  @Test
  fun `hailstone path intersection points can be computed`() {
    val hailstones = parseHailstones(lines)
    checkIntersection(hailstones[0], hailstones[1], 14.333, 15.333) // 43 over 3,  46 over 3)  // 14.333, 15.333
    checkIntersection(hailstones[0], hailstones[2], 11.667, 16.667) // 35 over 3,  50 over 3)  // 11.667, 16.667
    checkIntersection(hailstones[0], hailstones[3],  6.2,   19.4  ) // 31 over 5,  97 over 5)  //  6.2,   19.4
    checkIntersection(hailstones[0], hailstones[4], 21.444, 11.777) //193 over 9, 106 over 9)  // 21.444, 11.777
    checkIntersection(hailstones[1], hailstones[2],   null,   null) //      null,       null)
    checkIntersection(hailstones[1], hailstones[3], -6.0,     -5.0) // -6 over 1,  -5 over 1)  // -6,     -5
    checkIntersection(hailstones[1], hailstones[4], 19.666, 20.666) // 59 over 3,  62 over 3)  // 19.666, 20.666
    checkIntersection(hailstones[2], hailstones[3], -2.0,      3.0) // -2 over 1,        3.r)  // -2,      3
    checkIntersection(hailstones[2], hailstones[4], 19.0,     24.0) //      19.r,       24.r)  // 19,     24
    checkIntersection(hailstones[3], hailstones[4], 16.0,     39.0) //      16.r,       39.r)  // 16,     39
  }

  private fun checkIntersection(hailstone1: Hailstone, hailstone2: Hailstone, expectedX: Double?, expectedY: Double?) {
    val collisionPosition = computePathIntersectionPointXY(hailstone1, hailstone2)
    println(collisionPosition)
    if (expectedX != null && expectedY != null) {
      collisionPosition.shouldNotBeNull()
      collisionPosition.x.toDouble() shouldBe (expectedX plusOrMinus 0.001)
      collisionPosition.y.toDouble() shouldBe (expectedY plusOrMinus 0.001)
    } else {
      collisionPosition.shouldBeNull()
    }
  }

  @Test
  fun `combinations can be computed`() {
    emptyList<Int>().combinations().shouldBeEmpty()
    listOf(1).combinations().shouldBeEmpty()
    listOf(1, 2).combinations() shouldBe listOf(1 to 2)
    listOf(1, 2, 3).combinations() shouldBe listOf(1 to 2, 1 to 3, 2 to 3)
    listOf(1, 2, 3, 4).combinations() shouldBe listOf(1 to 2, 1 to 3, 1 to 4, 2 to 3, 2 to 4, 3 to 4)
    (1..300).toList().combinations() shouldHaveSize (300 * 299) / 2
  }

  @Test
  fun `hailstones can be parsed`() {
    parseHailstones(lines) shouldBe listOf(
      Hailstone(Vec(19.toBigInteger(), 13.toBigInteger(), 30.toBigInteger()), Vec((-2).toBigInteger(), ( 1).toBigInteger(), (-2).toBigInteger())),
      Hailstone(Vec(18.toBigInteger(), 19.toBigInteger(), 22.toBigInteger()), Vec((-1).toBigInteger(), (-1).toBigInteger(), (-2).toBigInteger())),
      Hailstone(Vec(20.toBigInteger(), 25.toBigInteger(), 34.toBigInteger()), Vec((-2).toBigInteger(), (-2).toBigInteger(), (-4).toBigInteger())),
      Hailstone(Vec(12.toBigInteger(), 31.toBigInteger(), 28.toBigInteger()), Vec((-1).toBigInteger(), (-2).toBigInteger(), (-1).toBigInteger())),
      Hailstone(Vec(20.toBigInteger(), 19.toBigInteger(), 15.toBigInteger()), Vec(( 1).toBigInteger(), (-5).toBigInteger(), (-3).toBigInteger())),
    )
  }
}