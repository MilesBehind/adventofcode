package de.smartsteuer.frank.adventofcode2021.day03

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class PowerKtTest {
  @Test
  internal fun `countOneDigits for some report results in expected list`() {
    countOneDigits(listOf("00100",
                          "11110",
                          "10110",
                          "10111",
                          "10101",
                          "01111",
                          "00111",
                          "11100",
                          "10000",
                          "11001",
                          "00010",
                          "01010")) shouldContainExactly listOf(7, 5, 8, 7, 5)
  }

  @Test
  internal fun `gamma and epsilon rates for empty report are 0 and 0`() {
    computeGammaAndEpsilonRates(emptyList()) shouldBe GammaAndEpsilonRates(0, 0)
  }

  @Test
  internal fun `gamma and epsilon rates for some report have expected values`() {
    computeGammaAndEpsilonRates(listOf("00100",
                                       "11110",
                                       "10110",
                                       "10111",
                                       "10101",
                                       "01111",
                                       "00111",
                                       "11100",
                                       "10000",
                                       "11001",
                                       "00010",
                                       "01010")) shouldBe GammaAndEpsilonRates(22, 9)
  }


  @Test
  internal fun `oxygen and CO2 rates for empty report are 0 and 0`() {
    computeOxygenAndCo2Rates(emptyList()) shouldBe OxygenAndCo2Rates(0, 0)
  }

  @Test
  internal fun `oxygen and CO2 for some report have expected values`() {
    computeOxygenAndCo2Rates(listOf("00100",
                                    "11110",
                                    "10110",
                                    "10111",
                                    "10101",
                                    "01111",
                                    "00111",
                                    "11100",
                                    "10000",
                                    "11001",
                                    "00010",
                                    "01010")) shouldBe OxygenAndCo2Rates(23, 10)
  }
}