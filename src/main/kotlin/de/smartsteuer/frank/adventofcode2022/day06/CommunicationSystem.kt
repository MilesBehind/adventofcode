package de.smartsteuer.frank.adventofcode2022.day06

import de.smartsteuer.frank.adventofcode2022.text

fun main() {
  val packetStream = text("/adventofcode2022/day06/packet-stream.txt")
  println("day 06, part 1: ${part1(packetStream)}")
  println("day 06, part 2: ${part2(packetStream)}")
}

fun part1(packetStream: String): Int = findSequenceOfDistinctCharacters(packetStream, 4)

fun part2(packetStream: String): Int = findSequenceOfDistinctCharacters(packetStream, 14)

private fun findSequenceOfDistinctCharacters(packetStream: String, distinctCharacterCount: Int) = packetStream
  .asSequence()
  .windowed(distinctCharacterCount)
  .withIndex()
  .first { window -> window.value.toSet().size == distinctCharacterCount }
  .index + distinctCharacterCount

