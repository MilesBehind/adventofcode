package de.smartsteuer.frank.adventofcode2022.day13

import de.smartsteuer.frank.adventofcode2022.day13.Day13.ListPacket
import de.smartsteuer.frank.adventofcode2022.day13.Day13.parsePackets
import de.smartsteuer.frank.adventofcode2022.day13.Day13.part1
import de.smartsteuer.frank.adventofcode2022.day13.Day13.part2
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class DistressSignalTest {

  private val input = listOf(
    "[1,1,3,1,1]",
    "[1,1,5,1,1]",
    "",
    "[[1],[2,3,4]]",
    "[[1],4]",
    "",
    "[9]",
    "[[8,7,6]]",
    "",
    "[[4,4],4,4]",
    "[[4,4],4,4,4]",
    "",
    "[7,7,7,7]",
    "[7,7,7]",
    "",
    "[]",
    "[3]",
    "",
    "[[[]]]",
    "[[]]",
    "",
    "[1,[2,[3,[4,[5,6,7]]]],8,9]",
    "[1,[2,[3,[4,[5,6,0]]]],8,9]",
  )

  @Test
  fun `part 1 is correct`() {
    part1(input) shouldBe 1 + 2 + 4 + 6
  }

  @Test
  fun `part 2 is correct`() {
    part2(input) shouldBe 10 * 14
  }

  @Test
  fun `packets can be parsed`() {
    parsePackets("[]")                          shouldBe ListPacket()
    parsePackets("[1]")                         shouldBe p(1)
    parsePackets("[1,2,3]")                     shouldBe p(1, 2, 3)
    parsePackets("[[1],2,3,4]")                 shouldBe p(p(1), 2, 3, 4)
    parsePackets("[1,[2],3,4]")                 shouldBe p(1, p(2), 3, 4)
    parsePackets("[1,2,3,[4]]")                 shouldBe p(1, 2, 3, p(4))
    parsePackets("[[[]]]")                      shouldBe p(p(p()))
    parsePackets("[[4,4],4,4]")                 shouldBe p(p(4, 4), 4, 4)
    parsePackets("[1,[2,[3,[4,[5,6,7]]]],8,9]") shouldBe p(1, p(2, p(3, p(4, p(5, 6, 7)))), 8, 9)
  }

  @Test
  fun `comparison works as expected`() {
    (parsePackets("[[0,[[9,6,4,5]],[]]]") < parsePackets("[[10,9,[[5,7],4,[5,8,0,9,8],[0,10],[3,1]],4]]")).shouldBeTrue()
    (parsePackets("[[6],[2]]") < parsePackets("[[9,[[8,2]]]")).shouldBeTrue()

  }

  @Suppress("KotlinConstantConditions")
  private fun p(vararg packets: Any) = ListPacket(packets.map { packet ->
    when (packet) {
      is Int        -> Day13.NumberPacket(packet)
      is ListPacket -> packet
      else          -> throw IllegalArgumentException("invalid packet: $packet")
    }
  })
}