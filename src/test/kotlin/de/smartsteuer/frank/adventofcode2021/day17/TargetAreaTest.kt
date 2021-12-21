package de.smartsteuer.frank.adventofcode2021.day17

import de.smartsteuer.frank.adventofcode2021.day17.TargetArea.Companion.toTargetArea
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import kotlin.math.exp

internal class TargetAreaTest {
  @Test
  internal fun `target area is parsed correctly from string`() {
    val targetArea = "target area: x=244..303, y=-91..-54".toTargetArea()
    targetArea.x1 shouldBe 244
    targetArea.x2 shouldBe 303
    targetArea.y1 shouldBe -91
    targetArea.y2 shouldBe -54
  }

  @Test
  internal fun `target area should compute whether it is hit or not`() {
    val targetArea = "target area: x=20..30, y=-10..-5".toTargetArea()
    listOf(0 to 0, 7 to 2, 13 to 3, 18 to 3, 22 to 2, 25 to 0, 27 to -3, 28 to -7)
    (Pair( 0,   0) in targetArea).shouldBeFalse()
    (Pair( 7,   2) in targetArea).shouldBeFalse()
    (Pair(13,   3) in targetArea).shouldBeFalse()
    (Pair(18,   3) in targetArea).shouldBeFalse()
    (Pair(22,   2) in targetArea).shouldBeFalse()
    (Pair(25,   0) in targetArea).shouldBeFalse()
    (Pair(27,  -3) in targetArea).shouldBeFalse()
    (Pair(28,  -7) in targetArea).shouldBeTrue()
    (Pair(28, -12) in targetArea).shouldBeFalse()
  }

  @Test
  internal fun `maximum height is computed as expected`() {
    val targetArea = "target area: x=20..30, y=-10..-5".toTargetArea()
    val maximumSpeedAfterPassingZero = (-targetArea.y1 -1)
    val heightWhenUsingThisSpeed = maximumSpeedAfterPassingZero * (maximumSpeedAfterPassingZero + 1) / 2
    heightWhenUsingThisSpeed shouldBe 45
  }

  @Test
  internal fun `hitting velocities is computed correctly`() {
    val targetArea = "target area: x=20..30, y=-10..-5".toTargetArea()
    val hittingVelocities = findAllHittingVelocities(targetArea)
    val expectedHittingVelocities = listOf(23 to -10, 25 to -9, 27 to  -5, 29 to  -6, 22 to -6, 21 to -7,  9 to   0, 27 to  -7, 24 to -5,
                                           25 to  -7, 26 to -6, 25 to  -5,  6 to   8, 11 to -2, 20 to -5, 29 to -10,  6 to   3, 28 to -7,
                                            8 to   0, 30 to -6, 29 to  -8, 20 to -10,  6 to  7,  6 to  4,  6 to   1, 14 to  -4, 21 to -6,
                                           26 to -10,  7 to -1,  7 to   7,  8 to  -1, 21 to -9,  6 to  2, 20 to  -7, 30 to -10, 14 to -3,
                                           20 to  -8, 13 to -2,  7 to   3, 28 to  -8, 29 to -9, 15 to -3, 22 to  -5, 26 to  -8, 25 to -8,
                                           25 to  -6, 15 to -4,  9 to  -2, 15 to  -2, 12 to -2, 28 to -9, 12 to  -3, 24 to  -6, 23 to -7,
                                           25 to -10,  7 to  8, 11 to  -3, 26 to  -7,  7 to  1, 23 to -9,  6 to   0, 22 to -10, 27 to -6,
                                            8 to   1, 22 to -8, 13 to  -4,  7 to   6, 28 to -6, 11 to -4, 12 to  -4, 26 to  -9,  7 to  4,
                                           24 to -10, 23 to -8, 30 to  -8,  7 to   0,  9 to -1, 10 to -1, 26 to  -5, 22 to  -9,  6 to  5,
                                            7 to   5, 23 to -6, 28 to -10, 10 to  -2, 11 to -1, 20 to -9, 14 to  -2, 29 to  -7, 13 to -3,
                                           23 to  -5, 24 to -8, 27 to  -9, 30 to  -7, 28 to -5, 21 to -10, 7 to   9,  6 to   6, 21 to -5,
                                           27 to -10,  7 to  2, 30 to  -9, 21 to  -8, 22 to -7, 24 to -9, 20 to  -6,  6 to   9, 29 to -5,
                                            8 to  -2, 27 to -8, 30 to  -5, 24 to  -7)
    hittingVelocities.toSet().shouldBe(expectedHittingVelocities.toSet())
  }
}