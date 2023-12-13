package de.smartsteuer.frank.adventofcode2023.day13

import de.smartsteuer.frank.adventofcode2022.day01.split
import de.smartsteuer.frank.adventofcode2023.lines
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
  val mirrorMaps = parseMap(lines("/adventofcode2023/day13/"))
  measureTime { println("part 1: ${part1(mirrorMaps)}") }.also { println("part 1 took $it") }
  measureTime { println("part 2: ${part2(mirrorMaps)}") }.also { println("part 2 took $it") }
}

internal fun part1(mirrorMaps: List<MirrorMap>): Long {
  return 0
}

internal fun part2(mirrorMaps: List<MirrorMap>): Long {
  return 0
}

internal data class MirrorMap(val width: Int, val height: Int, val rowBits: List<Int>, val columnBits: List<Int>) {
  fun findVerticalMirror(expectedDefects: Int = 0): Int? {
    val mirroredRowBits = rowBits.map { it reverse width }
    return (1..<width).firstOrNull { x ->
      rowBits.zip(mirroredRowBits).forEach { (bits, mirroredBits)  ->
        val relevantBitCount     = min(x, width - x)
        val relevantBits         = (bits         shr (width - 2 * x).coerceAtLeast(0)) clearLeftBits (width - x)
        val relevantMirroredBits = (mirroredBits shl (width -     x).coerceAtLeast(0)) clearLeftBits (width - x)
        println("x = $x, bits = ${bits.asBinary(width)}, mirroredBits = ${mirroredBits.asBinary(width)}")
        println("relevantBitCount = $relevantBitCount, relevantBits = ${relevantBits.asBinary(relevantBitCount)}, relevantMirroredBits = ${relevantMirroredBits.asBinary(relevantBitCount)}")
        //Integer.bitCount(relevantBits xor relevantMirroredBits) == expectedDefects
      }
      false
    }
  }
}

internal fun Int.asBinary(digits: Int) = toString(2).padStart(digits, '0').take(digits)

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

internal val masks = (2..20).fold(listOf(0, 1)) { acc, _ -> acc + (acc.last() shl 1 or 1) }

internal infix fun Int.clearLeftBits(bitCountToRemain: Int): Int = this and masks[bitCountToRemain]

internal fun parseMap(lines: List<String>): List<MirrorMap> =
  lines.split { it == "" }.map { mapLines ->
    val width      = mapLines.first().length
    val height     = mapLines.size
    val rowBits    = mapLines.map { line -> line.parseAsBitset() }
    val columnBits = (0..<width).map { mapLines.map { line -> line[it] }.joinToString(separator = "").parseAsBitset() }
    MirrorMap(width, height, rowBits, columnBits)
  }

internal fun String.parseAsBitset(): Int = replace('.', '0').replace('#', '1').toInt(2)
