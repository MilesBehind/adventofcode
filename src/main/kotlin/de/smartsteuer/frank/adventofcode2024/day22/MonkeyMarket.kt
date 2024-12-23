package de.smartsteuer.frank.adventofcode2024.day22

import de.smartsteuer.frank.adventofcode2024.Day
import de.smartsteuer.frank.adventofcode2024.lines

fun main() {
  MonkeyMarket.execute(lines("/adventofcode2024/day22/secrets.txt"))
}

object MonkeyMarket: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseSecrets().sumOf { secret -> generateSecrets(secret).drop(2_000).first() }

  override fun part2(input: List<String>): Long =
    computeBestSequence(input.parseSecrets()).second.toLong()

  private infix fun Long.mix(other: Long): Long = this xor other

  private fun Long.prune(): Long = this % 16_777_216

  fun generateSecrets(seed: Long): Sequence<Long> =
    generateSequence(seed) { secret ->
      val step1 = (secret mix (secret * 64)).prune()
      val step2 = (step1 mix (step1 / 32)).prune()
      (step2 mix (step2 * 2048)).prune()
    }

  fun computePrices(seed: Long, count: Int = 2_000): List<Int> =
    generateSecrets(seed).take(count + 1).map { (it % 10).toInt() }.toList()

  data class SequenceOfDifferences(val diff1: Int, val diff2: Int, val diff3: Int, val diff4: Int) {
    companion object {
      private val cache = mutableMapOf<List<Int>, SequenceOfDifferences>()

      fun fromList(list: List<Int>): SequenceOfDifferences =
        cache.getOrPut(list) { SequenceOfDifferences(list[0], list[1], list[2], list[3]) }
    }
  }

  fun computeSequencesToPrices(seed: Long, count: Int = 2_000): Map<SequenceOfDifferences, Int> {
    val prices      = computePrices(seed, count)
    val differences = prices.zipWithNext { a, b -> b - a }
    return (prices.drop(1) zip differences).windowed(4).fold(mutableMapOf()) { map, pricesAndDifferences ->
      map.also {
        val sequence = pricesAndDifferences.map { it.second }
        val sequenceOfDifferences = SequenceOfDifferences.fromList(sequence)
        if (sequenceOfDifferences !in map) {
          val price = pricesAndDifferences.last().first
          map[sequenceOfDifferences] = price
        }
      }
    }
  }

  fun computeBestSequence(seeds: List<Long>, count: Int = 2_000): Pair<SequenceOfDifferences, Int> {
    val allSequencesToPrices: List<Map<SequenceOfDifferences, Int>> = seeds.fold(mutableListOf()) { result, seed ->
      result.apply { add(computeSequencesToPrices(seed, count)) }
    }
    val uniqueSequences: Set<SequenceOfDifferences> = allSequencesToPrices.fold(mutableSetOf()) { result, differences ->
      result.apply { addAll(differences.keys) }
    }
    println(uniqueSequences.size)
    val sequencesToMaxPrices = uniqueSequences.map { sequence ->
      sequence to allSequencesToPrices.sumOf { sequencesToPrices ->
        sequencesToPrices[sequence] ?: 0
      }
    }.toMap()
    return sequencesToMaxPrices.maxBy { it.value }.toPair()
  }

  fun List<String>.parseSecrets(): List<Long> =
    map { it.toLong() }
}