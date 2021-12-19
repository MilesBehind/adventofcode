package de.smartsteuer.frank.adventofcode2021.day06

import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test


internal class LanternFishKtTest {
  @Test
  internal fun `fish population grows as expected`() {
    val startPopulation = sequenceOf(3, 4, 3, 1, 2).map { LanternFish(it) }
    simulateGrowth(startPopulation,  0).map { it.timerValue }.toList() shouldContainExactlyInAnyOrder sequenceOf(3, 4, 3, 1, 2).toList()
    simulateGrowth(startPopulation,  1).map { it.timerValue }.toList() shouldContainExactlyInAnyOrder sequenceOf(2, 3, 2, 0, 1).toList()
    simulateGrowth(startPopulation, 10).map { it.timerValue }.toList() shouldContainExactlyInAnyOrder sequenceOf(0, 1, 0, 5, 6, 0, 1, 2, 2, 3, 7, 8).toList()
    simulateGrowth(startPopulation, 18).map { it.timerValue }.toList() shouldContainExactlyInAnyOrder sequenceOf(6, 0, 6, 4, 5, 6, 0, 1, 1, 2, 6, 0, 1,
                                                                                                                 1, 1, 2, 2, 3, 3, 4, 6, 7, 8, 8, 8, 8).toList()
  }

  @Test
  internal fun `big fish population grows as expected`() {
    val startPopulation = sequenceOf(3, 4, 3, 1, 2).map { LanternFish(it) }.groupBy { it.timerValue }.mapValues { it.value.size.toLong() }
    simulateGrowth(startPopulation,   0).values.sum() shouldBe              5
    simulateGrowth(startPopulation,   1).values.sum() shouldBe              5
    simulateGrowth(startPopulation,  10).values.sum() shouldBe             12
    simulateGrowth(startPopulation,  18).values.sum() shouldBe             26
    simulateGrowth(startPopulation,  80).values.sum() shouldBe          5_934
    simulateGrowth(startPopulation, 256).values.sum() shouldBe 26_984_457_539
  }
}