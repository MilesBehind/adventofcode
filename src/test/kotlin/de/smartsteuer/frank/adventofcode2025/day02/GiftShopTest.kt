package de.smartsteuer.frank.adventofcode2025.day02

import de.smartsteuer.frank.adventofcode2025.day02.GiftShop.part1
import de.smartsteuer.frank.adventofcode2025.day02.GiftShop.part2
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GiftShopTest {
  private val input = listOf(
    "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 1227775554L
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe 4174379265L
  }

  @Test
  fun `id ranges can be parsed`() {
    input.parseIdRanges() shouldContainExactly listOf(
      IdRange(11, 22),
      IdRange(95, 115),
      IdRange(998, 1012),
      IdRange(1188511880, 1188511890),
      IdRange(222220, 222224),
      IdRange(1698522, 1698528),
      IdRange(446443, 446449),
      IdRange(38593856, 38593862),
      IdRange(565653, 565659),
      IdRange(824824821, 824824827),
      IdRange(2121212118, 2121212124)
    )
  }

  @Test
  fun `invalid ids can be found`() {
    input.parseIdRanges().map { it.findInvalidIds(::isInvalid) } shouldContainExactly listOf(
      listOf(11L, 22L),
      listOf(99L),
      listOf(1010L),
      listOf(1188511885L),
      listOf(222222L),
      listOf(),
      listOf(446446L),
      listOf(38593859L),
      listOf(),
      listOf(),
      listOf(),
    )
  }

  @Test
  fun `invalid ids can be found for multiplicities greater than 2`() {
    input.parseIdRanges().map { it.findInvalidIds(::isInvalid2) } shouldContainExactly listOf(
      listOf(11L, 22L),
      listOf(99L, 111L),
      listOf(999L, 1010L),
      listOf(1188511885L),
      listOf(222222L),
      listOf(),
      listOf(446446L),
      listOf(38593859L),
      listOf(565656L),
      listOf(824824824L),
      listOf(2121212121L),
    )
  }
}