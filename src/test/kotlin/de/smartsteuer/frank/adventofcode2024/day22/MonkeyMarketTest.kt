package de.smartsteuer.frank.adventofcode2024.day22

import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.SequenceOfDifferences
import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.SequenceOfDifferences.Companion.fromList
import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.computeBestSequence
import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.computePrices
import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.computeSequencesToPrices
import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.generateSecrets
import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.parseSecrets
import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.part1
import de.smartsteuer.frank.adventofcode2024.day22.MonkeyMarket.part2
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MonkeyMarketTest {
  private val input = listOf(
    "1",
    "10",
    "100",
    "2024",
  )

  private val input2 = listOf(
    "1",
    "2",
    "3",
    "2024",
  )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe 37_327_623
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input2) shouldBe 23
  }

  @Test
  fun `secrets can be generated`() {
    generateSecrets(123L).drop(1).take(10).toList() shouldContainExactly listOf(
      15_887_950,
      16_495_136,
         527_345,
         704_524,
       1_553_684,
      12_683_156,
      11_100_544,
      12_249_484,
       7_753_432,
       5_908_254,
    )
  }

  @Test
  fun `prices can be computed`() {
    computePrices(123L).take(10) shouldContainExactly listOf(3, 0, 6, 5, 4, 4, 6, 4, 4, 2)
  }

  @Test
  fun `sequences for prices are correct`() {
    val sequencesToPrices = computeSequencesToPrices(123, 9)
    sequencesToPrices shouldHaveSize 6
    sequencesToPrices[fromList(listOf(-3,  6, -1, -1))] shouldBe 4
    sequencesToPrices[fromList(listOf( 6, -1, -1,  0))] shouldBe 4
    sequencesToPrices[fromList(listOf(-1, -1,  0,  2))] shouldBe 6
    sequencesToPrices[fromList(listOf(-1,  0,  2, -2))] shouldBe 4
    sequencesToPrices[fromList(listOf( 0,  2, -2,  0))] shouldBe 4
    sequencesToPrices[fromList(listOf( 2, -2,  0, -2))] shouldBe 2

    computeSequencesToPrices(123).keys shouldHaveSize 1924
  }

  @Test
  fun `best sequence can be computed`() {
    val (bestSequence, bestPrice) = computeBestSequence(input2.parseSecrets())
    bestSequence shouldBe SequenceOfDifferences(-2, 1, -1, 3)
    bestPrice shouldBe 23
  }
}