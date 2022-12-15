package de.smartsteuer.frank.adventofcode2022

import java.net.URL

fun resource(path: String): URL? = object {}.javaClass.getResource(path)

fun lines(path: String): List<String> = resource(path)?.openStream()?.reader()?.readLines() ?: emptyList()

fun linesSequence(path: String): Sequence<String> {
  return resource(path)?.openStream()?.reader()?.buffered(20_000)?.let { reader ->
    generateSequence { reader.readLine() }
  } ?: emptySequence()
}

fun text(path: String): String = resource(path)?.openStream()?.reader()?.readText() ?: ""

fun Sequence<String>.extractNumbers(regex: Regex): Sequence<List<Int>> =
  map { line ->
    regex.matchEntire(line)?.let { matchResult ->
      matchResult.groupValues.drop(1).map { it.toInt() }
    } ?: throw IllegalArgumentException("could not parse '$line'")
  }

@Suppress("RemoveExplicitTypeArguments")
fun List<IntRange>.merge(other: IntRange): List<IntRange> {
  tailrec fun merge(intervals: List<IntRange>, result: List<IntRange>): List<IntRange> {
    if (intervals.isEmpty()) return result
    val interval = intervals.first()
    return when {
      result.isEmpty() || result.last().last < interval.first -> {
        merge(intervals.drop(1), result.plus<IntRange>(interval))
      }
      else                                                    -> {
        val newLastInterval = IntRange(result.last().first, result.last().last.coerceAtLeast(interval.last))
        merge(intervals.drop(1), result.dropLast(1).plus<IntRange>(newLastInterval))
      }
    }
  }
  return merge(this.plus<IntRange>(other).sortedBy { it.first }, emptyList())
}

fun IntRange.clamp(other: IntRange): IntRange = when {
  this.first > other.last  -> IntRange.EMPTY
  this.last  < other.first -> IntRange.EMPTY
  else                     -> (first.coerceAtLeast(other.first))..(last.coerceAtMost(other.last))
}