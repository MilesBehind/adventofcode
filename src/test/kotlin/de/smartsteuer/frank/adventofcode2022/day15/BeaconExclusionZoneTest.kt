package de.smartsteuer.frank.adventofcode2022.day15

import de.smartsteuer.frank.adventofcode2022.day15.Day15.Space
import de.smartsteuer.frank.adventofcode2022.day15.Day15.part1
import de.smartsteuer.frank.adventofcode2022.day15.Day15.part2
import de.smartsteuer.frank.adventofcode2022.merge
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class BeaconExclusionZoneTest {
  private val input = listOf(
    "Sensor at x=2, y=18: closest beacon is at x=-2, y=15",
    "Sensor at x=9, y=16: closest beacon is at x=10, y=16",
    "Sensor at x=13, y=2: closest beacon is at x=15, y=3",
    "Sensor at x=12, y=14: closest beacon is at x=10, y=16",
    "Sensor at x=10, y=20: closest beacon is at x=10, y=16",
    "Sensor at x=14, y=17: closest beacon is at x=10, y=16",
    "Sensor at x=8, y=7: closest beacon is at x=2, y=10",
    "Sensor at x=2, y=0: closest beacon is at x=2, y=10",
    "Sensor at x=0, y=11: closest beacon is at x=2, y=10",
    "Sensor at x=20, y=14: closest beacon is at x=25, y=17",
    "Sensor at x=17, y=20: closest beacon is at x=21, y=22",
    "Sensor at x=16, y=7: closest beacon is at x=15, y=3",
    "Sensor at x=14, y=3: closest beacon is at x=15, y=3",
    "Sensor at x=20, y=1: closest beacon is at x=15, y=3",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input, 10, printCells = true) shouldBe 26
  }

  @Test
  fun `part 2 is correct`() {
    part2(input, 0..20, printCells = true) shouldBe 56000011
  }

  @Test
  fun coverageIsCorrect() {
    assertSoftly {
      Space.sensorCoverage(Space.Coordinate( 2, 18), Space.Coordinate(-2, 15), 10) shouldBe IntRange.EMPTY
      Space.sensorCoverage(Space.Coordinate( 9, 16), Space.Coordinate(10, 16), 10) shouldBe IntRange.EMPTY
      Space.sensorCoverage(Space.Coordinate(13,  2), Space.Coordinate(15,  3), 10) shouldBe IntRange.EMPTY
      Space.sensorCoverage(Space.Coordinate(12, 14), Space.Coordinate(10, 16), 10) shouldBe 12..12
      Space.sensorCoverage(Space.Coordinate(10, 20), Space.Coordinate(10, 16), 10) shouldBe IntRange.EMPTY
      Space.sensorCoverage(Space.Coordinate(14, 17), Space.Coordinate(10, 16), 10) shouldBe IntRange.EMPTY
      Space.sensorCoverage(Space.Coordinate( 8,  7), Space.Coordinate( 2, 10), 10) shouldBe  2..14
      Space.sensorCoverage(Space.Coordinate( 2,  0), Space.Coordinate( 2, 10), 10) shouldBe  2.. 2
      Space.sensorCoverage(Space.Coordinate( 0, 11), Space.Coordinate( 2, 10), 10) shouldBe -2.. 2
      Space.sensorCoverage(Space.Coordinate(20, 14), Space.Coordinate(25, 17), 10) shouldBe 16..24
      Space.sensorCoverage(Space.Coordinate(17, 20), Space.Coordinate(21, 22), 10) shouldBe IntRange.EMPTY
      Space.sensorCoverage(Space.Coordinate(16,  7), Space.Coordinate(15,  3), 10) shouldBe 14..18
      Space.sensorCoverage(Space.Coordinate(14,  3), Space.Coordinate(15,  3), 10) shouldBe IntRange.EMPTY
      Space.sensorCoverage(Space.Coordinate(20,  1), Space.Coordinate(15,  3), 10) shouldBe IntRange.EMPTY
    }
  }

  @Test
  fun `intervals can be merged`() {
    listOf(      10..14, 16..18, 2..6, 8..10, 11..20).merge( 1.. 5) shouldContainExactly listOf(1..6, 8..20)
    listOf(1..5,         16..18, 2..6, 8..10, 11..20).merge(10..14) shouldContainExactly listOf(1..6, 8..20)
    listOf(1..5, 10..14,         2..6, 8..10, 11..20).merge(16..18) shouldContainExactly listOf(1..6, 8..20)
    listOf(1..5, 10..14, 16..18,       8..10, 11..20).merge( 2.. 6) shouldContainExactly listOf(1..6, 8..20)
    listOf(1..5, 10..14, 16..18, 2..6,        11..20).merge( 8..10) shouldContainExactly listOf(1..6, 8..20)
    listOf(1..5, 10..14, 16..18, 2..6, 8..10        ).merge(11..20) shouldContainExactly listOf(1..6, 8..20)
  }
}
