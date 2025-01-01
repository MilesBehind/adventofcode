package de.smartsteuer.frank.adventofcode2024.day23

import de.smartsteuer.frank.adventofcode2024.day23.LanParty.computeConnections
import de.smartsteuer.frank.adventofcode2024.day23.LanParty.findLargestConnectedSet
import de.smartsteuer.frank.adventofcode2024.day23.LanParty.findTriples
import de.smartsteuer.frank.adventofcode2024.day23.LanParty.parseConnections
import de.smartsteuer.frank.adventofcode2024.day23.LanParty.part1
import de.smartsteuer.frank.adventofcode2024.day23.LanParty.part2
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LanPartyTest {
   private val input = listOf(
     "kh-tc",
     "qp-kh",
     "de-cg",
     "ka-co",
     "yn-aq",
     "qp-ub",
     "cg-tb",
     "vc-aq",
     "tb-ka",
     "wh-tc",
     "yn-cg",
     "kh-ub",
     "ta-co",
     "de-co",
     "tc-td",
     "tb-wq",
     "wh-td",
     "ta-ka",
     "td-qp",
     "aq-cg",
     "wq-ub",
     "ub-vc",
     "de-ta",
     "wq-aq",
     "wq-vc",
     "wh-yn",
     "ka-de",
     "kh-ta",
     "co-tc",
     "wh-qp",
     "tb-vc",
     "td-yn",
   )

  @Test
  fun `part 1 returns expected result`() {
    part1(input) shouldBe "7"
  }

  @Test
  fun `part 2 returns expected result`() {
    part2(input) shouldBe "co,de,ka,ta"
  }

  @Test
  fun `targets can be computed`() {
    val connections = input.parseConnections()
    val connectionsToTargets = computeConnections(connections)
    connections shouldHaveSize 32
    connectionsToTargets shouldHaveSize 16
    connectionsToTargets["kh"] shouldBe setOf("tc", "qp", "ub", "ta")
  }

  @Test
  fun `triples can be computed`() {
    findTriples(computeConnections(input.parseConnections())) shouldBe 7
  }

  @Test
  fun `longest path can be found`() {
    val pairs = input.parseConnections()
    val connections = computeConnections(pairs)
    findLargestConnectedSet(connections) shouldBe listOf("co", "de", "ka", "ta")
  }
}