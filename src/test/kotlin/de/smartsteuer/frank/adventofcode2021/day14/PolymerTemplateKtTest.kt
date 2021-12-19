package de.smartsteuer.frank.adventofcode2021.day14

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test

internal class PolymerTemplateKtTest {
  private val start = "NNCB"
  private val polymerTemplate = listOf("CH -> B",
                                       "HH -> N",
                                       "CB -> H",
                                       "NH -> C",
                                       "HB -> C",
                                       "HC -> B",
                                       "HN -> C",
                                       "NN -> C",
                                       "BH -> H",
                                       "NC -> B",
                                       "NB -> B",
                                       "BN -> B",
                                       "BB -> N",
                                       "BC -> B",
                                       "CC -> N",
                                       "CN -> C")

  @Test
  internal fun `applying rules to input produces expected output`() {
    val rules = parseRules(polymerTemplate)
    val outputAfterStepX = (1..4).map { applyRules(start, rules, it) }
    outputAfterStepX[0] shouldBe "NCNBCHB"
    outputAfterStepX[1] shouldBe "NBCCNBBBCBHCB"
    outputAfterStepX[2] shouldBe "NBBBCNCCNBBNBNBBCHBHHBCHB"
    outputAfterStepX[3] shouldBe "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB"
    val counts = outputAfterStepX.map { polymer -> polymer.groupBy { it }.mapValues { it.value.size.toLong() } }
    counts[0] shouldBe mapOf('N' to  2L, 'C' to  2L, 'B' to  2L, 'H' to 1L)
    counts[1] shouldBe mapOf('N' to  2L, 'C' to  4L, 'B' to  6L, 'H' to 1L)
    counts[2] shouldBe mapOf('N' to  5L, 'C' to  5L, 'B' to 11L, 'H' to 4L)
    counts[3] shouldBe mapOf('N' to 11L, 'C' to 10L, 'B' to 23L, 'H' to 5L)
    maxMinusMin(outputAfterStepX[0]) shouldBe  1
    maxMinusMin(outputAfterStepX[1]) shouldBe  5
    maxMinusMin(outputAfterStepX[2]) shouldBe  7
    maxMinusMin(outputAfterStepX[3]) shouldBe 18
  }

  @Test
  internal fun `applying rules to input using polymer class produces expected output`() {
    val rules = parseRules(polymerTemplate)
    // NNCB => N + N + C +    B
    val polymer = Polymer(start, rules)
    val p1 = polymer.applyRules(times = 1)
    val p2 = polymer.applyRules(times = 2)
    val p3 = polymer.applyRules(times = 3)
    val p4 = polymer.applyRules(times = 4)
    println(polymer)
    println(p1)
    println(p2)
    println(p3)
    println(p4)
    p1.counts() shouldBe mapOf('N' to  2L, 'C' to  2L, 'B' to  2L, 'H' to 1L)
    p2.counts() shouldBe mapOf('N' to  2L, 'C' to  4L, 'B' to  6L, 'H' to 1L)
    p3.counts() shouldBe mapOf('N' to  5L, 'C' to  5L, 'B' to 11L, 'H' to 4L)
    p4.counts() shouldBe mapOf('N' to 11L, 'C' to 10L, 'B' to 23L, 'H' to 5L)
    p1.maxMinusMin() shouldBe  1
    p2.maxMinusMin() shouldBe  5
    p3.maxMinusMin() shouldBe  7
    p4.maxMinusMin() shouldBe 18
  }
}