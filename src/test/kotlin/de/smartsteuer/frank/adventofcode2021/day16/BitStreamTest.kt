package de.smartsteuer.frank.adventofcode2021.day16

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class BitStreamTest {
  @Test
  internal fun `bits are consumed as expected`() {
    val input: String = listOf(0b0001_0011_0111_1111, 0b1000_1100_1110_1111).joinToString("") { it.toString(16) }
    (1..8).map {
      val bitStream = BitStream(input)
      val result    = bitStream.consume(it)
      Pair(bitStream.bitReadPosition, result)
    } shouldBe listOf(
      Pair(1,  0L),
      Pair(2,  0L),
      Pair(3,  0L),
      Pair(4,  1L),
      Pair(5,  2L),
      Pair(6,  4L),
      Pair(7,  9L),
      Pair(8, 19L),
    )
    (1..8).map {
      val bitStream = BitStream(input)
      bitStream.consume(7)
      val result    = bitStream.consume(it)
      Pair(bitStream.bitReadPosition, result)
    } shouldBe listOf(
      Pair( 8,   1L),
      Pair( 9,   2L),
      Pair(10,   5L),
      Pair(11,  11L),
      Pair(12,  23L),
      Pair(13,  47L),
      Pair(14,  95L),
      Pair(15, 191L),
    )
  }

  @Test
  internal fun `type 4 stream can be parsed`() {
    val bitStream = BitStream("D2FE28")
    bitStream.version() shouldBe 6
    bitStream.type()    shouldBe 4
    bitStream.number()  shouldBe 2021  // 0b0111_1110_0101
  }

  @Test
  internal fun `non-type 4 stream with mode 0 can be parsed`() {
    val bitStream = BitStream("38006F45291200")
    bitStream.version() shouldBe 1
    bitStream.type()    shouldBe 6
    bitStream.mode()    shouldBe 0
    bitStream.length(mode = 0)  shouldBe 27
    val startPosition = bitStream.bitReadPosition
    // first literal
    bitStream.version() shouldBe 6
    bitStream.type()    shouldBe 4
    bitStream.number()  shouldBe 10
    // second literal
    bitStream.version() shouldBe 2
    bitStream.type()    shouldBe 4
    bitStream.number()  shouldBe 20

    bitStream.bitReadPosition - startPosition shouldBe 27
  }

  @Test
  internal fun `non-type 4 stream with mode 1 can be parsed`() {
    val bitStream = BitStream("EE00D40C823060")
    bitStream.version() shouldBe 7
    bitStream.type()    shouldBe 3
    bitStream.mode()    shouldBe 1
    bitStream.length(mode = 1)  shouldBe 3
    // first literal
    bitStream.version() shouldBe 2
    bitStream.type()    shouldBe 4
    bitStream.number()  shouldBe 1
    // second literal
    bitStream.version() shouldBe 4
    bitStream.type()    shouldBe 4
    bitStream.number()  shouldBe 2
    // third literal
    bitStream.version() shouldBe 1
    bitStream.type()    shouldBe 4
    bitStream.number()  shouldBe 3
  }

  @Test
  internal fun `packet 1 is read correctly`() {
    val bitStream = BitStream("8A004A801A8002F478")
    val packet = bitStream.packet()
    println("packet = $packet")
    packet.sumOfVersions() shouldBe 16
  }

  @Test
  internal fun `packet 2 is read correctly`() {
    val bitStream = BitStream("C0015000016115A2E0802F182340")
    val packet = bitStream.packet()
    println("packet = $packet")
    packet.sumOfVersions() shouldBe 23
  }

  @Test
  internal fun `packet 3 is read correctly`() {
    val bitStream = BitStream("A0016C880162017C3686B18A3D4780")
    val packet = bitStream.packet()
    println("packet = $packet")
    packet.sumOfVersions() shouldBe 31
  }

  @Test
  internal fun `packet values are correct`() {
    BitStream("C200B40A82"                ).packet().value() shouldBe  3
    BitStream("04005AC33890"              ).packet().value() shouldBe 54
    BitStream("880086C3E88112"            ).packet().value() shouldBe  7
    BitStream("CE00C43D881120"            ).packet().value() shouldBe  9
    BitStream("D8005AC2A8F0"              ).packet().value() shouldBe  1
    BitStream("F600BC2D8F"                ).packet().value() shouldBe  0
    BitStream("9C005AC2F8F0"              ).packet().value() shouldBe  0
    BitStream("9C0141080250320F1802104A08").packet().value() shouldBe  1
  }
}