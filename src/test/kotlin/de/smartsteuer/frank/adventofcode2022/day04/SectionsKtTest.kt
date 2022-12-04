package de.smartsteuer.frank.adventofcode2022.day04

import io.kotest.matchers.*
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test

class SectionsKtTest {

  private val sections = sequenceOf(
    "2-4,6-8",
    "2-3,4-5",
    "5-7,7-9",
    "2-8,3-7",
    "6-6,4-6",
    "2-6,4-8",
  )

  @Test
  fun `part 1 is correct`() {
    part1(sections) shouldBe 2
  }

  @Test
  fun `part 2 is correct`() {
    part2(sections) shouldBe 4
  }

  @Test
  fun `IntRange contains expected IntRanges`() {
    (( 1.. 1) contains ( 1.. 1)).shouldBeTrue()
    (( 1..10) contains ( 1..10)).shouldBeTrue()
    (( 1..10) contains ( 1.. 1)).shouldBeTrue()
    (( 1..10) contains (10..10)).shouldBeTrue()
    (( 1..10) contains ( 2.. 9)).shouldBeTrue()
    (( 1..10) contains (11..20)).shouldBeFalse()
    (( 1..10) contains (21..30)).shouldBeFalse()
    (( 1..10) contains ( 2..11)).shouldBeFalse()
    (( 1..10) contains (10..11)).shouldBeFalse()
    ((11..20) contains ( 1..10)).shouldBeFalse()
    ((11..20) contains (10..20)).shouldBeFalse()
  }

  @Test
  fun `intersecting IntRanges return expected IntRange`() {
    ( 1.. 1) intersect ( 1.. 1) shouldBe  1.. 1
    ( 1..10) intersect ( 1..10) shouldBe  1..10
    ( 1..10) intersect ( 1.. 1) shouldBe  1.. 1
    ( 1..10) intersect (10..10) shouldBe 10..10
    ( 1..10) intersect ( 2.. 9) shouldBe  2.. 9
    ( 1..10) intersect (11..20) shouldBe IntRange.EMPTY
    ( 1..10) intersect (21..30) shouldBe IntRange.EMPTY
    ( 1..10) intersect ( 2..11) shouldBe  2..10
    ( 1..10) intersect (10..11) shouldBe 10..10
    (11..20) intersect ( 1..10) shouldBe IntRange.EMPTY
    (11..20) intersect (10..20) shouldBe 11..20
  }

  @Test
  fun `size of IntRang has expected value`() {
    IntRange.EMPTY.size shouldBe  0
    ( 1.. 1).size       shouldBe  1
    ( 1.. 2).size       shouldBe  2
    ( 1..10).size       shouldBe 10
    (11..20).size       shouldBe 10
  }
}