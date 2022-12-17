package de.smartsteuer.frank.adventofcode2022.day16

import de.smartsteuer.frank.adventofcode2022.eachAndOthers
import de.smartsteuer.frank.adventofcode2022.combinations
import de.smartsteuer.frank.adventofcode2022.day16.Day16.Valves
import de.smartsteuer.frank.adventofcode2022.day16.Day16.Valves.Valve
import de.smartsteuer.frank.adventofcode2022.day16.Day16.Valves.Tunnel
import de.smartsteuer.frank.adventofcode2022.day16.Day16.part1
import de.smartsteuer.frank.adventofcode2022.day16.Day16.part2
import de.smartsteuer.frank.adventofcode2022.permutations
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ProboscideaVolcaniumTest {
  private val input = listOf(
    "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB",
    "Valve BB has flow rate=13; tunnels lead to valves CC, AA",
    "Valve CC has flow rate=2; tunnels lead to valves DD, BB",
    "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE",
    "Valve EE has flow rate=3; tunnels lead to valves FF, DD",
    "Valve FF has flow rate=0; tunnels lead to valves EE, GG",
    "Valve GG has flow rate=0; tunnels lead to valves FF, HH",
    "Valve HH has flow rate=22; tunnel leads to valve GG",
    "Valve II has flow rate=0; tunnels lead to valves AA, JJ",
    "Valve JJ has flow rate=21; tunnel leads to valve II",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 1651
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 1707
  }

  @Test
  fun `valves can be parsed`() {
    val valves = Valves.parseValves(input)
    valves.valves shouldBe mapOf(
      "AA" to Valve("AA",  0),
      "BB" to Valve("BB", 13),
      "CC" to Valve("CC",  2),
      "DD" to Valve("DD", 20),
      "EE" to Valve("EE",  3),
      "FF" to Valve("FF",  0),
      "GG" to Valve("GG",  0),
      "HH" to Valve("HH", 22),
      "II" to Valve("II",  0),
      "JJ" to Valve("JJ", 21),
    )
    valves.tunnels shouldContain Tunnel(Valve("AA",  0), Valve("DD", 20), 1)
    valves.tunnels shouldContain Tunnel(Valve("AA",  0), Valve("II",  0), 1)
    valves.tunnels shouldContain Tunnel(Valve("AA",  0), Valve("BB", 13), 1)
    valves.tunnels shouldContain Tunnel(Valve("BB", 13), Valve("CC",  2), 1)
    valves.tunnels shouldContain Tunnel(Valve("CC",  2), Valve("DD", 20), 1)
    valves.tunnels shouldContain Tunnel(Valve("DD", 20), Valve("EE",  3), 1)
    valves.tunnels shouldContain Tunnel(Valve("EE",  3), Valve("FF",  0), 1)
    valves.tunnels shouldContain Tunnel(Valve("FF",  0), Valve("GG",  0), 1)
    valves.tunnels shouldContain Tunnel(Valve("GG",  0), Valve("HH", 22), 1)
    valves.tunnels shouldContain Tunnel(Valve("II",  0), Valve("JJ", 21), 1)
    valves.tunnels shouldHaveSize 10
    valves.start shouldBe Valve("AA", 0)
  }

  @Test
  fun `all combinations of 2 list elements are found`() {
    assertSoftly {
      emptyList<Int>().combinations() shouldBe emptyList()
      listOf(1).combinations() shouldBe emptyList()
      listOf(1, 2).combinations() shouldBe listOf(listOf(1, 2))
      listOf(1, 2, 3).combinations() shouldBe listOf(listOf(1, 2), listOf(1, 3), listOf(2, 3))
      listOf(1, 2, 3, 4).combinations() shouldBe listOf(listOf(1, 2), listOf(1, 3), listOf(1, 4), listOf(2, 3), listOf(2, 4), listOf(3, 4))
    }
  }

  @Test
  fun `chooseOne chooses consecutive elements`() {
    emptyList<Int>().eachAndOthers() shouldBe emptySequence()
    listOf(1).eachAndOthers().toList() shouldBe listOf(1 to emptyList())
    listOf(1, 2).eachAndOthers().toList() shouldBe listOf(1 to listOf(2), 2 to listOf(1))
    listOf(1, 2, 3).eachAndOthers().toList() shouldBe listOf(1 to listOf(2, 3), 2 to listOf(1, 3), 3 to listOf(1, 2))
  }

  @Test
  fun `permutations are generated`() {
    emptyList<Int>().permutations() shouldBe emptySequence()
    listOf(1).permutations().toList() shouldBe listOf(listOf(1))
    listOf(1, 2).permutations().toList() shouldBe listOf(listOf(1, 2), listOf(2, 1))
    listOf(1, 2, 3).permutations().toList() shouldBe listOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 1, 2), listOf(3, 2, 1))
  }
}
