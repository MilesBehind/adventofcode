package de.smartsteuer.frank.adventofcode2025.day05

import de.smartsteuer.frank.adventofcode2022.day01.split
import de.smartsteuer.frank.adventofcode2025.Day
import de.smartsteuer.frank.adventofcode2025.lines

fun main() {
  Cafeteria.execute(lines("/adventofcode2025/day05/ingredients.txt"))
}

object Cafeteria: Day<Long> {
  override fun part1(input: List<String>): Long =
    input.parseIngredients().findFreshIngredients().size.toLong()

  override fun part2(input: List<String>): Long =
    input.parseIngredients().mergeAllRanges().sumOf { it.last - it.first + 1 }
}

internal data class Ingredients(val freshIngredients: List<LongRange>, val ingredients: List<Long>) {
  fun findFreshIngredients(): List<Long> =
    ingredients.filter { id -> freshIngredients.any { range -> id in range } }

  fun mergeAllRanges(): List<LongRange> {
    tailrec fun merge(toMerge: List<LongRange>, result: List<LongRange>): List<LongRange> {
      if (toMerge.isEmpty()) return result
      val range = toMerge.first()
      val last = result.last()
      val nextResult = if (range.first <= last.last + 1) result.dropLast(1) + listOf(last.first..maxOf(last.last, range.last)) else result + listOf(range)
      return merge(toMerge.drop(1), nextResult)
    }
    return freshIngredients.sortedBy { it.first }.let { sorted -> merge(sorted.drop(1), sorted.take(1).toList())}
  }
}

internal fun List<String>.parseIngredients(): Ingredients =
  split { it.isEmpty() }.let { (freshIngredients, ingredients) ->
    Ingredients(
      freshIngredients.map { it.split("-").let { (from, to) -> from.toLong()..to.toLong()} },
      ingredients.map { it.toLong() }
    )
  }
