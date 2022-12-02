package de.smartsteuer.frank.adventofcode2022.day01

import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class CaloriesKtTest {
  @Test
  internal fun `splits empty list to empty list`() {
    emptyList<Int>().split { it == 0 } shouldContainExactly emptyList()
  }

  @Test
  internal fun `splits list to expected list of lists`() {
    listOf(1, 2, 3, 0, 4, 5, 0, 6, 0, 7, 8, 9, 0, 10).split { it == 0 } shouldContainExactly
            listOf(listOf(1, 2, 3), listOf(4, 5), listOf(6), listOf(7, 8, 9), listOf(10))
  }
}