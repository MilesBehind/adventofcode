package de.smartsteuer.frank.adventofcode2022.day20

import de.smartsteuer.frank.adventofcode2022.day20.Day20.part1
import de.smartsteuer.frank.adventofcode2022.day20.Day20.part2
import de.smartsteuer.frank.adventofcode2022.linesSequence
import java.util.*
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
  measureTimeMillis {
    println("day 20, part 1: ${part1(linesSequence("/adventofcode2022/day20/encrypted-coordinates.txt"))}")
  }.also { println("took $it ms") }
  measureTimeMillis {
    println("day 20, part 2: ${part2(linesSequence("/adventofcode2022/day20/encrypted-coordinates.txt"))}")
  }.also { println("took $it ms") }
}

@Suppress("SimplifiableCallChain", "SameParameterValue")
object Day20 {
  fun part1(input: Sequence<String>): Long {
    val numbers = parseEncryptedCoordinates(input)
    numbers.moveNumbers()
    val coordinates = numbers.getGroveCoordinates()
    println(coordinates)
    return coordinates.sum()
  }

  fun part2(input: Sequence<String>): Long {
    val numbers = parseEncryptedCoordinates(input)
    numbers.multiplyNumbers(DECRYPTION_KEY)
    repeat(10) {
      numbers.moveNumbers()
    }
    val coordinates = numbers.getGroveCoordinates()
    println(coordinates)
    return coordinates.sum()
  }

  private const val DECRYPTION_KEY = 811_589_153L

  class CyclicNumbersList(numbers: List<Int>) {

    data class NumberWithIndex(val number: Long, val index: Int) {
      override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NumberWithIndex
        return index == other.index
      }
      override fun hashCode(): Int = index
    }

    private val numbersAndPositions: LinkedList<NumberWithIndex> = LinkedList(numbers.mapIndexed { index, number -> NumberWithIndex(number.toLong(), index) })

    private fun findCurrentIndexByOriginalIndex(originalIndex: Int): Int = numbersAndPositions.indexOfFirst { it.index == originalIndex }

    private fun findCurrentIndexByNumber(number: Long): Int = numbersAndPositions.indexOfFirst { it.number == number }

    private fun wrappedIndex(start: Int, offset: Long): Int {
      val wrappedIndex = (start + offset) % numbersAndPositions.size
      return (if (wrappedIndex < 0) numbersAndPositions.size - abs(wrappedIndex) else wrappedIndex).toInt()
    }

    private fun move(currentIndex: Int) {
      val numberAndOriginalIndex = numbersAndPositions[currentIndex]
      val amount = numberAndOriginalIndex.number
      if (amount != 0L) {
        numbersAndPositions.removeAt(currentIndex)
        numbersAndPositions.add(wrappedIndex(currentIndex, amount), numberAndOriginalIndex)
      }
    }

    fun moveNumbers() {
      for (index in numbersAndPositions.indices) {
        move(findCurrentIndexByOriginalIndex(index))
      }
    }

    fun getGroveCoordinates(): List<Long> =
      listOf(1_000, 2_000, 3_000).map { offset -> numbersAndPositions[(findCurrentIndexByNumber(0) + offset) % numbersAndPositions.size].number }

    fun multiplyNumbers(factor: Long) {
      for (index in numbersAndPositions.indices) {
        val (number, originalIndex) = numbersAndPositions[index]
        numbersAndPositions[index] = NumberWithIndex(number * factor, originalIndex)
      }
    }

    override fun toString(): String = numbersAndPositions.joinToString(prefix = "[", postfix = "]") { (number, index) -> "$number ($index)" }
  }

  private fun parseEncryptedCoordinates(input: Sequence<String>) = CyclicNumbersList(input.map { it.toInt() }.toList())
}
