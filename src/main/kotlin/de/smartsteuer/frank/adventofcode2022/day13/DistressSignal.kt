package de.smartsteuer.frank.adventofcode2022.day13

import de.smartsteuer.frank.adventofcode2022.lines

fun main() {
  val input = lines("/adventofcode2022/day13/packets.txt")
  println("day 13, part 1: ${Day13.part1(input)}")
  println("day 13, part 2: ${Day13.part2(input)}")
}

object Day13 {
  fun part1(input: List<String>): Int {
    val result = parsePackets(input).map { (packet1, packet2) -> packet1 < packet2 }
    return result.mapIndexed { index, packetResult -> if (packetResult) index + 1 else 0 }.sum()
  }

  fun part2(input: List<String>): Int {
    val dividerPackets = listOf(parsePackets("[[2]]"), parsePackets("[[6]]"))
    val sorted         = (parsePackets(input).flatten() + dividerPackets).sorted()
    return dividerPackets.map { sorted.indexOf(it) + 1 }.fold(1) { product, factor -> product * factor}
  }

  sealed interface Packet: Comparable<Packet> {
    fun asPacketList(): ListPacket = if (this is NumberPacket) ListPacket(listOf(this)) else this as ListPacket
  }

  @JvmInline
  value class NumberPacket(val number: Int): Packet {
    override fun toString() = number.toString()
    override fun compareTo(other: Packet): Int =
      if (other is NumberPacket) this.number.compareTo(other.number) else asPacketList().compareTo(other)
  }

  @JvmInline
  value class ListPacket(private val elements: List<Packet> = emptyList()): Packet {
    override fun toString() = elements.toString()
    operator fun plus(packet: Packet) = ListPacket(elements + packet)
    override fun compareTo(other: Packet): Int {
      if (other is ListPacket) {
        this.elements.zip(other.elements).forEach { (first, second) ->
          val result = first.compareTo(second)
          if (result != 0) return result
        }
        return elements.size.compareTo(other.elements.size)
      }
      return this.compareTo(other.asPacketList())
    }
  }

  fun parsePackets(input: List<String>): List<List<ListPacket>> =
    input.chunked(3).map { lines -> lines.take(2).map { line -> parsePackets(line) } }

  fun parsePackets(line: String): ListPacket {
    tailrec fun parsePackets(input: String, stack: List<ListPacket>): ListPacket {
      if (input.isEmpty()) return stack.first()
      val char = input.first()
      return when {
        char == '['    -> parsePackets(input.drop(1), stack + ListPacket())
        char == ']'    -> parsePackets(input.drop(1), if (stack.size == 1) stack else stack.dropLast(2) + (stack.dropLast(1).last() + stack.last()))
        char == ','    -> parsePackets(input.drop(1), stack)
        char.isDigit() -> {
          val digits = input.takeWhile { it.isDigit() }
          val numberPacket = NumberPacket(digits.toInt())
          val newLastStackElement = stack.last() + numberPacket
          parsePackets(input.drop(digits.length), stack.dropLast(1) + newLastStackElement)
        }
        else           -> throw IllegalArgumentException("invalid character: $char")
      }
    }
    return parsePackets(line, emptyList())
  }
}