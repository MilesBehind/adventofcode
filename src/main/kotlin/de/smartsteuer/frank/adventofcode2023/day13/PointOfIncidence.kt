package de.smartsteuer.frank.adventofcode2023.day13

import de.smartsteuer.frank.adventofcode2022.day01.split
import de.smartsteuer.frank.adventofcode2023.lines
import java.lang.Integer.bitCount
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
  val mirrorMaps = parseMap(lines("/adventofcode2023/day13/map.txt"))
  measureTime { println("part 1: ${part1(mirrorMaps)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(mirrorMaps)}") }.also { println("part 2 took $it") }
}

internal fun part1(mirrorMaps: List<MirrorMap>): Int =
  mirrorMaps.mapNotNull { it.findVerticalMirror() }.sum() + 100 * mirrorMaps.mapNotNull { it.findHorizontalMirror() }.sum()

internal fun part2(mirrorMaps: List<MirrorMap>): Int =
  mirrorMaps.mapNotNull { it.findVerticalMirror(1) }.sum() + 100 * mirrorMaps.mapNotNull { it.findHorizontalMirror(1) }.sum()

internal data class MirrorMap(val width: Int, val height: Int, val rowBits: List<Int>, val columnBits: List<Int>) {
  fun findVerticalMirror  (expectedDefects: Int = 0): Int? = findMirror(rowBits,    width,  expectedDefects)
  fun findHorizontalMirror(expectedDefects: Int = 0): Int? = findMirror(columnBits, height, expectedDefects)

  private fun findMirror(input: List<Int>, size: Int, expectedDefects: Int): Int? {
    return (1..<size).firstOrNull { pos ->
      input.sumOf { bits  ->
        val relevantBitCount = min(pos, size - pos)
        val rightBits        = bits.getSlice(pos, pos - relevantBitCount + 1)
        val leftBitsMirrored = bits.getSlice(pos + relevantBitCount, pos + 1).reverse(relevantBitCount)
        bitCount(rightBits xor leftBitsMirrored)
      } == expectedDefects
    }?.let { size - it }
  }
}

internal infix fun Int.reverse(bitCount: Int): Int {
  var b = 0
  var x = this
  repeat(bitCount) {
    b = b shl 1
    b = b or (x and 1)
    x = x shr 1
  }
  return b
}

private val masks = (2..20).fold(listOf(0, 1)) { acc, _ -> acc + (acc.last() shl 1 or 1) }

internal fun Int.getSlice(msb: Int, lsb: Int): Int = (this and masks[msb]) shr (lsb - 1)

internal fun parseMap(lines: List<String>): List<MirrorMap> =
  lines.split { it == "" }.map { mapLines ->
    val width      = mapLines.first().length
    val height     = mapLines.size
    val rowBits    = mapLines.map { line -> line.parseAsBitset() }
    val columnBits = (0..<width).map { mapLines.map { line -> line[it] }.joinToString(separator = "").parseAsBitset() }
    MirrorMap(width, height, rowBits, columnBits)
  }

internal fun String.parseAsBitset(): Int = replace('.', '0').replace('#', '1').toInt(2)
