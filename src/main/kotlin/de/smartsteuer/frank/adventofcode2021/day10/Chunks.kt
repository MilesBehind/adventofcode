package de.smartsteuer.frank.adventofcode2021.day10

import de.smartsteuer.frank.adventofcode2021.lines
import java.util.*

fun main() {
  val chunks = lines("/adventofcode2021/day10/chunks.txt")
  val scores = score(chunks)
  println("scores = $scores")
  println("score: ${scores.sumOf { it.syntaxScore }}")
  val middleScore = middleScore(scores)
  println("middle score = $middleScore")
}

data class Score(val syntaxScore: Int, val completionScore: Long) {
  override fun toString(): String = "score($syntaxScore, $completionScore)"
}

fun score(chunks: List<String>): List<Score> =
  chunks.map { chunk ->
    val stack = ArrayDeque<Char>()
    chunk.forEach { symbol ->
      when(symbol) {
        '('  -> stack.push(')')
        '['  -> stack.push(']')
        '{'  -> stack.push('}')
        '<'  -> stack.push('>')
        ')'  -> if (symbol != stack.pop()) return@map Score(    3, 0)
        ']'  -> if (symbol != stack.pop()) return@map Score(   57, 0)
        '}'  -> if (symbol != stack.pop()) return@map Score( 1197, 0)
        '>'  -> if (symbol != stack.pop()) return@map Score(25137, 0)
        else -> return@map Score(0, 0)
      }
    }
    return@map Score(0, completionScore(stack))
  }

fun completionScore(stack: Deque<Char>): Long =
  stack.fold(0L) { score, symbol ->
    5 * score + when (symbol) {
      ')'  -> 1
      ']'  -> 2
      '}'  -> 3
      '>'  -> 4
      else -> 0
    }
  }

fun middleScore(scores: List<Score>): Long {
  val completionScores = scores.map { it.completionScore }.filter { it != 0L }.sorted()
  return completionScores[completionScores.size / 2]
}
