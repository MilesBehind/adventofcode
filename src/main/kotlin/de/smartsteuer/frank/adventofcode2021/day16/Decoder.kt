package de.smartsteuer.frank.adventofcode2021.day16

import de.smartsteuer.frank.adventofcode2021.lines

fun main() {
  val bitStream = BitStream(lines("/adventofcode2021/day16/hex-stream.txt").first())
  val packet = bitStream.packet()
  val sumOfAllVersions = packet.sumOfVersions()
  println("sum of all versions = $sumOfAllVersions")
  val value = packet.value()
  println("value = $value")
}

class BitStream(private val input: IntArray, var bitReadPosition: Int = 0) {
  constructor(hexStream: String): this(hexStream.map { it.toString().toInt(16) }.toIntArray())

  fun packet(): Packet {
    val version = version()
    val type    = type()
    if (type == 4) {
      val literal = number()
      return LiteralPacket(version, type, literal)
    } else {
      val mode = mode()
      if (mode == 0) {
        val sumOfSubPacketLengths = length(mode)
        val startPosition = bitReadPosition
        val subPackets = mutableListOf<Packet>()
        while (bitReadPosition - startPosition < sumOfSubPacketLengths) {
          subPackets += packet()
        }
        return OperatorPacket(version, type, subPackets)
      } else {
        val subPacketCount = length(mode)
        val subPackets = mutableListOf<Packet>()
        repeat(subPacketCount) {
          subPackets += packet()
        }
        return OperatorPacket(version, type, subPackets)
      }
    }
  }

  fun version():          Int = consume(3).toInt()
  fun type():             Int = consume(3).toInt()
  fun mode():             Int = consume(1).toInt()
  fun length(mode: Int):  Int = consume(if (mode == 0) 15 else 11).toInt()

  tailrec fun number(result: Long = 0): Long =
    if (consume(1) == 0L) result shl 4 or consume(4) else number(result shl 4 or consume(4))

  infix fun consume(bitCount: Int): Long {
    val result = (0 until bitCount).fold(0L) { result, offset ->
      result shl 1 or bit(bitReadPosition + offset).toLong()
    }
    bitReadPosition += bitCount
    return result
  }

  private infix fun bit(bitReadPosition: Int): Int = (input[bitReadPosition shr 2] shr ((bitReadPosition and 0b11) xor 0b11)) and 0b0001
}

sealed interface Packet {
  val version: Int
  val type:    Int
  fun sumOfVersions(): Long
  fun value(): Long
}

data class LiteralPacket(override val version: Int, override val type: Int, val literal: Long): Packet {
  override fun sumOfVersions(): Long = version.toLong()
  override fun value(): Long = literal
}

data class OperatorPacket(override val version: Int, override val type: Int, val packets: List<Packet>): Packet {
  override fun sumOfVersions(): Long = version.toLong() + packets.sumOf { it.sumOfVersions() }
  override fun value(): Long = when (type) {
    0    -> packets.sumOf { it.value() }
    1    -> packets.fold(1) { product, packet -> product * packet.value() }
    2    -> packets.minOf { it.value() }
    3    -> packets.maxOf { it.value() }
    5    -> if (packets[0].value() >  packets[1].value()) 1 else 0
    6    -> if (packets[0].value() <  packets[1].value()) 1 else 0
    7    -> if (packets[0].value() == packets[1].value()) 1 else 0
    else -> error("invalid type: $type")
  }
}